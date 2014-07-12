package de.clashofdynasties.game;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.clashofdynasties.models.Road;
import de.clashofdynasties.repository.CityRepository;
import de.clashofdynasties.repository.RoadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/game/roads")
public class RoadController {
    @Autowired
    private RoadRepository roadRepository;

    @Autowired
    private CityRepository cityRepository;

    @RequestMapping(method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, ObjectNode> getRoads() {
        List<Road> roads = roadRepository.findAll();
        HashMap<String, ObjectNode> data = new HashMap<String, ObjectNode>();

        roads.forEach(road -> data.put(road.getId(), road.toJSON()));

        return data;
    }

    @RequestMapping(value = "/getByPoints", method = RequestMethod.GET)
    @ResponseBody
    public String getIdByPoints(@RequestParam String point1, @RequestParam String point2) {
        Road road = roadRepository.findByCities(point1, point2);

        if (road == null)
            return "";
        else
            return road.getId();
    }

    @RequestMapping(value = "/{road}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    public void remove(@PathVariable("road") String id) {
        roadRepository.delete(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    public void create(@RequestParam String point1, @RequestParam String point2, @RequestParam float weight) {
        Road road = new Road();
        roadRepository.save(road);
        save(road.getId(), point1, point2, weight);
    }

    @RequestMapping(value = "/{road}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    public void save(@PathVariable("road") String id, @RequestParam(required = false) String point1, @RequestParam(required = false) String point2, @RequestParam(required = false) Float weight) {
        Road road = roadRepository.findOne(id);

        if (point1 != null)
            road.setPoint1(cityRepository.findOne(point1));

        if (point2 != null)
            road.setPoint2(cityRepository.findOne(point2));

        if (weight != null)
            road.setWeight(weight);

        roadRepository.save(road);
    }
}
