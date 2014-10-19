package de.clashofdynasties.game;

import de.clashofdynasties.models.*;
import de.clashofdynasties.repository.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/game/menus")
@Secured("ROLE_USER")
public class MenuController {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private FormationRepository formationRepository;

    @Autowired
    private UnitBlueprintRepository unitBlueprintRepository;

    @Autowired
    private BuildingBlueprintRepository buildingBlueprintRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CaravanRepository caravanRepository;

    @Autowired
    private CityTypeRepository cityTypeRepository;

    @Autowired
    private RelationRepository relationRepository;

    @Autowired
    private MessageRepository messageRepository;

    @RequestMapping(value = "/formation", method = RequestMethod.GET)
    public String showFormationSetup(ModelMap map, Principal principal, @RequestParam(value = "formation", required = false) ObjectId id, @RequestParam(value = "city", required = false) ObjectId cityID, @RequestParam(required = false) Boolean demography) {
        Player player = playerRepository.findByName(principal.getName());

        Formation formation = null;
        City city = null;
        boolean create = false;

        if (id != null) {
            formation = formationRepository.findById(id);

            if (!formation.getPlayer().equals(player) || !formation.isDeployed() || !formation.getLastCity().getPlayer().equals(formation.getPlayer())) {
                map.addAttribute("name", formation.getName());
                map.addAttribute("units", formation.getUnits());

                HashMap<String, Long> unitsTotal = new HashMap<>();
                HashMap<String, Long> unitsInjured = new HashMap<>();

                List<Integer> classes = formation.getUnits().stream().map(u -> u.getBlueprint().getId()).distinct().collect(Collectors.toList());

                for(int i : classes) {
                    unitsTotal.put(unitBlueprintRepository.findById(i).getName(), formation.getUnits().stream().filter(u -> u.getBlueprint().getId() == i).count());
                    unitsInjured.put(unitBlueprintRepository.findById(i).getName(), formation.getUnits().stream().filter(u -> u.getBlueprint().getId() == i && u.getHealth() < 90).count());
                }

                map.addAttribute("unitsTotal", unitsTotal);
                map.addAttribute("unitsInjured", unitsInjured);

                return "menu/infoFormation";
            }
            else {
                city = formation.getLastCity();
                create = false;
            }
        } else if (cityID != null) {
            city = cityRepository.findById(cityID);

            if (!city.getPlayer().equals(player))
                return null;

            formation = new Formation();
            formation.setName("Neue Formation");

            create = true;
        }

        map.addAttribute("formation", formation);
        map.addAttribute("city", city);
        map.addAttribute("create", create);

        ArrayList<Unit> units = new ArrayList<>(formation.getUnits());
        units.addAll(city.getUnits());

        List<UnitBlueprint> blueprints = units.stream().map(u -> u.getBlueprint()).distinct().collect(Collectors.toList());

        HashMap<Integer, Long> unitMap = new HashMap<>();
        HashMap<Integer, Long> injuredUnitMap = new HashMap<>();

        HashMap<Integer, Long> formationUnitMap = new HashMap<>();
        HashMap<Integer, Long> formationInjuredUnitMap = new HashMap<>();

        for(UnitBlueprint blueprint : blueprints) {
            unitMap.put(blueprint.getId(), units.stream().filter(u -> u.getBlueprint().equals(blueprint) && u.getHealth() >= 90).count());
            injuredUnitMap.put(blueprint.getId(), units.stream().filter(u -> u.getBlueprint().equals(blueprint) && u.getHealth() < 90).count());

            formationUnitMap.put(blueprint.getId(), formation.getUnits().stream().filter(u -> u.getBlueprint().equals(blueprint) && u.getHealth() >= 90).count());
            formationInjuredUnitMap.put(blueprint.getId(), formation.getUnits().stream().filter(u -> u.getBlueprint().equals(blueprint) && u.getHealth() < 90).count());
        }

        map.addAttribute("unitBlueprints", blueprints);
        map.addAttribute("units", unitMap);
        map.addAttribute("injuredUnits", injuredUnitMap);
        map.addAttribute("formationUnits", formationUnitMap);
        map.addAttribute("formationInjuredUnits", formationInjuredUnitMap);

        map.addAttribute("demography", demography != null ? demography : false);

        return "menu/setupFormation";
    }

