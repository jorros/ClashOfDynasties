package de.clashofdynasties.game;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.clashofdynasties.models.*;
import de.clashofdynasties.repository.*;
import de.clashofdynasties.service.RoutingService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/game/formations")
@Secured("ROLE_USER")
public class FormationController {
    @Autowired
    private FormationRepository formationRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private RoadRepository roadRepository;

    @Autowired
    private RelationRepository relationRepository;

    @RequestMapping(value = "/{formation}/route", method = RequestMethod.GET)
    public
    @ResponseBody
    ObjectNode calculateRoute(Principal principal, @PathVariable("formation") ObjectId id, @RequestParam ObjectId target) {
        Player player = playerRepository.findByName(principal.getName());
        Formation formation = formationRepository.findById(id);
        City city = cityRepository.findById(target);

        RoutingService routing = new RoutingService(roadRepository, relationRepository);

        if(routing.calculateRoute(formation, city, player))
            return routing.getRoute().toJSON();
        else
            return JsonNodeFactory.instance.objectNode();
    }

    @RequestMapping(value = "/{formation}/move", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void move(Principal principal, @PathVariable("formation") ObjectId formationId, @RequestParam ObjectId target) {
        Formation formation = formationRepository.findById(formationId);
        City city = cityRepository.findById(target);
        Player player = playerRepository.findByName(principal.getName());
        RoutingService routing = new RoutingService(roadRepository, relationRepository);

        if (player.equals(formation.getPlayer())) {
            if (routing.calculateRoute(formation, city, player)) {
                if (formation.isDeployed()) {
                    formation.setRoute(routing.getRoute());
                    formation.getRoute().removeRoad(0);
                    formation.getRoute().setCurrentRoad(roadRepository.findByCities(formation.getLastCity(), formation.getRoute().getNext()));
                    formation.move(70);
                } else {
                    formation.setRoute(routing.getRoute());
                }

                formation.updateTimestamp();
            }
        }
    }

    @RequestMapping(value = "/{formation}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void remove(Principal principal, @PathVariable("formation") ObjectId formationId) {
        Player player = playerRepository.findByName(principal.getName());
        Formation formation = formationRepository.findById(formationId);

        if (formation.getPlayer().equals(player) && formation.isDeployed() && formation.getLastCity().getPlayer().equals(formation.getPlayer())) {
            City city = formation.getLastCity();

            formation.getUnits().forEach(u -> city.addUnit(u));

            formationRepository.remove(formation);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void create(Principal principal, @RequestParam("name") String name, @RequestParam("city") ObjectId cityId, @RequestParam String unitsJson, @RequestParam String injuredUnitsJson) {
        City city = cityRepository.findById(cityId);
        Player player = playerRepository.findByName(principal.getName());

        Formation formation = new Formation();
        formation.setLastCity(city);
        formation.setPlayer(player);
        formation.setX(city.getX());
        formation.setY(city.getY());

        formationRepository.add(formation);

        save(principal, formation.getId(), name, cityId, unitsJson, injuredUnitsJson);
    }

    @RequestMapping(value = "/{formation}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void save(Principal principal, @PathVariable("formation") ObjectId formationId, @RequestParam("name") String name, @RequestParam("city") ObjectId cityId, @RequestParam String unitsJson, @RequestParam String injuredUnitsJson) {
        City city = cityRepository.findById(cityId);
        Player player = playerRepository.findByName(principal.getName());
        ObjectMapper mapper = new ObjectMapper();

        Map<Integer, Integer> units = new HashMap<>();
        Map<Integer, Integer> injuredUnits = new HashMap<>();
        try {
            units = mapper.readValue(unitsJson, new TypeReference<HashMap<Integer, Integer>>() {});
            injuredUnits = mapper.readValue(injuredUnitsJson, new TypeReference<HashMap<Integer, Integer>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (player.equals(city.getPlayer())) {
            Formation formation;
            formation = formationRepository.findById(formationId);

            if (player.equals(formation.getPlayer())) {
                if (formation.getRoute() == null) {
                    List<Unit> sumUnits = new ArrayList<>(formation.getUnits());
                    sumUnits.addAll(city.getUnits());

                    city.clearUnits(false);
                    formation.clearUnits(false);

                    for(Integer blp : units.keySet()) {
                        List<Unit> healthyUnitList;
                        List<Unit> newHealthyUnitList = new ArrayList<>();

                        healthyUnitList = sumUnits.stream().filter(u -> u.getHealth() >= 90 && u.getBlueprint().getId() == blp).collect(Collectors.toList());
                        newHealthyUnitList.addAll(healthyUnitList.subList(0, units.get(blp)));

                        healthyUnitList.removeAll(newHealthyUnitList);

                        healthyUnitList.forEach(city::addUnit);
                        newHealthyUnitList.forEach(formation::addUnit);
                    }

                    for(Integer blp : injuredUnits.keySet()) {
                        List<Unit> injuredUnitList;
                        List<Unit> newInjuredUnitList = new ArrayList<>();

                        injuredUnitList = sumUnits.stream().filter(u -> u.getHealth() < 90).collect(Collectors.toList());
                        newInjuredUnitList.addAll(injuredUnitList.subList(0, injuredUnits.get(blp)));

                        injuredUnitList.removeAll(newInjuredUnitList);

                        injuredUnitList.forEach(city::addUnit);
                        newInjuredUnitList.forEach(formation::addUnit);
                    }

                    formation.setName(name);
                    formation.updateTimestamp();

                    formation.recalculateStrength();
                    formation.recalculateHealth();
                    city.recalculateStrength();
                }
            }
        }
    }
}
