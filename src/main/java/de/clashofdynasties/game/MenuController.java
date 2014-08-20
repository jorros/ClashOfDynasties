package de.clashofdynasties.game;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.clashofdynasties.models.*;
import de.clashofdynasties.repository.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
    CaravanRepository caravanRepository;

    @Autowired
    CityTypeRepository cityTypeRepository;

    @Autowired
    RelationRepository relationRepository;

    @Autowired
    EventRepository eventRepository;

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

            List<Event> eventList = eventRepository.findByPlayer(player);
            if(eventList != null && eventList.size() > 0) {
                eventList.stream().filter(e -> e.toJSON(timestamp) != null).forEach(e -> events.add(e.toJSON(timestamp)));
            }

            node.put("coins", player.getCoins());
            node.put("people", people);
            node.put("balance", balance);
            node.put("cityNum", cities.size());
            node.put("formationNum", formations.size());
            node.put("caravanNum", caravans.size());
            node.put("ranking", player.getStatistic() == null ? "Unplatziert" : Integer.valueOf(player.getStatistic().getRank()).toString());
            node.put("events", events);
        }

        return node;
    }

    @RequestMapping(value = "/event", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void removeEvent(Principal principal, @RequestParam ObjectId id) {
        Player player = playerRepository.findByName(principal.getName());
        Event event = eventRepository.findOne(id);

        if(event != null && event.getPlayer().equals(player)) {
            eventRepository.delete(event);
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
    public String showFormationSetup(ModelMap map, Principal principal, @RequestParam(value = "formation", required = false) ObjectId id, @RequestParam(value = "city", required = false) ObjectId cityID) {
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
    public String showBuild(ModelMap map, Principal principal, @RequestParam("city") ObjectId id) {
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

            delta /= city.getProductionRate();

            int day = (int) TimeUnit.SECONDS.toDays(delta);
            long hours = TimeUnit.SECONDS.toHours(delta) - (day * 24);
            long minute = TimeUnit.SECONDS.toMinutes(delta) - (hours * 60);

            String timeDescription = "";
            if(day > 0)
                timeDescription += day + "T ";
            if(hours > 0)
                timeDescription += hours + "h ";
            if(minute > 0)
                timeDescription += minute + "min ";
            else
                timeDescription = "unter 1min ";

            map.addAttribute("productionTime", timeDescription);

        }

        return "menu/build";
    }

    @RequestMapping(value = "/report", method = RequestMethod.GET)
    public String showReport(ModelMap map, Principal principal, @RequestParam("city") ObjectId id) {
        City city = cityRepository.findOne(id);

        map.addAttribute("city", city);
        map.addAttribute("formations", formationRepository.findByCity(id));
        map.addAttribute("player", playerRepository.findByName(principal.getName()));

        return "menu/report";
    }

    @RequestMapping(value = "/caravan", method = RequestMethod.GET)
    public String showCaravan(ModelMap map, Principal principal, @RequestParam(required = false) ObjectId point1, @RequestParam(required = false) ObjectId point2, @RequestParam(value = "caravan", required = false) ObjectId id) {
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
    public String showStore(ModelMap map, Principal principal, @RequestParam("city") ObjectId id) {
        City city = cityRepository.findOne(id);
        List<Item> items = itemRepository.findAll(new Sort(Sort.Direction.ASC, "_id"));

        map.addAttribute("city", city);
        map.addAttribute("items", items);
        map.addAttribute("player", playerRepository.findByName(principal.getName()));

        return "menu/store";
    }

    @RequestMapping(value = "/ranking", method = RequestMethod.GET)
    public String showRanking(ModelMap map, Principal principal) {
        Player player = playerRepository.findByName(principal.getName());
        List<Player> players = playerRepository.findAll();
        players.removeIf(p -> p.getStatistic() == null);
        Collections.sort(players, (Player p1, Player p2) -> Integer.compare(p1.getStatistic().getRank(), p2.getStatistic().getRank()));

        Player maxDemography = players.stream().max((Player p1, Player p2) -> Integer.compare(p1.getStatistic().getDemography(), p2.getStatistic().getDemography())).get();
        Player maxEconomy = players.stream().max((Player p1, Player p2) -> Integer.compare(p1.getStatistic().getEconomy(), p2.getStatistic().getEconomy())).get();
        Player maxMilitary = players.stream().max((Player p1, Player p2) -> Integer.compare(p1.getStatistic().getMilitary(), p2.getStatistic().getMilitary())).get();

        map.addAttribute("player", player);
        map.addAttribute("players", players);
        map.addAttribute("maxDemography", maxDemography);
        map.addAttribute("maxEconomy", maxEconomy);
        map.addAttribute("maxMilitary", maxMilitary);

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
    public String showDiplomacy(ModelMap map, Principal principal, @RequestParam(required = false) ObjectId pid) {
        Player player = playerRepository.findByName(principal.getName());
        List<Player> players = playerRepository.findAll();
        players.removeIf(p -> p.equals(player));
        players.removeIf(Player::isComputer);

        HashMap<ObjectId, Integer> relations = new HashMap<>();
        for(Player p : players) {
            Relation relation = relationRepository.findByPlayers(player.getId(), p.getId());

            if(relation == null)
                relations.put(p.getId(), 1);
            else
                relations.put(p.getId(), relation.getRelation());
        }

        map.addAttribute("player", player);
        map.addAttribute("players", players);
        map.addAttribute("relations", relations);

        if(pid != null && playerRepository.exists(pid)) {
            Player other = playerRepository.findOne(pid);
            Relation relation = relationRepository.findByPlayers(player.getId(), other.getId());

            if(relation == null) {
                relation = new Relation();
                relation.setCaravans(new ArrayList<>());
                relation.setPendingCaravans(new ArrayList<>());
                relation.setPlayer1(player);
                relation.setPlayer2(playerRepository.findOne(pid));
                relation.setRelation(1);
                relationRepository.save(relation);

                relation = relationRepository.findByPlayers(player.getId(), other.getId());

                if(relation == null)
                    relation = relationRepository.findByPlayers(player.getId(), other.getId());
            }

            map.addAttribute("relation", relation);
            map.addAttribute("otherPlayer", other);

            if(relation.getTicksLeft() != null) {
                int hours = relation.getTicksLeft() / 3600;
                int minutes = (relation.getTicksLeft() - hours * 3600) / 60;
                map.addAttribute("hoursLeft", hours);
                map.addAttribute("minutesLeft", minutes);
            }
        }


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
        map.addAttribute("items", itemRepository.findAll(new Sort(Sort.Direction.ASC, "_id")));

        return "menu/editbuildings";
    }

    @RequestMapping(value = "/editplayers", method = RequestMethod.GET)
    public String showEditPlayers(ModelMap map) {
        map.addAttribute("players", playerRepository.findAll());

        return "menu/editplayers";
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
