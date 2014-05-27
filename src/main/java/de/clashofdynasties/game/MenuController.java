package de.clashofdynasties.game;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.clashofdynasties.models.*;
import de.clashofdynasties.repository.*;
import de.clashofdynasties.service.CounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/game/menus")
public class MenuController {
    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    CityRepository cityRepository;

    @Autowired
    FormationRepository formationRepository;

    @Autowired
    UnitBlueprintRepository unitBlueprintRepository;

    @Autowired
    BuildingBlueprintRepository buildingBlueprintRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    CounterService counterService;

    @Autowired
    CaravanRepository caravanRepository;

    @Autowired
    CityTypeRepository cityTypeRepository;

    @RequestMapping(value = "/top", method = RequestMethod.GET)
    @ResponseBody
    public ObjectNode getTop(Principal principal, @RequestParam boolean editor, @RequestParam long timestamp) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();

        if(!editor) {
            Player player = playerRepository.findByName(principal.getName());
            List<City> cities = cityRepository.findByPlayer(player);
            List<Formation> formations = formationRepository.findByPlayer(player);
            List<Caravan> caravans = caravanRepository.findByPlayer(player);

            int people = 0;
            int balance = 0;
            ArrayNode events = JsonNodeFactory.instance.arrayNode();

            for (City city : cities) {
                people += city.getPopulation();
                balance += city.getIncome() - city.getOutcome();
            }

            if(player.getEvents() != null) {
                player.getEvents().stream().filter(e -> e.toJSON(timestamp) != null).forEach(e -> events.add(e.toJSON(timestamp)));
            }

            node.put("coins", player.getCoins());
            node.put("people", people);
            node.put("balance", balance);
            node.put("cityNum", cities.size());
            node.put("formationNum", formations.size());
            node.put("caravanNum", caravans.size());
            node.put("ranking", 1);
            node.put("events", events);
        }

