package de.clashofdynasties.game;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.clashofdynasties.models.Caravan;
import de.clashofdynasties.models.City;
import de.clashofdynasties.models.Player;
import de.clashofdynasties.models.Route;
import de.clashofdynasties.repository.*;
import de.clashofdynasties.service.RoutingService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/game/caravans")
public class CaravanController {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private CaravanRepository caravanRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private RoutingService routingService;

    @Autowired
    private RoadRepository roadRepository;

    @RequestMapping(method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, ObjectNode> getCaravans(Principal principal, @RequestParam boolean editor, @RequestParam long timestamp) {
        Player player = playerRepository.findByName(principal.getName());

        List<Caravan> caravans = caravanRepository.findAll();
        HashMap<String, ObjectNode> data = new HashMap<String, ObjectNode>();

        for (Caravan caravan : caravans) {
            if (player.equals(caravan.getPlayer()))
                caravan.setDiplomacy(1);

            // Wenn Spieler neutral
            if (caravan.getPlayer().isComputer()) {
                caravan.setDiplomacy(4);
            }

            if(!editor && caravan.isVisible(player))
                data.put(caravan.getId().toHexString(), caravan.toJSON(timestamp));
        }

        return data;
    }

    @RequestMapping(value = "/route", method = RequestMethod.GET)
    public
    @ResponseBody
    ObjectNode calculateRoute(Principal principal, @RequestParam ObjectId point1, @RequestParam ObjectId point2) {
        Player player = playerRepository.findByName(principal.getName());
        City city1 = cityRepository.findOne(point1);
        City city2 = cityRepository.findOne(point2);

        if (city1 != null && city2 != null) {
            Route route = routingService.calculateRoute(city1, city2, player);

            if(route != null)
                route.setTime(routingService.calculateTime(route));
            else
                return null;

            return route.toJSON();
        }

        return null;
    }

    @RequestMapping(value = "/{caravan}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void remove(Principal principal, @PathVariable("caravan") ObjectId caravanId) {
        Player player = playerRepository.findByName(principal.getName());
        Caravan caravan = caravanRepository.findOne(caravanId);

        if (caravan.getPlayer().equals(player)) {
            caravan.setTerminate(!caravan.isTerminate());
            caravanRepository.save(caravan);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void create(Principal principal, @RequestParam String name, @RequestParam ObjectId point1, @RequestParam Integer point1Item, @RequestParam Integer point1Load, @RequestParam ObjectId point2, @RequestParam Integer point2Item, @RequestParam Integer point2Load) {
        Player player = playerRepository.findByName(principal.getName());
        City city1 = cityRepository.findOne(point1);
        City city2 = cityRepository.findOne(point2);

        if (city1 != null && city2 != null) {
            Route route = routingService.calculateRoute(city1, city2, player);

            if (route != null && city1.getPlayer().equals(player) && city2.getPlayer().equals(player)) {
                Caravan caravan = new Caravan();
                caravan.setPoint1(city1);
                caravan.setPoint2(city2);
                caravan.setRoute(route);
                caravan.setX(city1.getX());
                caravan.setY(city1.getY());

                route.setCurrentRoad(roadRepository.findByCities(city1.getId(), route.getNext().getId()));

                caravan.setPoint1Item(itemRepository.findOne(point1Item));
                caravan.setPoint1Load(point1Load);

                caravan.setPoint2Item(itemRepository.findOne(point2Item));
                caravan.setPoint2Load(point2Load);

                caravan.setName(name);
                caravan.setPlayer(player);

                double amount = (city1.getStoredItem(point1Item) - point1Load > 0) ? point1Load : Math.floor(city1.getStoredItem(point1Item));

                if (city1.getItems() == null)
                    city1.setItems(new HashMap<>());

                city1.getItems().put(point1Item, city1.getStoredItem(point1Item) - amount);
                cityRepository.save(city1);

                caravan.setPoint1Store(new Double(amount).intValue());
                caravan.setPoint1StoreItem(itemRepository.findOne(point1Item));

                caravan.move(70);

                caravan.updateTimestamp();

                caravan.setDirection(2);

                caravanRepository.save(caravan);
            }
        }
    }

    @RequestMapping(value = "/{caravan}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void save(Principal principal, @PathVariable("caravan") ObjectId id, @RequestParam(required = false) String name, @RequestParam(required = false) Integer point1Item, @RequestParam(required = false) Integer point1Load, @RequestParam(required = false) Integer point2Item, @RequestParam(required = false) Integer point2Load) {
        Player player = playerRepository.findByName(principal.getName());
        Caravan caravan = caravanRepository.findOne(id);

        if (caravan.getPlayer().equals(player)) {
            if (name != null)
                caravan.setName(name);

            if (point1Item != null)
                caravan.setPoint1Item(itemRepository.findOne(point1Item));

            if (point1Load != null)
                caravan.setPoint1Load(point1Load);

            if (point2Item != null)
                caravan.setPoint2Item(itemRepository.findOne(point2Item));

            if (point2Load != null)
                caravan.setPoint2Load(point2Load);

            caravanRepository.save(caravan);
        }
    }
}
