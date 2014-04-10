package de.clashofdynasties.game;

import de.clashofdynasties.models.Road;
import de.clashofdynasties.repository.CityRepository;
import de.clashofdynasties.repository.RoadRepository;
import de.clashofdynasties.service.CounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/game/roads")
public class RoadController
{
	@Autowired
	private RoadRepository roadRepository;

    @Autowired
    private CounterService counterService;

    @Autowired
    private CityRepository cityRepository;

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody
    Map<Integer, Road> getRoads()
	{
		List<Road> roads = roadRepository.findAll();
        HashMap<Integer, Road> data = new HashMap<Integer, Road>();

        for(Road road : roads)
        {
            data.put(road.getId(), road);
        }

		return data;
	}

    @RequestMapping(value = "/{road}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void remove(@PathVariable("road") int id)
    {
        roadRepository.delete(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void create(@RequestParam int point1, @RequestParam int point2, @RequestParam float weight)
    {
        save(0, point1, point2, weight);
    }

    @RequestMapping(value = "/{road}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void save(@PathVariable("road") int id, @RequestParam(required = false) Integer point1, @RequestParam(required = false) Integer point2, @RequestParam(required = false) Float weight)
    {
        Road road;
        if(id > 0)
        {
            road = roadRepository.findOne(id);
        }
        else
        {
            road = new Road();
            road.setId(counterService.getNextSequence("road"));
        }

        if(point1 != null)
            road.setPoint1(cityRepository.findOne(point1));

        if(point2 != null)
            road.setPoint2(cityRepository.findOne(point2));

        if(weight != null)
            road.setWeight(weight);

        roadRepository.save(road);
    }
}