        return node;
    }

    @RequestMapping(value = "/event", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void removeEvent(Principal principal, @RequestParam long timestamp, @RequestParam String type, @RequestParam int city) {
        Player player = playerRepository.findByName(principal.getName());

        if(player.getEvents() != null) {
            player.getEvents().removeIf(e -> e.getTimestamp() == timestamp && e.getCity().getId() == city && e.getType().equalsIgnoreCase(type));
            playerRepository.save(player);
        }
    }

    @RequestMapping(value = "/scroll", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updateScrollPosition(Principal principal, @RequestParam int x, @RequestParam int y) {
        Player player = playerRepository.findByName(principal.getName());

        player.setLastScrollX(x);
        player.setLastScrollY(y);

        playerRepository.save(player);
    }

    @RequestMapping(value = "/scroll", method = RequestMethod.GET)
    public @ResponseBody ObjectNode getScrollPosition(Principal principal) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        Player player = playerRepository.findByName(principal.getName());

        node.put("x", player.getLastScrollX());
        node.put("y", player.getLastScrollY());

        return node;
    }

    @RequestMapping(value = "/formation", method = RequestMethod.GET)
    public String showFormationSetup(ModelMap map, Principal principal, @RequestParam(value = "formation", required = false) Integer id, @RequestParam(value = "city", required = false) Integer cityID) {
        Player player = playerRepository.findByName(principal.getName());

        if (id != null) {
            Formation formation = formationRepository.findOne(id);
            map.addAttribute("formation", formation);
            map.addAttribute("city", formation.getLastCity());

            if (!formation.getPlayer().equals(player) || !formation.isDeployed())
                return "menu/infoFormation";
        } else if (cityID != null) {
            City city = cityRepository.findOne(cityID);

            if (!city.getPlayer().equals(player))
                return null;

            Formation formation = new Formation();
            formation.setName("Neue Formation");

            map.addAttribute("formation", formation);
            map.addAttribute("city", city);
        }

        return "menu/setupFormation";
    }

    @RequestMapping(value = "/build", method = RequestMethod.GET)
    public String showBuild(ModelMap map, Principal principal, @RequestParam("city") int id) {
        City city = cityRepository.findOne(id);

        map.addAttribute("city", city);
        map.addAttribute("buildingBlueprints", buildingBlueprintRepository.findAll());
        map.addAttribute("unitBlueprints", unitBlueprintRepository.findAll());
        map.addAttribute("player", playerRepository.findByName(principal.getName()));

        if(city.getBuildingConstruction() != null) {
            double neededProduction = city.getBuildingConstruction().getBlueprint().getRequiredProduction();
            double currentProduction = city.getBuildingConstruction().getProduction();
            long delta = Math.round(neededProduction - currentProduction);

            map.addAttribute("productionPercent", (long)(currentProduction / neededProduction * 100));

            int day = (int) TimeUnit.SECONDS.toDays(delta);
            long hours = TimeUnit.SECONDS.toHours(delta) - (day * 24);
            long minute = TimeUnit.SECONDS.toMinutes(delta) - (hours * 60);

            String timeDescription = "";
            if(day > 0)
                timeDescription += day + "T ";
            if(hours > 0)
                timeDescription += hours + "h ";
            if(minute > 0)
                timeDescription += minute + "m ";

            map.addAttribute("productionTime", timeDescription);

        }

        return "menu/build";
    }

    @RequestMapping(value = "/report", method = RequestMethod.GET)
    public String showReport(ModelMap map, Principal principal, @RequestParam("city") int id) {
        City city = cityRepository.findOne(id);

        map.addAttribute("city", city);
        map.addAttribute("formations", formationRepository.findByCity(id));
        map.addAttribute("player", playerRepository.findByName(principal.getName()));

        return "menu/report";
    }

    @RequestMapping(value = "/caravan", method = RequestMethod.GET)
    public String showCaravan(ModelMap map, Principal principal, @RequestParam(required = false) Integer point1, @RequestParam(required = false) Integer point2, @RequestParam(value = "caravan", required = false) Integer id) {
        Player player = playerRepository.findByName(principal.getName());

        if (id != null) {
            Caravan caravan = caravanRepository.findOne(id);

            if (caravan.getPlayer().equals(player)) {
                map.addAttribute("caravan", caravan);
                map.addAttribute("point1", caravan.getPoint1());
                map.addAttribute("point2", caravan.getPoint2());
            }
        } else {
            Caravan caravan = new Caravan();
            caravan.setName("Neue Karawane");

            map.addAttribute("caravan", caravan);
            map.addAttribute("point1", cityRepository.findOne(point1));
            map.addAttribute("point2", cityRepository.findOne(point2));
        }

        map.addAttribute("items", itemRepository.findAll(new Sort(Sort.Direction.ASC, "_id")));

        return "menu/caravan";
    }

    @RequestMapping(value = "/store", method = RequestMethod.GET)
    public String showStore(ModelMap map, Principal principal, @RequestParam("city") int id) {
        City city = cityRepository.findOne(id);
        List<Item> items = itemRepository.findAll(new Sort(Sort.Direction.ASC, "_id"));

        map.addAttribute("city", city);
        map.addAttribute("items", items);
        map.addAttribute("player", playerRepository.findByName(principal.getName()));

        HashMap<Integer, Integer> production = new HashMap<>();

        if(city.getBuildings() != null) {
            for(Building building : city.getBuildings()) {
                Item item = building.getBlueprint().getProduceItem();
                if(item != null) {
                    if(production.containsKey(item.getId()))
                        production.put(item.getId() - 1, production.get(item.getId() - 1) + (int)Math.floor(building.getBlueprint().getProducePerStep() * 60));
                    else
                        production.put(item.getId() - 1, (int)Math.floor(building.getBlueprint().getProducePerStep() * 60));
                }
            }
        }
        map.addAttribute("production", production);

        HashMap<Integer, Integer> consumption = new HashMap<>();
        HashMap<Integer, Integer> balance = new HashMap<>();

        for(Item item : items) {
            int consume = 0;
            switch(item.getId()) {
                case 1:
                    consume = (int)Math.floor(city.getPopulation() * city.getType().getConsumeBasic() * 0.001 * 60);
                    break;
                case 2:
                    consume = (int)Math.floor(city.getPopulation() * city.getType().getConsumeBasic() * 0.001 * 60);
                    break;
                case 3:
                    consume = (int)Math.floor(city.getPopulation() * city.getType().getConsumeLuxury1() * 0.001 * 60);
                    break;
                case 4:
                    consume = (int)Math.floor(city.getPopulation() * city.getType().getConsumeLuxury1() * 0.001 * 60);
                    break;
                case 5:
                    consume = (int)Math.floor(city.getPopulation() * city.getType().getConsumeLuxury3() * 0.001 * 60);
                    break;
                case 6:
                    consume = (int)Math.floor(city.getPopulation() * city.getType().getConsumeLuxury3() * 0.001 * 60);
                    break;
                case 7:
                    consume = (int)Math.floor(city.getPopulation() * city.getType().getConsumeLuxury2() * 0.001 * 60);
                    break;
                case 8:
                    consume = (int)Math.floor(city.getPopulation() * city.getType().getConsumeLuxury3() * 0.001 * 60);
                    break;
                case 9:
                    consume = (int)Math.floor(city.getPopulation() * city.getType().getConsumeLuxury2() * 0.001 * 60);
                    break;
                case 10:
                    consume = (int)Math.floor(city.getPopulation() * city.getType().getConsumeLuxury3() * 0.001 * 60);
                    break;
                case 11:
                    consume = (int)Math.floor(city.getPopulation() * city.getType().getConsumeLuxury2() * 0.001 * 60);
                    break;
                case 12:
                    consume = (int)Math.floor(city.getPopulation() * city.getType().getConsumeLuxury2() * 0.001 * 60);
                    break;
            }

            consumption.put(item.getId() - 1, consume);
            balance.put(item.getId() - 1, production.getOrDefault(item.getId() - 1, 0) - consume);
        }
        map.addAttribute("consumption", consumption);
        map.addAttribute("balance", balance);

        return "menu/store";
    }

    @RequestMapping(value = "/ranking", method = RequestMethod.GET)
    public String showRanking(ModelMap map, Principal principal) {
        HashMap<Integer, Integer> economy = new HashMap<>();
        HashMap<Integer, Integer> demography = new HashMap<>();
        HashMap<Integer, Integer> military = new HashMap<>();
        HashMap<Integer, Integer> total = new HashMap<>();
        HashMap<Integer, Player> playerMap = new HashMap<>();
        int highestDemography = 0;
        int highestEconomy = 0;
        int highestMilitary = 0;
        int highestDemographyVal = 0;
        int highestEconomyVal = 0;
        int highestMilitaryVal = 0;
        List<Integer> ranking = new LinkedList<>();

        Player player = playerRepository.findByName(principal.getName());
        List<Player> players = playerRepository.findAll();
        List<City> cities = cityRepository.findAll();
        List<Formation> formations = formationRepository.findAll();
        List<Caravan> caravans = caravanRepository.findAll();

        for(Player p : players) {
            economy.put(p.getId(), 0);
            demography.put(p.getId(), 0);
            military.put(p.getId(), 0);
            playerMap.put(p.getId(), p);
        }

        for(City city : cities) {
            int p = city.getPlayer().getId();

            demography.put(p, demography.get(p) + city.getPopulation());
            demography.put(p, demography.get(p) + city.getSatisfaction());
            military.put(p, military.get(p) + city.getDefencePoints());

            if(city.getBuildings() != null)
                city.getBuildings().forEach(c -> economy.put(p, economy.get(p) + new Double(c.getBlueprint().getProducePerStep() * 100).intValue()));

            if(city.getUnits() != null)
                military.put(p, military.get(p) + city.getUnits().size() * 10);
        }

        for(Formation formation : formations) {
            int p = formation.getPlayer().getId();

            military.put(p, military.get(p) + 50);

            if(formation.getUnits() != null)
                military.put(p, military.get(p) + formation.getUnits().size() * 10);
        }

        for(Caravan caravan : caravans) {
            int p = caravan.getPlayer().getId();

            economy.put(p, economy.get(p) + 10);
        }

        for(Player p : players) {
            economy.put(p.getId(), economy.get(p.getId()) + p.getCoins());
            total.put(p.getId(), military.get(p.getId()) + demography.get(p.getId()) + economy.get(p.getId()));

            if(demography.get(p.getId()) > highestDemographyVal) {
                highestDemographyVal = demography.get(p.getId());
                highestDemography = p.getId();
            }
            if(military.get(p.getId()) > highestMilitaryVal) {
                highestMilitaryVal = military.get(p.getId());
                highestMilitary = p.getId();
            }
            if(economy.get(p.getId()) > highestEconomyVal) {
                highestEconomyVal = economy.get(p.getId());
                highestEconomy = p.getId();
            }
        }

        List<Object> list = new LinkedList<Object>(total.entrySet());
        Collections.sort(list, (o1, o2) -> ((Comparable) ((Map.Entry) (o1)).getValue()).compareTo(((Map.Entry) (o2)).getValue()) * -1);

        for (Object aList : list) {
            Map.Entry entry = (Map.Entry) aList;
            if ((Integer) entry.getKey() > 1)
                ranking.add((Integer) entry.getKey());
        }

        map.addAttribute("player", player);
        map.addAttribute("players", playerMap);
        map.addAttribute("economy", economy);
        map.addAttribute("demography", demography);
        map.addAttribute("military", military);
        map.addAttribute("total", total);
        map.addAttribute("highestDemography", highestDemography);
        map.addAttribute("highestEconomy", highestEconomy);
        map.addAttribute("highestMilitary", highestMilitary);
        map.addAttribute("ranking", ranking);

        return "menu/ranking";
    }

    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    public String showSettings(ModelMap map) {
        return "menu/settings";
    }

    @RequestMapping(value = "/demography", method = RequestMethod.GET)
    public String showDemography(ModelMap map) {
        return "menu/demography";
    }

    @RequestMapping(value = "/diplomacy", method = RequestMethod.GET)
    public String showDiplomacy(ModelMap map) {
        return "menu/diplomacy";
    }

    @RequestMapping(value = "/editcity", method = RequestMethod.GET)
    public String showEditCity(ModelMap map) {
        map.addAttribute("cityTypes", cityTypeRepository.findAll(new Sort(Sort.Direction.ASC, "_id")));

        return "menu/editcity";
    }

    @RequestMapping(value = "/editbuildings", method = RequestMethod.GET)
    public String showEditBuildings(ModelMap map) {
        map.addAttribute("buildingBlueprints", buildingBlueprintRepository.findAll(new Sort(Sort.Direction.ASC, "_id")));

        return "menu/editbuildings";
    }

    @RequestMapping(value = "/editresources", method = RequestMethod.GET)
    public String showEditResources(ModelMap map) {
        map.addAttribute("buildingBlueprints", buildingBlueprintRepository.findAll(new Sort(Sort.Direction.ASC, "_id")));
        map.addAttribute("items", itemRepository.findAll(new Sort(Sort.Direction.ASC, "_id")));

        return "menu/editresources";
    }

    @RequestMapping(value = "/editunits", method = RequestMethod.GET)
    public String showEditUnits(ModelMap map) {
        map.addAttribute("unitBlueprints", unitBlueprintRepository.findAll(new Sort(Sort.Direction.ASC, "_id")));

        return "menu/editunits";
    }

    @RequestMapping(value = "/timestamp", method = RequestMethod.GET)
    public
    @ResponseBody
    long getTimestamp() {
        return System.currentTimeMillis();
    }
}
