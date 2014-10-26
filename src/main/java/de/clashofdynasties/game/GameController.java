package de.clashofdynasties.game;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.clashofdynasties.models.*;
import de.clashofdynasties.repository.*;
import de.clashofdynasties.service.LogicService;
import de.clashofdynasties.service.RoutingService;
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
import java.util.Map;

@Controller
@RequestMapping("/game/core")
@Secured("ROLE_USER")
public class GameController {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private FormationRepository formationRepository;

    @Autowired
    private RelationRepository relationRepository;

    @Autowired
    private RoadRepository roadRepository;

    @Autowired
    private CaravanRepository caravanRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private BuildingBlueprintRepository buildingBlueprintRepository;

    @Autowired
    private BiomeRepository biomeRepository;

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    @Secured("ROLE_USER")
    public @ResponseBody
    Map<String, Object> getUpdate(Principal principal, @RequestParam boolean editor, @RequestParam long timestamp) {
        HashMap<String, Object> data = new HashMap<>();

        if(principal != null) {
            Player player = playerRepository.findByName(principal.getName());
            RoutingService routing = new RoutingService(roadRepository, relationRepository);

            List<City> cities = cityRepository.getList();
            List<Road> roads = roadRepository.getList();
            List<Formation> formations = formationRepository.getList();
            List<Caravan> caravans = caravanRepository.getList();
            List<Event> events = eventRepository.findByPlayer(player);

            HashMap<String, ObjectNode> cityMap = new HashMap<>();
            HashMap<String, ObjectNode> roadMap = new HashMap<>();
            HashMap<String, ObjectNode> formationMap = new HashMap<>();
            HashMap<String, ObjectNode> caravanMap = new HashMap<>();
            HashMap<String, Object> topMap = new HashMap<>();
            List<Object> eventList = new ArrayList<>();
            List<Object> objectiveList = new ArrayList<>();

            int people = 0;
            int balance = 0;
            int numCities = 0;
            int numFormations = 0;
            int numCaravans = 0;

            for (City city : cities) {
                if (city.getPlayer().equals(player)) {
                    people += city.getPopulation();
                    balance += city.getIncome() - city.getOutcome();
                    numCities++;
                }

                cityMap.put(city.getId().toHexString(), city.toJSON(editor, timestamp, player));
            }

            roads.stream().filter(road -> road.isVisible(player) || editor).forEach(road -> {
                roadMap.put(road.getId().toHexString(), road.toJSON(timestamp));
            });

            for (Formation formation : formations) {
                if (!editor && formation.isVisible(player)) {
                    // Deploy Status ermitteln
                    if (formation.getRoute() != null) {
                        routing.setRoute(formation.getRoute());
                        formation.getRoute().setTime(routing.calculateTime());
                    }

                    if (player.equals(formation.getPlayer())) {
                        numFormations++;
                        balance -= formation.getCosts();
                    }

                    formationMap.put(formation.getId().toHexString(), formation.toJSON(timestamp, player));
                }
            }

            for (Caravan caravan : caravans) {
                if (!editor && caravan.isVisible(player) && !caravan.isPaused()) {
                    if (player.equals(caravan.getPlayer())) {
                        numCaravans++;
                        balance -= caravan.getCost();
                    }

                    caravanMap.put(caravan.getId().toHexString(), caravan.toJSON(timestamp));
                }
            }

            topMap.put("coins", player.getCoins());
            topMap.put("people", people);
            topMap.put("balance", balance);
            topMap.put("cityNum", numCities);
            topMap.put("formationNum", numFormations);
            topMap.put("caravanNum", numCaravans);
            topMap.put("ranking", player.getStatistic() == null ? "Unplatziert" : Integer.valueOf(player.getStatistic().getRank()).toString());

            if (events != null && events.size() > 0) {
                events.stream().filter(e -> e.toJSON(timestamp) != null).forEach(e -> eventList.add(e.toJSON(timestamp)));
            }

            if (player.getLevel() == 0 || !player.getObjectives().isEmpty()) {
                if (player.getLevel() == 0 || player.getObjectives().stream().filter(o -> !o.isCompleted()).count() == 0) {
                    player.setLevel(player.getLevel() + 1);

                    player.getObjectives().clear();

                    Biome desert = biomeRepository.findById(1);
                    Biome grassland = biomeRepository.findById(2);
                    Biome coast = biomeRepository.findById(3);
                    Biome forest = biomeRepository.findById(4);
                    Biome mountains = biomeRepository.findById(5);

                    City start = cityRepository.findByPlayer(player).get(0);
                    BuildingBlueprint blp, blp2;

                    switch (player.getLevel()) {
                        case 1:
                            blp = buildingBlueprintRepository.findById(18);

                            if(start.getBiome().equals(desert) || start.getBiome().equals(mountains))
                                blp = buildingBlueprintRepository.findById(17);

                            Objective A = new Objective();
                            A.setBuilding(blp);
                            A.setCount(1);
                            A.setCity(start);
                            player.getObjectives().add(A);

                            data.put("brief", true);
                            break;

                        case 2:
                            blp = buildingBlueprintRepository.findById(20);

                            if(player.getNation().getId() == 2)
                                blp = buildingBlueprintRepository.findById(21);

                            Objective B = new Objective();
                            B.setBuilding(blp);
                            B.setCount(1);
                            B.setCity(start);
                            player.getObjectives().add(B);

                            data.put("brief", true);
                            break;

                        case 3:
                            blp = buildingBlueprintRepository.findById(4);

                            Objective H = new Objective();
                            H.setBuilding(blp);
                            H.setCount(1);
                            H.setCity(start);
                            player.getObjectives().add(H);

                            data.put("brief", true);
                            break;

                        case 4:
                            blp = buildingBlueprintRepository.findById(1);

                            Objective C = new Objective();
                            C.setBuilding(blp);
                            C.setCount(2);
                            C.setCity(start);
                            player.getObjectives().add(C);

                            data.put("brief", true);
                            break;

                        case 5:
                            blp = buildingBlueprintRepository.findById(18);

                            if(start.getBiome().equals(desert) || start.getBiome().equals(mountains))
                                blp = buildingBlueprintRepository.findById(17);

                            Objective D = new Objective();
                            D.setBuilding(blp);
                            D.setCount(1);
                            D.setCity(start);
                            player.getObjectives().add(D);

                            blp2 = buildingBlueprintRepository.findById(20);

                            if(player.getNation().getId() == 2)
                                blp2 = buildingBlueprintRepository.findById(21);

                            Objective E = new Objective();
                            E.setBuilding(blp2);
                            E.setCount(1);
                            E.setCity(start);
                            player.getObjectives().add(E);

                            data.put("brief", true);
                            break;

                        case 6:
                            blp = buildingBlueprintRepository.findById(18);

                            if(start.getBiome().equals(desert) || start.getBiome().equals(mountains))
                                blp = buildingBlueprintRepository.findById(17);

                            Objective F = new Objective();
                            F.setBuilding(blp);
                            F.setCount(2);
                            F.setCity(start);
                            player.getObjectives().add(F);

                            blp2 = buildingBlueprintRepository.findById(1);

                            Objective G = new Objective();
                            G.setBuilding(blp2);
                            G.setCount(2);
                            G.setCity(start);
                            player.getObjectives().add(G);

                            data.put("brief", true);
                            break;

                        case 7:
                            blp = buildingBlueprintRepository.findById(7);

                            if(player.getNation().getId() == 2)
                                blp = buildingBlueprintRepository.findById(15);

                            Objective I = new Objective();
                            I.setBuilding(blp);
                            I.setCount(1);
                            I.setCity(start);
                            player.getObjectives().add(I);

                            data.put("brief", true);
                            break;

                        case 8:
                            Road road = roadRepository.findByCity(start).get(0);
                            City other = road.getPoint1().equals(start) ? road.getPoint2() : road.getPoint1();

                            Objective J = new Objective();
                            J.setConquerCity(other);
                            player.getObjectives().add(J);

                            data.put("brief", true);
                            break;

                        case 9:
                            data.put("brief", true);
                            break;
                    }
                }

                player.getObjectives().stream().filter(e -> e.toJSON() != null).forEach(e -> objectiveList.add(e.toJSON()));
            }

            data.put("cities", cityMap);
            data.put("roads", roadMap);
            data.put("formations", formationMap);
            data.put("caravans", caravanMap);
            data.put("timestamp", System.currentTimeMillis());
            data.put("top", topMap);
            data.put("events", eventList);
            data.put("objectives", objectiveList);
        }

        return data;
    }

    @Autowired
    private LogicService logicService;

    @RequestMapping(value = "/fastForward", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    public void fastForward(@RequestParam int hours) {
        for(int i = 0; i < hours * 3600; i++) {
            logicService.Worker();
        }
    }

    @RequestMapping(value = "/event", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void removeEvent(Principal principal, @RequestParam ObjectId id) {
        Player player = playerRepository.findByName(principal.getName());
        Event event = eventRepository.findById(id);

        if(event != null && event.getPlayer().equals(player)) {
            eventRepository.remove(event);
        }
    }

    @RequestMapping(value = "/scroll", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updateScrollPosition(Principal principal, @RequestParam int x, @RequestParam int y) {
        Player player = playerRepository.findByName(principal.getName());

        player.setLastScrollX(x);
        player.setLastScrollY(y);
    }

    @RequestMapping(value = "/scroll", method = RequestMethod.GET)
    public @ResponseBody ObjectNode getScrollPosition(Principal principal) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        if(principal != null) {
            Player player = playerRepository.findByName(principal.getName());

            node.put("x", player.getLastScrollX());
            node.put("y", player.getLastScrollY());
        }

        return node;
    }
}
