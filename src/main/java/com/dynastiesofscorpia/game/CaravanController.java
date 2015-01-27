package com.dynastiesofscorpia.game;

import com.dynastiesofscorpia.models.*;
import com.dynastiesofscorpia.repository.*;
import com.dynastiesofscorpia.service.RoutingService;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;

@Controller
@RequestMapping("/game/caravans")
@Secured("ROLE_USER")
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
    private RoadRepository roadRepository;

    @Autowired
    private RelationRepository relationRepository;

    @Autowired
    private EventRepository eventRepository;

    @RequestMapping(value = "/route", method = RequestMethod.GET)
    public
    @ResponseBody
    ObjectNode calculateRoute(Principal principal, @RequestParam ObjectId point1, @RequestParam ObjectId point2) {
        Player player = playerRepository.findByName(principal.getName());
        City city1 = cityRepository.findById(point1);
        City city2 = cityRepository.findById(point2);

        if (city1 != null && city2 != null) {
            if(city1.getBuildings().stream().filter(b -> b.getBlueprint().getId() == 12).count() > 0 && city2.getBuildings().stream().filter(b -> b.getBlueprint().getId() == 12).count() > 0) {
                RoutingService routing = new RoutingService(roadRepository, relationRepository);

                if (routing.calculateRoute(city1, city2, player)) {
                    return routing.getRoute().toJSON();
                }
            }
        }

        return JsonNodeFactory.instance.objectNode();
    }

    @RequestMapping(value = "/{caravan}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void remove(Principal principal, @PathVariable("caravan") ObjectId caravanId) {
        Player player = playerRepository.findByName(principal.getName());
        Caravan caravan = caravanRepository.findById(caravanId);

        if(caravan.isPaused()) {
            Relation relation = relationRepository.findByPlayers(caravan.getPoint1().getPlayer(), caravan.getPoint2().getPlayer());

            if(relation != null) {
                relation.removePendingCaravan(caravan);
            }

            eventRepository.add(new Event("Trade", "Handelsvorschlag wurde abgelehnt", caravan.getPoint2().getPlayer().getName() + " hat euer Handelsvorschlag abgelehnt.Die Karawane " + caravan.getName() + " wird nicht entsandt.", "diplomacy?pid=" + player.getId(), caravan.getPoint1().getPlayer()));

            caravanRepository.remove(caravan);
        } else if (caravan.getPoint1().getPlayer().equals(caravan.getPoint2().getPlayer()) && caravan.getPlayer().equals(player)) {
            caravan.setTerminate(!caravan.isTerminate());
        } else if(caravan.getPoint1().getPlayer().equals(player) || caravan.getPoint2().getPlayer().equals(player)) {
            caravan.setTerminate(true);

            Player otherPlayer = caravan.getPoint1().getPlayer();

            if(otherPlayer.equals(player))
                otherPlayer = caravan.getPoint2().getPlayer();

            eventRepository.add(new Event("Trade", "Handelsweg wurde von " + player.getName() + " aufgelöst", "Der Handelsweg zwischen " + caravan.getPoint1().getName() + " und " + caravan.getPoint2().getName() + " wurde von " + player.getName() + " aufgelöst. Die Karawane " + caravan.getName() + " wird bei Ankunft in " + caravan.getPoint1().getName() + " aufgelöst.", "diplomacy?pid=" + player.getId(), otherPlayer));
        }
    }

    @RequestMapping(value = "/{caravan}/accept", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void accept(Principal principal, @PathVariable("caravan") ObjectId caravanId) {
        Player player = playerRepository.findByName(principal.getName());
        Caravan caravan = caravanRepository.findById(caravanId);

        if(caravan.getPoint2().getPlayer().equals(player)) {
            Relation relation = relationRepository.findByPlayers(caravan.getPoint1().getPlayer(), caravan.getPoint2().getPlayer());

            if(relation != null) {
                relation.removePendingCaravan(caravan);
                relation.addCaravan(caravan);

                caravan.setPaused(false);
                loadPoint1In(caravan);

                caravan.updateTimestamp();

                eventRepository.add(new Event("Trade", "Handelsweg wurde etabliert", "Der Handelsweg zwischen " + caravan.getPoint1().getName() + " und " + caravan.getPoint2().getName() + " wurde etabliert. Die Karawane " + caravan.getName() + " wurde entsandt.", caravan.getPoint1(), caravan.getPoint1().getPlayer()));
            } else {
                remove(principal, caravanId);
            }
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void create(Principal principal, @RequestParam String name, @RequestParam ObjectId point1, @RequestParam Integer point1Item, @RequestParam Integer point1Load, @RequestParam ObjectId point2, @RequestParam Integer point2Item, @RequestParam Integer point2Load) {
        Player player = playerRepository.findByName(principal.getName());
        City city1 = cityRepository.findById(point1);
        City city2 = cityRepository.findById(point2);

        if (city1 != null && city2 != null) {
            RoutingService routing = new RoutingService(roadRepository, relationRepository);

            if (routing.calculateRoute(city1, city2, player) && city1.getPlayer().equals(player) && city2.getPlayer().equals(player)) {
                createCaravan(player, name, city1, itemRepository.findById(point1Item), point1Load, city2, itemRepository.findById(point2Item), point2Load, routing.getRoute(), false);
            } else if(routing.calculateRoute(city1, city2, player) && city1.getPlayer().equals(player) || !city2.getPlayer().equals(player)) {
                Relation relation = relationRepository.findByPlayers(city1.getPlayer(), city2.getPlayer());

                if(relation != null && relation.getRelation() >= 2) {
                    Caravan caravan = createCaravan(player, name, city1, itemRepository.findById(point1Item), point1Load, city2, itemRepository.findById(point2Item), point2Load, routing.getRoute(), true);
                    relation.addPendingCaravan(caravan);

                    eventRepository.add(new Event("Trade", "Handelsvorschlag von " + player.getName(), "Der Spieler " + player.getName() + " unterbreitet euch einen Handelsvorschlag zwischen eurer Stadt " + city2.getName() + " und " + city1.getName() + ".", "diplomacy?pid=" + player.getId(), city2.getPlayer()));
                }
            }
        }
    }

    private Caravan createCaravan(Player player, String name, City point1, Item point1Item, Integer point1Load, City point2, Item point2Item, Integer point2Load, Route route, boolean paused) {
        Caravan caravan = new Caravan();
        caravan.setPoint1(point1);
        caravan.setPoint2(point2);
        caravan.setRoute(route);
        caravan.setX(point1.getX());
        caravan.setY(point1.getY());

        caravanRepository.add(caravan);

        caravan.getRoute().setCurrentRoad(roadRepository.findByCities(point1, caravan.getRoute().getNext()));

        caravan.setPoint1Item(point1Item);
        caravan.setPoint1Load(point1Load);

        caravan.setPoint2Item(point2Item);
        caravan.setPoint2Load(point2Load);

        caravan.setName(name);
        caravan.setPlayer(player);

        caravan.setPaused(paused);

        if(!paused)
            loadPoint1In(caravan);

        caravan.move(70);

        caravan.updateTimestamp();

        caravan.setDirection(2);

        return caravan;
    }

    private void loadPoint1In(Caravan caravan) {
        double amount = (caravan.getPoint1().getStoredItem(caravan.getPoint1Item().getId()) - caravan.getPoint1Load() > 0) ? caravan.getPoint1Load() : Math.floor(caravan.getPoint1().getStoredItem(caravan.getPoint1Item().getId()));

        if (caravan.getPoint1().getItems() == null)
            caravan.getPoint1().setItems(new HashMap<>());

        caravan.getPoint1().getItems().put(caravan.getPoint1Item().getId(), caravan.getPoint1().getStoredItem(caravan.getPoint1Item().getId()) - amount);

        caravan.setPoint1Store(new Double(amount).intValue());
        caravan.setPoint1StoreItem(caravan.getPoint1Item());
    }

    @RequestMapping(value = "/{caravan}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void save(Principal principal, @PathVariable("caravan") ObjectId id, @RequestParam(required = false) String name, @RequestParam(required = false) Integer point1Item, @RequestParam(required = false) Integer point1Load, @RequestParam(required = false) Integer point2Item, @RequestParam(required = false) Integer point2Load) {
        Player player = playerRepository.findByName(principal.getName());
        Caravan caravan = caravanRepository.findById(id);

        if (caravan.getPlayer().equals(player)) {
            if (name != null)
                caravan.setName(name);

            if (point1Item != null)
                caravan.setPoint1Item(itemRepository.findById(point1Item));

            if (point1Load != null)
                caravan.setPoint1Load(point1Load);

            if (point2Item != null)
                caravan.setPoint2Item(itemRepository.findById(point2Item));

            if (point2Load != null)
                caravan.setPoint2Load(point2Load);
        }
    }
}
