package de.clashofdynasties.game;

import de.clashofdynasties.models.Caravan;
import de.clashofdynasties.models.City;
import de.clashofdynasties.models.Player;
import de.clashofdynasties.models.Route;
import de.clashofdynasties.repository.CaravanRepository;
import de.clashofdynasties.repository.CityRepository;
import de.clashofdynasties.repository.ItemRepository;
import de.clashofdynasties.repository.PlayerRepository;
import de.clashofdynasties.service.CounterService;
import de.clashofdynasties.service.RoutingService;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
public class CaravanController
{
    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    CaravanRepository caravanRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    CounterService counterService;

    @Autowired
    CityRepository cityRepository;

    @Autowired
    RoutingService routingService;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    Map<Integer, ObjectNode> getCaravans(Principal principal, @RequestParam boolean editor, @RequestParam long timestamp) {
        Player player = playerRepository.findByName(principal.getName());

        List<Caravan> caravans = caravanRepository.findAll();
        HashMap<Integer, ObjectNode> data = new HashMap<Integer, ObjectNode>();

        for(Caravan caravan : caravans) {
            if (player.equals(caravan.getPlayer()))
                caravan.setDiplomacy(1);

            // Wenn Spieler neutral
            if (caravan.getPlayer().getId() == 1) {
                caravan.setDiplomacy(4);
            }

            data.put(caravan.getId(), caravan.toJSON(editor, timestamp));
        }

        return data;
    }

    @RequestMapping(value="/route", method = RequestMethod.GET)
    public @ResponseBody ObjectNode calculateRoute(@RequestParam int point1, @RequestParam int point2) {
        City city1 = cityRepository.findOne(point1);
        City city2 = cityRepository.findOne(point2);

        if(city1 != null && city2 != null) {
            Route route = routingService.calculateRoute(city1, city2);
            route.setTime(routingService.calculateTime(route));

            return route.toJSON();
        }

        return null;
    }

    @RequestMapping(value="/{caravan}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void remove(Principal principal, @PathVariable("caravan") int caravanId) {
        Player player = playerRepository.findByName(principal.getName());
        Caravan caravan = caravanRepository.findOne(caravanId);

        if(caravan.getPlayer().equals(player)) {
            caravan.setTerminate(true);
            caravanRepository.save(caravan);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void create(Principal principal, @RequestParam String name, @RequestParam Integer point1, @RequestParam Integer point1Item, @RequestParam Integer point1Load, @RequestParam Integer point2, @RequestParam Integer point2Item, @RequestParam Integer point2Load) {
        Player player = playerRepository.findByName(principal.getName());
        City city1 = cityRepository.findOne(point1);
        City city2 = cityRepository.findOne(point2);

        if(city1 != null && city2 != null) {
            Route route = routingService.calculateRoute(city1, city2);

            if(route != null)
            {
                Caravan caravan = new Caravan();
                caravan.setId(counterService.getNextSequence("Caravan"));
                caravan.setPoint1(city1);
                caravan.setPoint2(city2);
                caravan.setRoute(route);
                caravan.setX(city1.getX());
                caravan.setY(city1.getY());

                caravan.setPoint1Item(itemRepository.findOne(point1Item));
                caravan.setPoint1Load(point1Load);

                caravan.setPoint2Item(itemRepository.findOne(point2Item));
                caravan.setPoint2Load(point2Load);

                caravan.setName(name);
                caravan.setPlayer(player);

                caravan.move(70);

                caravanRepository.save(caravan);
            }
        }
    }

    @RequestMapping(value="/{caravan}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void save(Principal principal, @PathVariable("caravan") int id, @RequestParam(required = false) String name, @RequestParam(required = false) Integer point1Item, @RequestParam(required = false) Integer point1Load, @RequestParam(required = false) Integer point2Item, @RequestParam(required = false) Integer point2Load) {
        Player player = playerRepository.findByName(principal.getName());
        Caravan caravan = caravanRepository.findOne(id);

        if(caravan.getPlayer().equals(player)) {
            if(name != null)
                caravan.setName(name);

            if(point1Item != null)
                caravan.setPoint1Item(itemRepository.findOne(point1Item));

            if(point1Load != null)
                caravan.setPoint1Load(point1Load);

            if(point2Item != null)
                caravan.setPoint2Item(itemRepository.findOne(point2Item));

            if(point2Load != null)
                caravan.setPoint2Load(point2Load);

            caravanRepository.save(caravan);
        }
    }
}
