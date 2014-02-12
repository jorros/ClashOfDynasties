package de.clashofdynasties.game;

import de.clashofdynasties.models.City;
import de.clashofdynasties.models.Nation;
import de.clashofdynasties.models.UnitBlueprint;
import de.clashofdynasties.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;

@Controller
public class CityController
{
	@Autowired
	CityRepository cityRepository;

	@Autowired
	BuildingBlueprintRepository buildingBlueprintRepository;

    @Autowired
    UnitBlueprintRepository unitBlueprintRepository;

	@Autowired
	PlayerRepository playerRepository;

    @Autowired
    NationRepository nationRepository;

	@RequestMapping(value = "/game/cities/load", method = RequestMethod.GET)
	public @ResponseBody
	List<City> loadAllCities()
	{
		List<City> cities = cityRepository.findAll();

		return cities;
	}

	@RequestMapping(value = "/game/cities/update", method = RequestMethod.GET)
	public @ResponseBody
	List<City> updateCities()
	{
		List<City> cities = cityRepository.findAll();

		return cities;
	}

	@RequestMapping(value="/game/menu/build", method = RequestMethod.GET)
	public String showBuildMenu(ModelMap map, Principal principal, @RequestParam("city") int id)
	{
		map.addAttribute("city", cityRepository.findOne(id));
		map.addAttribute("buildingBlueprints", buildingBlueprintRepository.findAll());
        map.addAttribute("unitBlueprints", unitBlueprintRepository.findAll());
		map.addAttribute("player", playerRepository.findByName(principal.getName()));

		return "city/build";
	}

	@RequestMapping(value="/game/controls/city", method = RequestMethod.GET)
	public String showInfoMenu(ModelMap map, Principal principal, @RequestParam("city") int id)
	{
		City city = cityRepository.findOne(id);

		int maxSlots = Math.round(city.getCapacity() * city.getType().getCapacity());
        int freeSlots = maxSlots;

        if(city.getBuildings() != null)
		    freeSlots -= city.getBuildings().size();

        String smiley = "happy";

        if(city.getPopulation() > 0)
        {
            if(city.getSatisfaction() < 80 && city.getSatisfaction() >= 60)
                smiley = "satisfied";
            else if(city.getSatisfaction() < 60 && city.getSatisfaction() >= 30)
                smiley = "unhappy";
            else
                smiley = "angry";
        }
        else
            smiley = null;

        map.addAttribute("satisfaction", smiley);
        map.addAttribute("player", playerRepository.findByName(principal.getName()));
		map.addAttribute("city", city);
		map.addAttribute("freeSlots", freeSlots);
		map.addAttribute("maxSlots", maxSlots);

		return "city/info";
	}
}
