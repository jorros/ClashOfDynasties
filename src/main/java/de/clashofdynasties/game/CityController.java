package de.clashofdynasties.game;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.clashofdynasties.models.*;
import de.clashofdynasties.repository.*;
import org.bson.types.ObjectId;
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
    UnitRepository unitRepository;

    @Autowired
    ItemTypeRepository itemTypeRepository;

    @Autowired
    CityTypeRepository cityTypeRepository;

    @Autowired
    ResourceRepository resourceRepository;

    @Autowired
    BiomeRepository biomeRepository;

    @Autowired
    RelationRepository relationRepository;

    @Autowired
    RoadRepository roadRepository;

    @RequestMapping(method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, ObjectNode> getCities(Principal principal, @RequestParam boolean editor, @RequestParam long timestamp) {
        Player player = playerRepository.findByName(principal.getName());

        List<City> cities = cityRepository.findAll();
        List<Formation> formations = formationRepository.findAll();
        HashMap<String, ObjectNode> data = new HashMap<String, ObjectNode>();

        for (City city : cities) {
            for (Formation formation : formations) {
                if (formation.getRoute() == null && formation.getLastCity().equals(city)) {
                    if (city.getFormations() == null)
                        city.setFormations(new ArrayList<Formation>());

                    city.getFormations().add(formation);
                }
            }

            // Diplomatie
            if(city.getPlayer().equals(player))
                city.setDiplomacy(4);
            else {
                Relation relation = relationRepository.findByPlayers(player.getId(), city.getPlayer().getId());
                if(relation == null)
                    city.setDiplomacy(1);
                else
                    city.setDiplomacy(relation.getRelation());
            }

            // Sicht
            if(city.getDiplomacy() >= 3)
                city.setVisible(true);
            else {
                city.setVisible(isVisible(city, player, city.getType().getId()));
            }

            data.put(city.getId().toHexString(), city.toJSON(editor, timestamp));
        }

        return data;
    }

    private boolean isVisible(City city, Player player, int level) {
        boolean visible = false;

        for (Road r : roadRepository.findByCity(city.getId())) {
            if(r.getPoint1().getPlayer().equals(player) || r.getPoint2().getPlayer().equals(player))
                visible = true;
            else if(level > 1) {
                if(r.getPoint1().equals(city))
                    visible = isVisible(r.getPoint2(), player, level - 1);
                else
                    visible = isVisible(r.getPoint1(), player, level - 1);
            }

            if(visible)
                break;
        }

        return visible;
    }

    @RequestMapping(value = "/{city}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    public void remove(Principal principal, @PathVariable("city") ObjectId id) {
        cityRepository.delete(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    public void create(HttpServletRequest request, Principal principal, @RequestParam int x, @RequestParam int y, @RequestParam(required = false) String name, @RequestParam(required = false) Integer type, @RequestParam(required = false) Integer capacity, @RequestParam(required = false) Integer resource, @RequestParam(required = false) Integer biome, @RequestParam(required = false) ObjectId player) {
        City city = new City();
        city.setName("Neu Stadt");
        city.setCapacity(0);
        city.setHealth(100);
        city.setBiome(biomeRepository.findOne(1));
        city.setPlayer(playerRepository.findAll().stream().filter(p -> p.isComputer()).findFirst().get());
        city.setX(x);
        city.setY(y);
        city.setType(cityTypeRepository.findOne(1));
        city.setResource(resourceRepository.findOne(1));

        List<ItemType> types = city.getRequiredItemTypes();

        types.add(itemTypeRepository.findOne(1));
        types.add(itemTypeRepository.findOne(2));

        int i;
        i = (int) (Math.random() * 3 + 1);
        if (i == 1)
            types.add(itemTypeRepository.findOne(4));
        else if (i == 2)
            types.add(itemTypeRepository.findOne(6));
        else
            types.add(itemTypeRepository.findOne(7));

        i = (int) (Math.random() * 2 + 1);
        if (i == 1)
            types.add(itemTypeRepository.findOne(3));
        else
            types.add(itemTypeRepository.findOne(5));

        city.setRequiredItemTypes(types);

        city.updateTimestamp();

        cityRepository.save(city);
        save(request, principal, city.getId(), name, type, capacity, resource, biome, player);
    }

    @RequestMapping(value = "/{city}/build", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void build(Principal principal, @PathVariable("city") ObjectId id, @RequestParam int type, @RequestParam int blueprint) {
        Player player = playerRepository.findByName(principal.getName());
        City city = cityRepository.findOne(id);

        if(city.getPlayer().equals(player)) {
            BuildingConstruction construction = new BuildingConstruction();
            construction.setProduction(0);

            if(city.getBuildingConstruction() != null) {
                stopBuild(principal, id);
                player = playerRepository.findByName(principal.getName());
                city = cityRepository.findOne(id);
            }

            if(type == 0) {
                BuildingBlueprint blp = buildingBlueprintRepository.findOne(blueprint);

                if(blp.getRequiredBiomes().contains(city.getBiome()) && (blp.getRequiredResource() == null || blp.getRequiredResource().equals(city.getResource())) && city.getBuildings().size() < city.getCapacity())
                    construction.setBlueprint(blp);
            }
            else if(type == 1) {
                if(city.getBuildings().stream().filter(b -> b.getBlueprint().getId() == 7 || b.getBlueprint().getId() == 15).count() > 0)
                    construction.setBlueprint(unitBlueprintRepository.findOne(blueprint));
            }

            if(construction.getBlueprint() != null && player.getCoins() >= construction.getBlueprint().getPrice()) {
                city.setBuildingConstruction(construction);
                cityRepository.save(city);
                player.addCoins(-construction.getBlueprint().getPrice());
                playerRepository.save(player);
            }
        }
    }

    @RequestMapping(value = "/{city}/build", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void stopBuild(Principal principal, @PathVariable("city") ObjectId id) {
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

    @RequestMapping(value = "/{city}/consumption", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void toggleConsumption(Principal principal, @PathVariable("city") ObjectId id, @RequestParam("item") int itemId) {
        City city = cityRepository.findOne(id);
        Item item = itemRepository.findOne(itemId);
        Player player = playerRepository.findByName(principal.getName());

        if(city.getPlayer().equals(player)) {
            city.toggleConsumption(item);

            cityRepository.save(city);
        }
    }

    @RequestMapping(value = "/{city}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void save(HttpServletRequest request, Principal principal, @PathVariable("city") ObjectId id, @RequestParam(required = false) String name, @RequestParam(required = false) Integer type, @RequestParam(required = false) Integer capacity, @RequestParam(required = false) Integer resource, @RequestParam(required = false) Integer biome, @RequestParam(required = false) ObjectId player) {
        City city = cityRepository.findOne(id);

        if (city == null)
            return;

        if (name != null)
            city.setName(name);

        if (capacity != null && request.isUserInRole("ROLE_ADMIN"))
            city.setCapacity(capacity);

        if (resource != null && request.isUserInRole("ROLE_ADMIN"))
            city.setResource(resourceRepository.findOne(resource));

        if (biome != null && request.isUserInRole("ROLE_ADMIN"))
            city.setBiome(biomeRepository.findOne(biome));

        if (player != null && request.isUserInRole("ROLE_ADMIN"))
            city.setPlayer(playerRepository.findOne(player));

        if (type != null && city.getType().getId() != type && request.isUserInRole("ROLE_ADMIN"))
            city.setType(cityTypeRepository.findOne(type));

        city.updateTimestamp();

        cityRepository.save(city);
    }
}
