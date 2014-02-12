package de.clashofdynasties.game;

import de.clashofdynasties.models.Road;
import de.clashofdynasties.repository.RoadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class RoadController
{
	@Autowired
	private RoadRepository roadRepository;

	@RequestMapping(value = "/game/roads/load", method = RequestMethod.GET)
	public @ResponseBody
	List<Road> loadAllRoads()
	{
		List<Road> roads = roadRepository.findAll();

		return roads;
	}
}
