package de.clashofdynasties.game;

import de.clashofdynasties.logic.PlayerLogic;
import de.clashofdynasties.models.*;
import de.clashofdynasties.repository.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/game/cities")
public class CityController {
    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private BuildingBlueprintRepository buildingBlueprintRepository;

    @Autowired
    private UnitBlueprintRepository unitBlueprintRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemTypeRepository itemTypeRepository;

    @Autowired
    private CityTypeRepository cityTypeRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private BiomeRepository biomeRepository;

    @Autowired
    private RoadRepository roadRepository;

    @Autowired
    private PlayerLogic playerLogic;

    @RequestMapping(value = "/{city}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    public void remove(@PathVariable("city") ObjectId id) {
        roadRepository.delete(roadRepository.findByCity(id));
        cityRepository.delete(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    public void create(@RequestParam int x, @RequestParam int y, @RequestParam(required = false) String name, @RequestParam(required = false) Integer type, @RequestParam(required = false) Integer capacity, @RequestParam(required = false) Integer resource, @RequestParam(required = false) Integer biome, @RequestParam(required = false) ObjectId player) {
        City city = new City();
        city.setName("Neue Stadt");
        city.setCapacity(0);
        city.setHealth(100);
        city.setBiome(biomeRepository.findOne(1));
        city.setPlayer(playerRepository.findAll().stream().filter(Player::isComputer).findFirst().get());
        city.setX(x);
        city.setY(y);
        city.setType(cityTypeRepository.findOne(1));
        city.setResource(resourceRepository.findOne(1));
        city.setPopulation(5);

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
        save(city.getId(), name, type, capacity, resource, biome, player);
    }

    @RequestMapping(value = "/{city}/build", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void build(Principal principal, @PathVariable("city") ObjectId id, @RequestParam int type, @RequestParam int blueprint, @RequestParam int count) {
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
                count = 1;

                if(blp.getRequiredBiomes().contains(city.getBiome()) && (blp.getRequiredResource() == null || blp.getRequiredResource().equals(city.getResource())) && city.getBuildings().size() < city.getCapacity() && (blp.getMaxCount() == 0 || blp.getMaxCount() > city.countBuildings(blp.getId())))
                    construction.setBlueprint(blp);
            }
            else if(type == 1) {
                if(city.getBuildings().stream().filter(b -> b.getBlueprint().getId() == 7 || b.getBlueprint().getId() == 15).count() > 0) {
                    construction.setBlueprint(unitBlueprintRepository.findOne(blueprint));
                }
            }

            if(construction.getBlueprint() != null) {
                if (player.getCoins() < construction.getBlueprint().getPrice() * count && count > 1)
                    count = (int) Math.floor(player.getCoins() / construction.getBlueprint().getPrice());
                else if (player.getCoins() < construction.getBlueprint().getPrice() * count)
                    count = 0;

                if (count > 0) {
                    construction.setCount(count);
                    city.setBuildingConstruction(construction);
                    cityRepository.save(city);
                    player.addCoins(-construction.getBlueprint().getPrice());
                    playerRepository.save(player);
                }
            }
        }
    }

    @RequestMapping(value = "/{city}/build", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void stopBuild(Principal principal, @PathVariable("city") ObjectId id) {
        Player player = playerRepository.findByName(principal.getName());
        City city = cityRepository.findOne(id);

        if(city.getPlayer().equals(player) && city.getBuildingConstruction() != null) {
            double neededProduction = city.getBuildingConstruction().getRequiredProduction();
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

    @RequestMapping(value = "/{city}/reset", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    public void reset(@PathVariable("city") ObjectId id) {
        City city = cityRepository.findOne(id);
        city.setBuildings(new ArrayList<>());
        city.setItems(new HashMap<>());
        city.setUnits(new ArrayList<>());
        city.setHealth(100);
        city.setPopulation(5);
        city.updateTimestamp();
        city.setType(cityTypeRepository.findOne(1));

        cityRepository.save(city);
    }

    @RequestMapping(value = "/{city}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    public void save(@PathVariable("city") ObjectId id, @RequestParam(required = false) String name, @RequestParam(required = false) Integer type, @RequestParam(required = false) Integer capacity, @RequestParam(required = false) Integer resource, @RequestParam(required = false) Integer biome, @RequestParam(required = false) ObjectId player) {
        City city = cityRepository.findOne(id);
        Player newPlayer = null;

        if (city == null)
            return;

        if (name != null)
            city.setName(name);

        if (capacity != null)
            city.setCapacity(capacity);

        if (resource != null)
            city.setResource(resourceRepository.findOne(resource));

        if (biome != null)
            city.setBiome(biomeRepository.findOne(biome));

        if (player != null) {
            newPlayer = playerRepository.findOne(player);

            city.setPlayer(newPlayer);
        }

        if (type != null && city.getType().getId() != type) {
            city.setType(cityTypeRepository.findOne(type));
            if(!city.getPlayer().isComputer())
                playerLogic.updateFOW(city.getPlayer());
        }

        city.updateTimestamp();

        cityRepository.save(city);

        if(newPlayer != null) {
            if(!city.getPlayer().isComputer())
                playerLogic.updateFOW(city.getPlayer());

            if(!newPlayer.isComputer() && !city.getPlayer().equals(newPlayer))
                playerLogic.updateFOW(newPlayer);
        }
    }
}
