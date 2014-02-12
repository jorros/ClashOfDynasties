package de.clashofdynasties.game;

import de.clashofdynasties.models.City;
import de.clashofdynasties.models.Player;
import de.clashofdynasties.repository.CityRepository;
import de.clashofdynasties.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.List;

@Controller
public class GlobalController
{
    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    CityRepository cityRepository;

    @RequestMapping(value = "/game", method = RequestMethod.GET)
    public String game(ModelMap map, Principal principal)
    {
        Player player = playerRepository.findByName(principal.getName());
        List<City> cities = cityRepository.findByPlayer(player);

        int people = 0;
        for(City city : cities)
        {
            people += city.getPopulation();
        }

        map.addAttribute("player", player);
        map.addAttribute("cities", cities);
        map.addAttribute("people", people);

        return "game";
    }

	@RequestMapping(value = "/game/menu/ranking", method = RequestMethod.GET)
	public String getRanking(ModelMap map)
	{
		return "ranking";
	}

	@RequestMapping(value = "/game/menu/settings", method = RequestMethod.GET)
	public String getSettings(ModelMap map)
	{
		return "settings";
	}

	@RequestMapping(value = "/game/menu/demography", method = RequestMethod.GET)
	public String getDemography(ModelMap map)
	{
		return "demography";
	}

	@RequestMapping(value = "/game/menu/diplomacy", method = RequestMethod.GET)
	public String getDiplomacy(ModelMap map)
	{
		return "diplomacy";
	}
}
