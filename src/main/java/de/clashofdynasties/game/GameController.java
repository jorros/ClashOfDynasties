package de.clashofdynasties.game;

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

                    switch (player.getLevel()) {
                        case 1:
                        /*
                        Objective A = new Objective();
                        A.setBuilding(buildingBlueprintRepository.findById(1));
                        A.setCount(1);
                        A.setCity(cityRepository.findByPlayer(player).get(0));
                        player.getObjectives().add(A);
                        */

                            data.put("brief", true);
                            break;

                        case 2:
                            data.put("brief", true);
                            break;

                        case 3:
                            data.put("brief", true);
                            break;

                        case 4:
                            data.put("brief", true);
                            break;

                        case 5:
                            data.put("brief", true);
                            break;

                        case 6:
                            data.put("brief", true);
                            break;

                        case 7:
                            data.put("brief", true);
                            break;

                        case 8:
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
        Player player = playerRepository.findByName(principal.getName());

        node.put("x", player.getLastScrollX());
        node.put("y", player.getLastScrollY());

        return node;
    }
}
