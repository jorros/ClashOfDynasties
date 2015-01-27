package de.dynastiesofscorpia.game;

import de.dynastiesofscorpia.models.City;
import de.dynastiesofscorpia.models.Road;
import de.dynastiesofscorpia.repository.CityRepository;
import de.dynastiesofscorpia.repository.RoadRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/game/roads")
@Secured("ROLE_USER")
public class RoadController {
    @Autowired
    private RoadRepository roadRepository;

    @Autowired
    private CityRepository cityRepository;

    @RequestMapping(value = "/getByPoints", method = RequestMethod.GET)
    @ResponseBody
    public String getIdByPoints(@RequestParam ObjectId point1, @RequestParam ObjectId point2) {
        City c1 = cityRepository.findById(point1);
        City c2 = cityRepository.findById(point2);

        Road road = roadRepository.findByCities(c1, c2);

        if (road == null)
            return "";
        else
            return road.getId().toHexString();
    }

    @RequestMapping(value = "/{road}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    public void remove(@PathVariable("road") ObjectId id) {
        roadRepository.remove(roadRepository.findById(id));
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    public void create(@RequestParam ObjectId point1, @RequestParam ObjectId point2, @RequestParam float weight) {
        Road road = new Road();
        roadRepository.add(road);
        save(road.getId(), point1, point2, weight);
    }

    @RequestMapping(value = "/{road}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    public void save(@PathVariable("road") ObjectId id, @RequestParam(required = false) ObjectId point1, @RequestParam(required = false) ObjectId point2, @RequestParam(required = false) Float weight) {
        Road road = roadRepository.findById(id);

        if (point1 != null)
            road.setPoint1(cityRepository.findById(point1));

        if (point2 != null)
            road.setPoint2(cityRepository.findById(point2));

        if (weight != null)
            road.setWeight(weight);

        road.updateTimestamp();
    }
}
