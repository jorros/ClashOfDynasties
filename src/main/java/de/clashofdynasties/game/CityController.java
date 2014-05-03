package de.clashofdynasties.game;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.clashofdynasties.models.*;
import de.clashofdynasties.repository.*;
import de.clashofdynasties.service.CounterService;
import jdk.nashorn.internal.ir.RuntimeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/game/cities")
public class CityController {
    @Autowired
    CityRepository cityRepository;

    @Autowired
    BuildingBlueprintRepository buildingBlueprintRepository;

    @Autowired
    UnitBlueprintRepository unitBlueprintRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    NationRepository nationRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    FormationRepository formationRepository;

    @Autowired
    CounterService counterService;

    @Autowired
    UnitRepository unitRepository;

    @Autowired
    ItemTypeRepository itemTypeRepository;

    @Autowired
    CityTypeRepository cityTypeRepository;

    @Autowired
    ResourceRepository resourceRepository;

    @Autowired
    BiomeRepository biomeRepository;

    @RequestMapping(method = RequestMethod.GET)
    public
    @ResponseBody
    Map<Integer, ObjectNode> getCities(Principal principal, @RequestParam boolean editor, @RequestParam long timestamp) {
        Player player = playerRepository.findByName(principal.getName());

        List<City> cities = cityRepository.findAll();
        List<Formation> formations = formationRepository.findAll();
        HashMap<Integer, ObjectNode> data = new HashMap<Integer, ObjectNode>();

        for (City city : cities) {
            for (Formation formation : formations) {
                if (formation.getRoute() == null && formation.getLastCity().equals(city)) {
                    if (city.getFormations() == null)
                        city.setFormations(new ArrayList<Formation>());

                    city.getFormations().add(formation);
                }
            }

            // Diplomatie setzen
            if (player.equals(city.getPlayer())) {
                city.setDiplomacy(1);

                if(city.getPopulation() <= 0)
                    city.setSatisfaction(-1);
            }
            // Wenn Spieler neutral
            else if (city.getPlayer().getId() == 1) {
                city.setDiplomacy(4);
                city.setSatisfaction(-1);
            } else {
                city.setDiplomacy(3);
                city.setSatisfaction(-1);
            }

            data.put(city.getId(), city.toJSON(editor, timestamp));
        }

        return data;
    }

    @RequestMapping(value = "/{city}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    public void remove(Principal principal, @PathVariable("city") int id) {
        cityRepository.delete(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    public void create(HttpServletRequest request, Principal principal, @RequestParam int x, @RequestParam int y, @RequestParam(required = false) String name, @RequestParam(required = false) Integer type, @RequestParam(required = false) Integer capacity, @RequestParam(required = false) Integer resource) {
        City city = new City();
        city.setId(counterService.getNextSequence("city"));
        city.setName("Neu - " + city.getId());
        city.setCapacity(0);
        city.setHealth(100);
        city.setBiome(biomeRepository.findOne(1));
        city.setPlayer(playerRepository.findOne(1));
        city.setX(x);
        city.setY(y);
        city.setType(cityTypeRepository.findOne(1));
        city.setResource(resourceRepository.findOne(1));
        city.updateTimestamp();

        cityRepository.save(city);
        save(request, principal, city.getId(), name, type, capacity, resource);
    }

    @RequestMapping(value = "/{city}/build", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void build(Principal principal, @PathVariable("city") int id, @RequestParam int type, @RequestParam int blueprint) {
        Player player = playerRepository.findByName(principal.getName());
        City city = cityRepository.findOne(id);

        if(city.getPlayer().equals(player)) {
            BuildingConstruction construction = new BuildingConstruction();
            construction.setProduction(0);

            if(city.getBuildingConstruction() != null) {
                stopBuild(principal, id);
            }

            if(type == 0) {
                construction.setBlueprint(buildingBlueprintRepository.findOne(blueprint));
            }
            else if(type == 1) {
                construction.setBlueprint(unitBlueprintRepository.findOne(blueprint));
            }

            if(player.getCoins() >= construction.getBlueprint().getPrice()) {
                city.setBuildingConstruction(construction);
                cityRepository.save(city);
                player.addCoins(-construction.getBlueprint().getPrice());
                playerRepository.save(player);
            }
        }
    }

    @RequestMapping(value = "/{city}/build", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void stopBuild(Principal principal, @PathVariable("city") int id) {
        Player player = playerRepository.findByName(principal.getName());
        City city = cityRepository.findOne(id);

        if(city.getPlayer().equals(player) && city.getBuildingConstruction() != null) {
            double neededProduction = city.getBuildingConstruction().getBlueprint().getRequiredProduction();
            double currentProduction = city.getBuildingConstruction().getProduction();

            player.addCoins(Math.floor(city.getBuildingConstruction().getBlueprint().getPrice() * (1 - currentProduction / neededProduction)));

            city.setBuildingConstruction(null);

            cityRepository.save(city);
            playerRepository.save(player);
        }
    }

    @RequestMapping(value = "/{city}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void save(HttpServletRequest request, Principal principal, @PathVariable("city") int id, @RequestParam(required = false) String name, @RequestParam(required = false) Integer type, @RequestParam(required = false) Integer capacity, @RequestParam(required = false) Integer resource) {
        City city = cityRepository.findOne(id);

        if (city == null)
            return;

        if (name != null)
            city.setName(name);

        if (capacity != null && request.isUserInRole("ROLE_ADMIN"))
            city.setCapacity(capacity);

        if (resource != null && request.isUserInRole("ROLE_ADMIN"))
            city.setResource(resourceRepository.findOne(resource));

        if (type != null && city.getType().getId() != type && request.isUserInRole("ROLE_ADMIN")) {
            city.setType(cityTypeRepository.findOne(type));
            List<ItemType> types = city.getRequiredItemTypes();

            if (types == null)
                types = new ArrayList<ItemType>();
            else
                types.clear();

            int i;
            switch (type) {
                case 3:
                    i = (int) (Math.random() * 2 + 1);
                    if (i == 1)
                        types.add(itemTypeRepository.findOne(3));
                    else
                        types.add(itemTypeRepository.findOne(5));

                case 2:
                    i = (int) (Math.random() * 3 + 1);
                    if (i == 1)
                        types.add(itemTypeRepository.findOne(4));
                    else if (i == 2)
                        types.add(itemTypeRepository.findOne(6));
                    else
                        types.add(itemTypeRepository.findOne(7));

                case 1:
                    types.add(itemTypeRepository.findOne(1));
                    types.add(itemTypeRepository.findOne(2));
                    break;

                case 4:
                    break;
            }
            city.setRequiredItemTypes(types);
        }

        city.updateTimestamp();

        cityRepository.save(city);
    }
}
