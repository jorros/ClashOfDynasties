package de.clashofdynasties.game;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.clashofdynasties.models.*;
import de.clashofdynasties.repository.*;
import de.clashofdynasties.service.RoutingService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/game/formations")
public class FormationController {
    @Autowired
    FormationRepository formationRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    CityRepository cityRepository;

    @Autowired
    UnitRepository unitRepository;

    @Autowired
    RoadRepository roadRepository;

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
    public void create(Principal principal, @RequestParam("name") String name, @RequestParam("city") ObjectId cityId, @RequestParam("units[]") List<ObjectId> unitsId) {
        City city = cityRepository.findById(cityId);
        Player player = playerRepository.findByName(principal.getName());

        Formation formation = new Formation();
        formation.setLastCity(city);
        formation.setPlayer(player);
        formation.setX(city.getX());
        formation.setY(city.getY());

        formationRepository.add(formation);

        save(principal, formation.getId(), name, cityId, unitsId);
    }

    @RequestMapping(value = "/{formation}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void save(Principal principal, @PathVariable("formation") ObjectId formationId, @RequestParam("name") String name, @RequestParam("city") ObjectId cityId, @RequestParam("units[]") List<ObjectId> unitsId) {
        City city = cityRepository.findById(cityId);
        Player player = playerRepository.findByName(principal.getName());

        if (player.equals(city.getPlayer())) {
            Formation formation;
            formation = formationRepository.findById(formationId);

            if (player.equals(formation.getPlayer())) {
                if (formation.getRoute() == null) {
                    for (Unit unit : formation.getUnits()) {
                        if (!unitsId.contains(unit.getId())) {
                            formation.removeUnit(unit);
                            city.addUnit(unit);
                        }
                    }
                    for (ObjectId unitId : unitsId) {
                        Unit unit = unitRepository.findById(unitId);

                        if (city.getUnits().contains(unit))
                            city.removeUnit(unit);

                        formation.addUnit(unit);
                    }

                    formation.setName(name);
                    formation.updateTimestamp();
                }
            }
        }
    }
}
