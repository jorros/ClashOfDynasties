package de.clashofdynasties.game;

import de.clashofdynasties.models.Road;
import de.clashofdynasties.repository.RoadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class RoadController
{
	@Autowired
	private RoadRepository roadRepository;

	@RequestMapping(value = "/game/roads/all", method = RequestMethod.GET)
	public @ResponseBody
    Map<Integer, Road> loadAllRoads()
	{
		List<Road> roads = roadRepository.findAll();
        HashMap<Integer, Road> data = new HashMap<Integer, Road>();

        for(Road road : roads)
        {
            data.put(road.getId(), road);
        }

		return data;
	}
}