    @RequestMapping(value = "/units", method = RequestMethod.GET)
    public String showCityUnits(ModelMap map, Principal principal, @RequestParam(value = "city") ObjectId cityID) {
        Player player = playerRepository.findByName(principal.getName());
        City city = cityRepository.findById(cityID);

        map.addAttribute("name", city.getName());
        map.addAttribute("units", city.getUnits());
        map.addAttribute("buildings", city.getBuildings().stream().filter(b -> b.getBlueprint().getDefencePoints() > 0).collect(Collectors.toList()));

        HashMap<String, Long> unitsTotal = new HashMap<>();
        HashMap<String, Long> unitsInjured = new HashMap<>();

        List<Integer> classes = city.getUnits().stream().map(u -> u.getBlueprint().getId()).distinct().collect(Collectors.toList());

        for(int i : classes) {
            unitsTotal.put(unitBlueprintRepository.findById(i).getName(), city.getUnits().stream().filter(u -> u.getBlueprint().getId() == i).count());
            unitsInjured.put(unitBlueprintRepository.findById(i).getName(), city.getUnits().stream().filter(u -> u.getBlueprint().getId() == i && u.getHealth() < 90).count());
        }

        map.addAttribute("unitsTotal", unitsTotal);
        map.addAttribute("unitsInjured", unitsInjured);

        return "menu/infoFormation";
    }

    @RequestMapping(value = "/build", method = RequestMethod.GET)
    public String showBuild(ModelMap map, Principal principal, @RequestParam("city") ObjectId id, @RequestParam boolean demography) {
        City city = cityRepository.findById(id);
        List<BuildingBlueprint> buildingBlueprints = buildingBlueprintRepository.getList();
        List<UnitBlueprint> unitBlueprints = unitBlueprintRepository.getList();

        Collections.sort(buildingBlueprints, (BuildingBlueprint b1, BuildingBlueprint b2) -> ((Integer)b1.getId()).compareTo(b2.getId()));
        Collections.sort(unitBlueprints, (UnitBlueprint b1, UnitBlueprint b2) -> ((Integer)b1.getId()).compareTo(b2.getId()));

        map.addAttribute("city", city);
        map.addAttribute("buildingBlueprints", buildingBlueprints);
        map.addAttribute("unitBlueprints", unitBlueprints);
        map.addAttribute("player", playerRepository.findByName(principal.getName()));
        map.addAttribute("demography", demography);

        if(city.getBuildingConstruction() != null) {
            double neededProduction = city.getBuildingConstruction().getRequiredProduction();
            double currentProduction = city.getBuildingConstruction().getProduction();
            long delta = Math.round(neededProduction - currentProduction);

            map.addAttribute("productionPercent", (long)(currentProduction / neededProduction * 100));

            delta /= city.getProductionRate();

            int day = (int) TimeUnit.SECONDS.toDays(delta);
            long hours = TimeUnit.SECONDS.toHours(delta) - TimeUnit.DAYS.toHours(day);
            long minute = TimeUnit.SECONDS.toMinutes(delta) - TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(delta));

            String timeDescription = "";
            if(day > 0)
                timeDescription += day + "T ";
            if(hours > 0)
                timeDescription += hours + "h ";
            if(minute > 0)
                timeDescription += minute + "min ";

            if(day == 0 && hours == 0 && minute == 0)
                timeDescription = "unter 1min ";

            map.addAttribute("productionTime", timeDescription);

        }

