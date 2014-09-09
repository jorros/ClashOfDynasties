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
@RequestMapping("/game/core")
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

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object> getUpdate(Principal principal, @RequestParam boolean editor, @RequestParam long timestamp) {
        Player player = playerRepository.findByName(principal.getName());
        RoutingService routing = new RoutingService(roadRepository, relationRepository);

        List<City> cities = cityRepository.findAll();
        List<Road> roads = roadRepository.findAll();
        List<Formation> formations = formationRepository.findAll();
        List<Caravan> caravans = caravanRepository.findAll();
        List<Event> events = eventRepository.findByPlayer(player);

        HashMap<String, Object> data = new HashMap<>();
        HashMap<String, City> cityMap = new HashMap<>();
        HashMap<String, ObjectNode> roadMap = new HashMap<>();
        HashMap<String, ObjectNode> formationMap = new HashMap<>();
        HashMap<String, ObjectNode> caravanMap = new HashMap<>();
        HashMap<String, ObjectNode> cityJSON = new HashMap<>();
        HashMap<String, Object> topMap = new HashMap<>();
        List<Object> eventList = new ArrayList<>();

        int people = 0;
        int balance = 0;
        int numCities = 0;
        int numFormations = 0;
        int numCaravans = 0;

        for (City city : cities) {
            // Diplomatie
            if(city.getPlayer().equals(player)) {
                people += city.getPopulation();
                balance += city.getIncome() - city.getOutcome();
                numCities++;
            }

            cityMap.put(city.getId().toHexString(), city);
        }

        for(Road road : roads) {
            if(road.isVisible(player) || editor) {
                roadMap.put(road.getId().toHexString(), road.toJSON(timestamp));
            }
        }

        for (Formation formation : formations) {
            if(!editor && formation.isVisible(player)) {
                // Deploy Status ermitteln
                if (formation.getRoute() != null) {
                    formation.setDeployed(false);
                    routing.setRoute(formation.getRoute());
                    formation.getRoute().setTime(routing.calculateTime());
                } else {
                    formation.setDeployed(true);

                    if(cityMap.containsKey(formation.getLastCity().getId().toHexString())) {
                        City city = cityMap.get(formation.getLastCity().getId().toHexString());

                        if (city.getFormations() == null)
                            city.setFormations(new ArrayList<>());

                        city.getFormations().add(formation);
                    }
                }

                if (player.equals(formation.getPlayer())) {
                    numFormations++;
                }

                formationMap.put(formation.getId().toHexString(), formation.toJSON(timestamp, player));
            }
        }

        for (Caravan caravan : caravans) {
            if(!editor && caravan.isVisible(player)) {
                if (player.equals(caravan.getPlayer())) {
                    numCaravans++;
                }

                caravanMap.put(caravan.getId().toHexString(), caravan.toJSON(timestamp));
            }
        }

        for(String key : cityMap.keySet()) {
            cityJSON.put(key, cityMap.get(key).toJSON(editor, timestamp, player));
        }

        topMap.put("coins", player.getCoins());
        topMap.put("people", people);
        topMap.put("balance", balance);
        topMap.put("cityNum", numCities);
        topMap.put("formationNum", numFormations);
        topMap.put("caravanNum", numCaravans);
        topMap.put("ranking", player.getStatistic() == null ? "Unplatziert" : Integer.valueOf(player.getStatistic().getRank()).toString());

        if(events != null && events.size() > 0) {
            events.stream().filter(e -> e.toJSON(timestamp) != null).forEach(e -> eventList.add(e.toJSON(timestamp)));
        }

        data.put("cities", cityJSON);
        data.put("roads", roadMap);
        data.put("formations", formationMap);
        data.put("caravans", caravanMap);
        data.put("timestamp", System.currentTimeMillis());
        data.put("top", topMap);
        data.put("events", eventList);

        return data;
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
}
