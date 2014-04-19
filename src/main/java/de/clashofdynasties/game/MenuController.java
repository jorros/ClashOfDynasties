package de.clashofdynasties.game;

import de.clashofdynasties.models.*;
import de.clashofdynasties.repository.*;
import de.clashofdynasties.service.CounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/game/menus")
public class MenuController
{
    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    CityRepository cityRepository;

    @Autowired
    FormationRepository formationRepository;

    @Autowired
    UnitBlueprintRepository unitBlueprintRepository;

    @Autowired
    BuildingBlueprintRepository buildingBlueprintRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    CounterService counterService;

    @RequestMapping(value = "/top", method = RequestMethod.GET)
    @ResponseBody
    public HashMap<String, Object> getTop(Principal principal)
    {
        HashMap<String, Object> map = new HashMap<String, Object>();

        Player player = playerRepository.findByName(principal.getName());
        List<City> cities = cityRepository.findByPlayer(player);

        int people = 0;
        int balance = 0;

        for(City city : cities)
        {
            people += city.getPopulation();
            balance += city.getIncome() - city.getOutcome();
        }

        map.put("player", player);
        map.put("cities", cities);
        map.put("people", people);
        map.put("balance", balance);
        map.put("cityNum", cities.size());

        return map;
    }

    @RequestMapping(value="/formation", method = RequestMethod.GET)
    public String showFormationSetup(ModelMap map, Principal principal, @RequestParam(value = "formation", required = false) Integer id, @RequestParam(value = "city", required = false) Integer cityID)
    {
        Player player = playerRepository.findByName(principal.getName());

        if(id != null)
        {
            Formation formation = formationRepository.findOne(id);
            map.addAttribute("formation", formation);
            map.addAttribute("city", formation.getLastCity());

            if(!formation.getPlayer().equals(player) || !formation.isDeployed())
                return "menu/infoFormation";
        }
        else if(cityID != null)
        {
            City city = cityRepository.findOne(cityID);

            if(!city.getPlayer().equals(player))
                return null;

            Formation formation = new Formation();
            formation.setName("Neue Formation");

            map.addAttribute("formation", formation);
            map.addAttribute("city", city);
        }

        return "menu/setupFormation";
    }

    @RequestMapping(value="/build", method = RequestMethod.GET)
    public String showBuild(ModelMap map, Principal principal, @RequestParam("city") int id)
    {
        map.addAttribute("city", cityRepository.findOne(id));
        map.addAttribute("buildingBlueprints", buildingBlueprintRepository.findAll());
        map.addAttribute("unitBlueprints", unitBlueprintRepository.findAll());
        map.addAttribute("player", playerRepository.findByName(principal.getName()));

        return "menu/build";
    }

    @RequestMapping(value="/report", method = RequestMethod.GET)
    public String showReport(ModelMap map, Principal principal, @RequestParam("city") int id) {
        City city = cityRepository.findOne(id);

        map.addAttribute("city", city);
        map.addAttribute("formations", formationRepository.findByCity(id));
        map.addAttribute("player", playerRepository.findByName(principal.getName()));

        return "menu/report";
    }

    @RequestMapping(value="/store", method = RequestMethod.GET)
    public String showStore(ModelMap map, Principal principal, @RequestParam("city") int id) {
        City city = cityRepository.findOne(id);

        map.addAttribute("city", city);
        map.addAttribute("items", itemRepository.findAll(new Sort(Sort.Direction.ASC, "_id")));
        map.addAttribute("player", playerRepository.findByName(principal.getName()));

        return "menu/store";
    }

	@RequestMapping(value = "/ranking", method = RequestMethod.GET)
	public String showRanking(ModelMap map)
	{
		return "menu/ranking";
	}

	@RequestMapping(value = "/settings", method = RequestMethod.GET)
	public String showSettings(ModelMap map)
	{
		return "menu/settings";
	}

	@RequestMapping(value = "/demography", method = RequestMethod.GET)
	public String showDemography(ModelMap map)
	{
		return "menu/demography";
	}

	@RequestMapping(value = "/diplomacy", method = RequestMethod.GET)
	public String showDiplomacy(ModelMap map)
	{
		return "menu/diplomacy";
	}

    @RequestMapping(value="/editresources", method = RequestMethod.GET)
    public String showEditResources(ModelMap map)
    {
        map.addAttribute("items", itemRepository.findAll());

        return "menu/editbuildings";
    }

    @RequestMapping(value="/editbuildings", method = RequestMethod.GET)
    public String showEditBuildings(ModelMap map)
    {
        map.addAttribute("buildingBlueprints", buildingBlueprintRepository.findAll(new Sort(Sort.Direction.ASC, "_id")));

        return "menu/editbuildings";
    }

    @RequestMapping(value="/editunits", method = RequestMethod.GET)
    public String showEditUnits(ModelMap map)
    {
        map.addAttribute("unitBlueprints", unitBlueprintRepository.findAll(new Sort(Sort.Direction.ASC, "_id")));

        return "menu/editunits";
    }
}
