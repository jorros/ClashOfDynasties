package de.clashofdynasties.game;

import de.clashofdynasties.models.*;
import de.clashofdynasties.repository.*;
import de.clashofdynasties.service.CounterService;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

    @Autowired
    CaravanRepository caravanRepository;

    @RequestMapping(value = "/top", method = RequestMethod.GET)
    @ResponseBody
    public ObjectNode getTop(Principal principal)
    {
        ObjectNode node = JsonNodeFactory.instance.objectNode();

        Player player = playerRepository.findByName(principal.getName());
        List<City> cities = cityRepository.findByPlayer(player);

        int people = 0;
        int balance = 0;

        for(City city : cities)
        {
            people += city.getPopulation();
            balance += city.getIncome() - city.getOutcome();
        }

        node.put("coins", player.getCoins());
        node.put("people", people);
        node.put("balance", balance);
        node.put("cityNum", cities.size());

        return node;
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

    @RequestMapping(value="/caravan", method = RequestMethod.GET)
    public String showCaravan(ModelMap map, Principal principal, @RequestParam(required = false) Integer point1, @RequestParam(required = false) Integer point2, @RequestParam(value = "caravan", required = false) Integer id) {
        Player player = playerRepository.findByName(principal.getName());

        if(id != null) {
            Caravan caravan = caravanRepository.findOne(id);

            if(caravan.getPlayer().equals(player)) {
                map.addAttribute("caravan", caravan);
                map.addAttribute("point1", caravan.getPoint1());
                map.addAttribute("point2", caravan.getPoint2());
            }
        }
        else {
            Caravan caravan = new Caravan();
            caravan.setName("Neue Karawane");

            map.addAttribute("caravan", caravan);
            map.addAttribute("point1", cityRepository.findOne(point1));
            map.addAttribute("point2", cityRepository.findOne(point2));
        }

        map.addAttribute("items", itemRepository.findAll(new Sort(Sort.Direction.ASC, "_id")));

        return "menu/caravan";
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

    @RequestMapping(value="/timestamp", method = RequestMethod.GET)
    public @ResponseBody long getTimestamp()
    {
        return System.currentTimeMillis();
    }
}
