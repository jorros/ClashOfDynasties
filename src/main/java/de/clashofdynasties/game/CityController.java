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
@Secured("ROLE_USER")
public class CityController {
    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private BuildingBlueprintRepository buildingBlueprintRepository;

    @Autowired
    private UnitBlueprintRepository unitBlueprintRepository;

    @Autowired
    private BuildingRepository buildingRepository;

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
    private UnitRepository unitRepository;

    @Autowired
    private EventRepository eventRepository;

    @RequestMapping(value = "/{city}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    public void remove(@PathVariable("city") ObjectId id) {
        City city = cityRepository.findById(id);
        roadRepository.remove(roadRepository.findByCity(city));
        cityRepository.remove(city);
    }

    private void generateRandomRequiredItems(City city) {
        city.addRequiredItemType(itemTypeRepository.findById(1));
        city.addRequiredItemType(itemTypeRepository.findById(2));

        int i;
        i = (int) (Math.random() * 3 + 1);
        if (i == 1)
            city.addRequiredItemType(itemTypeRepository.findById(4));
        else if (i == 2)
            city.addRequiredItemType(itemTypeRepository.findById(6));
        else
            city.addRequiredItemType(itemTypeRepository.findById(7));

        i = (int) (Math.random() * 2 + 1);
        if (i == 1)
            city.addRequiredItemType(itemTypeRepository.findById(3));
        else
            city.addRequiredItemType(itemTypeRepository.findById(5));
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    public void create(@RequestParam int x, @RequestParam int y, @RequestParam(required = false) String name, @RequestParam(required = false) Integer type, @RequestParam(required = false) Integer capacity, @RequestParam(required = false) Integer resource, @RequestParam(required = false) Integer biome, @RequestParam(required = false) ObjectId player) {
        City city = new City();
        city.setName("Neue Stadt");
        city.setCapacity(0);
        city.setHealth(100);
        city.setBiome(biomeRepository.findById(1));
        city.setPlayer(playerRepository.findComputer());
        city.setX(x);
        city.setY(y);
        city.setType(cityTypeRepository.findById(1));
        city.setResource(resourceRepository.findById(1));
        city.setPopulation(5);

        generateRandomRequiredItems(city);

        city.updateTimestamp();

        cityRepository.add(city);

        save(city.getId(), name, type, capacity, resource, biome, player);
    }

    @RequestMapping(value = "/{city}/build", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void build(Principal principal, @PathVariable("city") ObjectId id, @RequestParam int type, @RequestParam int blueprint, @RequestParam int count) {
        Player player = playerRepository.findByName(principal.getName());
        City city = cityRepository.findById(id);

        if(city.getPlayer().equals(player)) {
            BuildingConstruction construction = new BuildingConstruction();
            construction.setProduction(0);

            if(type == 0) {
                BuildingBlueprint blp = buildingBlueprintRepository.findById(blueprint);
                count = 1;

                if(blp.getRequiredBiomes().contains(city.getBiome()) && (blp.getRequiredResource() == null || blp.getRequiredResource().equals(city.getResource())) && city.getBuildings().size() < city.getCapacity() && (blp.getMaxCount() == 0 || blp.getMaxCount() > city.countBuildings(blp)))
                    construction.setBlueprint(blp);
            }
            else if(type == 1) {
                if(city.getBuildings().stream().filter(b -> b.getBlueprint().getId() == 7 || b.getBlueprint().getId() == 15).count() > 0) {
                    construction.setBlueprint(unitBlueprintRepository.findById(blueprint));
                }
            }

            if(construction.getBlueprint().getNation() != null && !construction.getBlueprint().getNation().equals(player.getNation()))
                construction.setBlueprint(null);

            if(construction.getBlueprint() != null) {
                if(city.getBuildingConstruction() != null) {
                    stopBuild(principal, id);
                    player = playerRepository.findByName(principal.getName());
                    city = cityRepository.findById(id);
                }

                if (player.getCoins() < construction.getBlueprint().getPrice() * count && count > 1)
                    count = (int) Math.floor(player.getCoins() / construction.getBlueprint().getPrice());
                else if (player.getCoins() < construction.getBlueprint().getPrice() * count)
                    count = 0;

                if (count > 0) {
                    construction.setCount(count);
                    city.setBuildingConstruction(construction);
                    player.addCoins(-construction.getBlueprint().getPrice() * count);

                    if(type == 0 && blueprint == 5) {
                        for(Player other : playerRepository.getList()) {
                            if(!other.equals(player))
                                eventRepository.add(new Event("Wonder", player.getName() + " baut ein Weltwunder", "Der Spieler " + player.getName() + " baut ein Weltwunder in " + city.getName() + ". Bei Abschluss gewinnt er das Spiel.", city, other));
                        }
                    }
                }
            }
        }
    }

    @RequestMapping(value = "/{city}/alias", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void setAlias(Principal principal, @PathVariable("city") ObjectId id, @RequestParam String alias) {
        Player player = playerRepository.findByName(principal.getName());
        City city = cityRepository.findById(id);

        if(city.getPlayer().equals(player)) {
            city.setAlias(alias);
        }
    }

    @RequestMapping(value = "/{city}/destroy", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void destroy(Principal principal, @PathVariable("city") ObjectId id, @RequestParam int type, @RequestParam int blueprint, @RequestParam int count) {
        Player player = playerRepository.findByName(principal.getName());
        City city = cityRepository.findById(id);

        if(city.getPlayer().equals(player)) {
            if(type == 0) {
                BuildingBlueprint buildingBlueprint = buildingBlueprintRepository.findById(blueprint);

                if(city.getBuildings().stream().filter(b -> b.getBlueprint().equals(buildingBlueprint)).count() > 0) {
                    Building building = city.getBuildings().stream().filter(b -> b.getBlueprint().equals(buildingBlueprint)).findFirst().get();
                    city.removeBuilding(building);
                    buildingRepository.remove(building);

                    city.recalculateStrength();

                    player.addCoins(buildingBlueprint.getPrice() * 0.5);
                }
            } else {
                UnitBlueprint unitBlueprint = unitBlueprintRepository.findById(blueprint);

                long num = city.getUnits().stream().filter(u -> u.getBlueprint().equals(unitBlueprint)).count();

                if(num > 0) {
                    if(count > num)
                        count = (int)num;

                    for(int i = 0; i < count; i++) {
                        Unit unit = city.getUnits().stream().filter(u -> u.getBlueprint().equals(unitBlueprint)).findFirst().get();

                        city.removeUnit(unit);
                        unitRepository.remove(unit);

                        player.addCoins(unitBlueprint.getPrice() * 0.5);
                    }

                    city.recalculateStrength();
                }
            }
        }
    }

    @RequestMapping(value = "/{city}/build", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void stopBuild(Principal principal, @PathVariable("city") ObjectId id) {
        Player player = playerRepository.findByName(principal.getName());
        City city = cityRepository.findById(id);

        if(city.getPlayer().equals(player) && city.getBuildingConstruction() != null) {
            double neededProduction = city.getBuildingConstruction().getRequiredProduction();
            double currentProduction = city.getBuildingConstruction().getProduction();

            player.addCoins(Math.floor(city.getBuildingConstruction().getBlueprint().getPrice() * city.getBuildingConstruction().getCount() * (1 - currentProduction / neededProduction)));

            city.setBuildingConstruction(null);
        }
    }

    @RequestMapping(value = "/{city}/consumption", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void toggleConsumption(Principal principal, @PathVariable("city") ObjectId id, @RequestParam("item") int itemId) {
        City city = cityRepository.findById(id);
        Item item = itemRepository.findById(itemId);
        Player player = playerRepository.findByName(principal.getName());

        if(city.getPlayer().equals(player)) {
            city.toggleConsumption(item);
        }
    }

    @RequestMapping(value = "/{city}/reset", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    public void reset(@PathVariable("city") ObjectId id) {
        City city = cityRepository.findById(id);
        city.clearBuildings(true);
        city.getItems().clear();
        city.clearUnits(true);
        city.setHealth(100);
        city.setPopulation(10);
        city.updateTimestamp();
        city.setType(cityTypeRepository.findById(1));
        city.setAlias("");
        city.setFire(false);
        city.setPlague(false);
        city.setReport(null);
        city.clearStoppedConsumption();

        city.clearRequiredItemTypes();
        generateRandomRequiredItems(city);
    }

    @RequestMapping(value = "/{city}/prebuild", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    public void preBuild(@PathVariable("city") ObjectId id) {
        City city = cityRepository.findById(id);

        city.clearBuildings(true);
        for(int i = 0; i < 5; i++) {
            Building building = new Building();
            building.setHealth(100);
            building.setBlueprint(buildingBlueprintRepository.findById(9));
            buildingRepository.add(building);

            city.addBuilding(building);
        }
    }

    @RequestMapping(value = "/{city}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    public void save(@PathVariable("city") ObjectId id, @RequestParam(required = false) String name, @RequestParam(required = false) Integer type, @RequestParam(required = false) Integer capacity, @RequestParam(required = false) Integer resource, @RequestParam(required = false) Integer biome, @RequestParam(required = false) ObjectId player) {
        City city = cityRepository.findById(id);
        Player newPlayer = null;

        if (city == null)
            return;

        if (name != null)
            city.setName(name);

        if (capacity != null)
            city.setCapacity(capacity);

        if (resource != null)
            city.setResource(resourceRepository.findById(resource));

        if (biome != null)
            city.setBiome(biomeRepository.findById(biome));

        if (player != null) {
            newPlayer = playerRepository.findById(player);

            city.setPlayer(newPlayer);
        }

        if (type != null && city.getType().getId() != type) {
            city.setType(cityTypeRepository.findById(type));
            if(!city.getPlayer().isComputer()) {
                city.getPlayer().setSightUpdate(true);
            }
        }

        city.updateTimestamp();

        if(newPlayer != null) {
            if(!city.getPlayer().isComputer()) {
                city.getPlayer().setSightUpdate(true);
            }

            if(!newPlayer.isComputer() && !city.getPlayer().equals(newPlayer)) {
                newPlayer.setSightUpdate(true);
            }
        }
    }
}
