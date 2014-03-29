package de.clashofdynasties.game;

import de.clashofdynasties.models.*;
import de.clashofdynasties.repository.*;
import de.clashofdynasties.service.CounterService;
import javafx.scene.control.TableColumn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.security.krb5.PrincipalName;

import java.security.Principal;
import java.util.*;

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

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    FormationRepository formationRepository;

    @Autowired
    CounterService counterService;

    @Autowired
    UnitRepository unitRepository;

    @RequestMapping(value = "/game/cities/all", method = RequestMethod.GET)
	public @ResponseBody
    Map<Integer, City> loadAllCities(Principal principal)
	{
        Player player = playerRepository.findByName(principal.getName());

		List<City> cities = cityRepository.findAll();
        List<Formation> formations = formationRepository.findAll();
        HashMap<Integer, City> data = new HashMap<Integer, City>();

        for(City city : cities)
        {
            for(Formation formation : formations)
            {
                if(formation.getRoute() == null && formation.getLastCity().equals(city))
                {
                    if(city.getFormations() == null)
                        city.setFormations(new ArrayList<Formation>());

                    city.getFormations().add(formation);
                }
            }

            // Diplomatie setzen
            if(player.equals(city.getPlayer()))
                city.setDiplomacy(1);

            // Wenn Spieler neutral
            if(city.getPlayer().getId() == 1)
            {
                city.setDiplomacy(4);
                city.setSatisfaction(-1);
            }

            data.put(city.getId(), city);
        }

		return data;
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

    @RequestMapping(value="/game/menu/items", method = RequestMethod.GET)
    public String showItemsMenu(ModelMap map, Principal principal, @RequestParam("city") int id)
    {
        City city = cityRepository.findOne(id);

        map.addAttribute("city", city);
        map.addAttribute("items", itemRepository.findAll(new Sort(Sort.Direction.ASC, "_id")));
        map.addAttribute("player", playerRepository.findByName(principal.getName()));

        return "city/items";
    }

	@RequestMapping(value="/game/controls/city", method = RequestMethod.GET)
	public String showInfoMenu(ModelMap map, Principal principal, @RequestParam("city") int id)
	{
		City city = cityRepository.findOne(id);

		int maxSlots = Math.round(city.getCapacity() * city.getType().getCapacity());
        int freeSlots = maxSlots;

        if(city.getBuildings() != null)
		    freeSlots -= city.getBuildings().size();

        String smiley = "Happy";

        if(city.getPopulation() > 0)
        {
            if(city.getSatisfaction() < 80 && city.getSatisfaction() >= 60)
                smiley = "Satisfied";
            else if(city.getSatisfaction() < 60 && city.getSatisfaction() >= 30)
                smiley = "Unhappy";
            else
                smiley = "Angry";
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