        return "menu/build";
    }

    @RequestMapping(value = "/report", method = RequestMethod.GET)
    public String showReport(ModelMap map, Principal principal, @RequestParam("city") ObjectId id) {
        City city = cityRepository.findById(id);
        Player player = playerRepository.findByName(principal.getName());

        ArrayList<Player> alliedForces = new ArrayList<>();
        ArrayList<Player> hostileForces = new ArrayList<>();

        map.addAttribute("city", city);
        map.addAttribute("formations", formationRepository.findByCity(city));
        map.addAttribute("player", player);
        map.addAttribute("alliedForces", alliedForces);
        map.addAttribute("hostileForces", hostileForces);

        for(Party party : city.getReport().getParties()) {
            if(party.getPlayer().equals(player))
                alliedForces.add(party.getPlayer());
            else {
                Relation relation = relationRepository.findByPlayers(party.getPlayer(), player);

                if (relation == null)
                    hostileForces.add(party.getPlayer());
                else {
                    if (relation.getRelation() <= 1)
                        hostileForces.add(party.getPlayer());
                    else
                        alliedForces.add(party.getPlayer());
                }
            }
        }

        return "menu/report";
    }

    @RequestMapping(value = "/caravan", method = RequestMethod.GET)
    public String showCaravan(ModelMap map, Principal principal, @RequestParam(required = false) ObjectId point1, @RequestParam(required = false) ObjectId point2, @RequestParam(value = "caravan", required = false) ObjectId id, @RequestParam(required = false) Boolean demography) {
        Player player = playerRepository.findByName(principal.getName());
        List<Item> items = itemRepository.getList();

        Collections.sort(items, (Item i1, Item i2) -> ((Integer)i1.getId()).compareTo(i2.getId()));

        if (id != null) {
            Caravan caravan = caravanRepository.findById(id);

            if (caravan.getPoint1().getPlayer().equals(player) || caravan.getPoint2().getPlayer().equals(player)) {
                map.addAttribute("caravan", caravan);
                map.addAttribute("point1", caravan.getPoint1());
                map.addAttribute("point2", caravan.getPoint2());
                map.addAttribute("create", false);
            }
        } else {
            Caravan caravan = new Caravan();
            caravan.setName("Neue Karawane");

            map.addAttribute("caravan", caravan);
            map.addAttribute("point1", cityRepository.findById(point1));
            map.addAttribute("point2", cityRepository.findById(point2));
            map.addAttribute("create", true);
        }

        map.addAttribute("items", items);
        map.addAttribute("player", player);
        map.addAttribute("demography", demography != null ? demography : false);

        return "menu/caravan";
    }

    @RequestMapping(value = "/store", method = RequestMethod.GET)
    public String showStore(ModelMap map, Principal principal, @RequestParam("city") ObjectId id) {
        City city = cityRepository.findById(id);
        List<Item> items = itemRepository.getList();

        Collections.sort(items, (Item i1, Item i2) -> ((Integer)i1.getId()).compareTo(i2.getId()));

        map.addAttribute("city", city);
        map.addAttribute("items", items);
        map.addAttribute("player", playerRepository.findByName(principal.getName()));

        return "menu/store";
    }

    @RequestMapping(value = "/ranking", method = RequestMethod.GET)
    public String showRanking(ModelMap map, Principal principal) {
        Player player = playerRepository.findByName(principal.getName());
        List<Player> players = playerRepository.getList().stream().filter(p -> p.getStatistic() != null).filter(p -> !p.getName().equalsIgnoreCase("jorros") && !p.getName().equalsIgnoreCase("cheeterhakim")).collect(Collectors.toList());

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

    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    public String showMessages(ModelMap map, Principal principal, @RequestParam(required = false) ObjectId pid) {
        Player player = playerRepository.findByName(principal.getName());
        List<Player> players = new ArrayList<>(playerRepository.getList().stream().filter(p -> !p.equals(player)).filter(p -> !p.isComputer()).filter(Player::isActivated).filter(p -> !p.getName().equalsIgnoreCase("jorros") && !p.getName().equalsIgnoreCase("cheeterhakim")).collect(Collectors.toList()));

        Map<ObjectId, String> lastMessages = new HashMap<>();
        Map<ObjectId, Long> playerSort = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");

        if(pid != null) {
            Player other = playerRepository.findById(pid);

            List<Message> messages = messageRepository.findByPlayers(player, other);
            Collections.sort(messages, (Message m1, Message m2) -> Long.compare(m1.getTimestamp(), m2.getTimestamp()));

            messages.stream().filter(m -> m.getTo().equals(player)).forEach(m -> m.setUnread(false));

            map.addAttribute("other", other);
            map.addAttribute("messages", messages);
        }

        for(Player other : players) {
            List<Message> messages = messageRepository.findByPlayers(player, other);
            Collections.sort(messages, (Message m1, Message m2) -> Long.compare(m2.getTimestamp(), m1.getTimestamp()));

            if(messages.size() == 0) {
                lastMessages.put(other.getId(), "Keine Nachrichten");

                playerSort.put(other.getId(), 0L);
            }
            else {
                if(messages.stream().filter(m -> m.isUnread() && m.getTo().equals(player)).count() > 0)
                    lastMessages.put(other.getId(), "Neue Nachricht");
                else {
                    Date date = new Date(messages.get(0).getTimestamp());

                    lastMessages.put(other.getId(), sdf.format(date));
                }

                playerSort.put(other.getId(), messages.get(0).getTimestamp());
            }
        }

        Collections.sort(players, (Player p1, Player p2) -> Long.compare(playerSort.get(p2.getId()), playerSort.get(p1.getId())));

        map.addAttribute("player", player);
        map.addAttribute("players", players);
        map.addAttribute("lastMessages", lastMessages);

        return "menu/messages";
    }

    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    public String showSettings(ModelMap map, Principal principal) {
        Player player = playerRepository.findByName(principal.getName());

        map.addAttribute("player", player);

        return "menu/settings";
    }

    @RequestMapping(value = "/demography", method = RequestMethod.GET)
    public String showDemography(ModelMap map, Principal principal, @RequestParam(required = false) String p) {
        Player player = playerRepository.findByName(principal.getName());
        if(p == null || p.equals("1")) {
            List<City> cities = cityRepository.findByPlayer(player);

            map.addAttribute("cities", cities);
            map.addAttribute("page", 1);
        } else if(p.equals("2")) {
            List<Caravan> caravans = caravanRepository.findByPlayer(player);

            map.addAttribute("caravans", caravans);
            map.addAttribute("page", 2);
        } else if(p.equals("3")) {
            List<Formation> formations = formationRepository.findByPlayer(player);

            map.addAttribute("formations", formations);
            map.addAttribute("page", 3);
        }

        map.addAttribute("player", player);

        return "menu/demography";
    }

    @RequestMapping(value = "/diplomacy", method = RequestMethod.GET)
    public String showDiplomacy(ModelMap map, Principal principal, @RequestParam(required = false) ObjectId pid) {
        Player player = playerRepository.findByName(principal.getName());
        List<Player> players = playerRepository.getList().stream().filter(p -> !p.equals(player)).filter(p -> !p.isComputer()).filter(Player::isActivated).filter(p -> !p.getName().equalsIgnoreCase("jorros") && !p.getName().equalsIgnoreCase("cheeterhakim")).collect(Collectors.toList());

        HashMap<ObjectId, Integer> relations = new HashMap<>();
        for(Player p : players) {
            Relation relation = relationRepository.findByPlayers(player, p);

            if(relation == null)
                relations.put(p.getId(), 1);
            else
                relations.put(p.getId(), relation.getRelation());
        }

        map.addAttribute("player", player);
        map.addAttribute("players", players);
        map.addAttribute("relations", relations);

        if(pid != null && playerRepository.exists(pid)) {
            Player other = playerRepository.findById(pid);
            Relation relation = relationRepository.findByPlayers(player, other);

            if(relation == null) {
                relation = new Relation();
                relation.setPlayer1(player);
                relation.setPlayer2(playerRepository.findById(pid));
                relation.setRelation(1);
                relationRepository.add(relation);
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

    @RequestMapping(value = "/briefing", method = RequestMethod.GET)
    public String showBriefing(ModelMap map, Principal principal) {
        Player player = playerRepository.findByName(principal.getName());

        map.addAttribute("ytid", "9SgYw9gBqBg");

        return "menu/briefing";
    }

    @RequestMapping(value = "/editcity", method = RequestMethod.GET)
    public String showEditCity(ModelMap map) {
        List<CityType> cityTypes = cityTypeRepository.getList();
        Collections.sort(cityTypes, (CityType c1, CityType c2) -> ((Integer)c1.getId()).compareTo(c2.getId()));

        map.addAttribute("cityTypes", cityTypes);

        return "menu/editcity";
    }

    @RequestMapping(value = "/editbuildings", method = RequestMethod.GET)
    public String showEditBuildings(ModelMap map) {
        List<Item> items = itemRepository.getList();
        List<BuildingBlueprint> buildingBlueprints = buildingBlueprintRepository.getList();
        List<CityType> cityTypes = cityTypeRepository.getList();

        Collections.sort(items, (Item i1, Item i2) -> ((Integer)i1.getId()).compareTo(i2.getId()));
        Collections.sort(buildingBlueprints, (BuildingBlueprint b1, BuildingBlueprint b2) -> ((Integer)b1.getId()).compareTo(b2.getId()));
        Collections.sort(cityTypes, (CityType ct1, CityType ct2) -> ((Integer)ct1.getId()).compareTo(ct2.getId()));

        map.addAttribute("buildingBlueprints", buildingBlueprints);
        map.addAttribute("items", items);
        map.addAttribute("cityTypes", cityTypes);

        return "menu/editbuildings";
    }

    @RequestMapping(value = "/editplayers", method = RequestMethod.GET)
    public String showEditPlayers(ModelMap map) {
        map.addAttribute("players", playerRepository.getList());

        return "menu/editplayers";
    }

    @RequestMapping(value = "/editunits", method = RequestMethod.GET)
    public String showEditUnits(ModelMap map) {
        List<UnitBlueprint> unitBlueprints = unitBlueprintRepository.getList();

        Collections.sort(unitBlueprints, (UnitBlueprint b1, UnitBlueprint b2) -> ((Integer)b1.getId()).compareTo(b2.getId()));

        map.addAttribute("unitBlueprints", unitBlueprints);

        return "menu/editunits";
    }
}
