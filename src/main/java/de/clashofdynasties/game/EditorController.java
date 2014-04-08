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

import java.util.ArrayList;
import java.util.List;

@Controller
public class EditorController
{
	@Autowired
	PlayerRepository playerRepository;

	@Autowired
	NationRepository nationRepository;

	@Autowired
	BiomeRepository biomeRepository;

	@Autowired
	BuildingBlueprintRepository buildingBlueprintRepository;

	@Autowired
	CityRepository cityRepository;

	@Autowired
	CityTypeRepository cityTypeRepository;

	@Autowired
	FormationRepository formationRepository;

	@Autowired
	ItemRepository itemRepository;

	@Autowired
	ItemTypeRepository itemTypeRepository;

	@Autowired
	ResourceRepository resourceRepository;

	@Autowired
	RoadRepository roadRepository;

	@Autowired
	UnitBlueprintRepository unitBlueprintRepository;

	@Autowired
	CounterService counterService;

	@RequestMapping(value = "/editor", method = RequestMethod.GET)
	public String loadEditor()
	{
		return "editor";
	}

	@RequestMapping(value = "/editor/createCity", method = RequestMethod.GET)
	public @ResponseBody
	City createCity(@RequestParam("x") int x, @RequestParam("y") int y)
	{
		System.out.println("Neue Stadt");

		City city = new City();
		city.setId(counterService.getNextSequence("city"));
		city.setName("Neu - " + city.getId());
		city.setCapacity(0);
		city.setHealth(100);
		city.setBiome(biomeRepository.findOne(1));
		city.setPlayer(playerRepository.findOne(1));
		city.setX(x);
		city.setY(y);
		city.setType(cityTypeRepository.findOne(1));
        city.setResource(resourceRepository.findOne(1));

		cityRepository.save(city);

		return city;
	}

	@RequestMapping(value="/editor/saveCity", method = RequestMethod.GET)
	public String saveCity(@RequestParam("id") int id, @RequestParam("name") String name, @RequestParam("type") int type, @RequestParam("capacity") int capacity, @RequestParam("resource") int resource)
	{
		City city = cityRepository.findOne(id);
		city.setName(name);
		city.setCapacity(capacity);
		city.setResource(resourceRepository.findOne(resource));
        if(city.getType().getId() != type)
        {
            city.setType(cityTypeRepository.findOne(type));
            List<ItemType> types = city.getRequiredItemTypes();

            if(types == null)
                types = new ArrayList<ItemType>();
            else
                types.clear();

            int i;
            switch(type)
            {
                case 3:
                    i = (int) (Math.random()*2+1);
                    if(i == 1)
                        types.add(itemTypeRepository.findOne(3));
                    else
                        types.add(itemTypeRepository.findOne(5));

                case 2:
                    i = (int) (Math.random()*3+1);
                    if(i == 1)
                        types.add(itemTypeRepository.findOne(4));
                    else if(i == 2)
                        types.add(itemTypeRepository.findOne(6));
                    else
                        types.add(itemTypeRepository.findOne(7));

                case 1:
                    types.add(itemTypeRepository.findOne(1));
                    types.add(itemTypeRepository.findOne(2));
                    break;

                case 4:
                    break;
            }
            city.setRequiredItemTypes(types);
        }
		cityRepository.save(city);
		return null;
	}

	@RequestMapping(value="/editor/addWay", method = RequestMethod.GET)
	@ResponseBody
	public String addWay(@RequestParam("point1") int point1, @RequestParam("point2") int point2)
	{
		Road found = roadRepository.findByCities(point1, point2);
		if(found != null)
		{
			roadRepository.delete(found);

			return "removed";
		}
		else
		{
			Road road = new Road();
			road.setId(counterService.getNextSequence("road"));
			road.setPoint1(cityRepository.findOne(point1));
			road.setPoint2(cityRepository.findOne(point2));
			roadRepository.save(road);

			return "ok";
		}
	}

	@RequestMapping(value="/editor/setWay", method = RequestMethod.GET)
	@ResponseBody
	public String setWay(@RequestParam("point1") int point1, @RequestParam("point2") int point2, @RequestParam("weight") float weight)
	{
		Road found = roadRepository.findByCities(point1, point2);
		if(found != null)
		{
			found.setWeight(weight);
			roadRepository.save(found);
		}
		return "ok";
	}

	@RequestMapping(value="/editor/deleteCity", method = RequestMethod.GET)
	public String deleteCity(@RequestParam("id") int id)
	{
		roadRepository.delete(roadRepository.findByCity(id));
		cityRepository.delete(id);
		return null;
	}

	@RequestMapping(value="/editor/city", method = RequestMethod.GET)
	public String showInfoMenu(ModelMap map, @RequestParam("city") int id)
	{
		City city = cityRepository.findOne(id);

		map.addAttribute("city", city);

		map.addAttribute("types", cityTypeRepository.findAll());
		map.addAttribute("resources", resourceRepository.findAll());

		return "editor/city";
	}

    @RequestMapping(value="/editor/menu/resource", method = RequestMethod.GET)
    public String showResourceMenu(ModelMap map)
    {
        map.addAttribute("items", itemRepository.findAll());

        return "editor/building";
    }

    @RequestMapping(value="/editor/menu/building", method = RequestMethod.GET)
    public String showBuildingMenu(ModelMap map)
    {
        map.addAttribute("buildingBlueprints", buildingBlueprintRepository.findAll(new Sort(Sort.Direction.ASC, "_id")));

        return "editor/building";
    }

    @RequestMapping(value="/editor/menu/unit", method = RequestMethod.GET)
    public String showUnitMenu(ModelMap map)
    {
        map.addAttribute("unitBlueprints", unitBlueprintRepository.findAll(new Sort(Sort.Direction.ASC, "_id")));

        return "editor/unit";
    }

    @RequestMapping(value="/editor/building/setDesc", method = RequestMethod.GET)
    @ResponseBody
    public String setBuildingDesc(@RequestParam("id") int id, @RequestParam("value") String value)
    {
        BuildingBlueprint bp = buildingBlueprintRepository.findOne(id);
        bp.setDescription(value);
        buildingBlueprintRepository.save(bp);
        return "OK";
    }

    @RequestMapping(value="/editor/building/setProduction", method = RequestMethod.GET)
    @ResponseBody
    public String setBuildingProduction(@RequestParam("id") int id, @RequestParam("value") int value)
    {
        BuildingBlueprint bp = buildingBlueprintRepository.findOne(id);
        bp.setRequiredProduction(value);
        buildingBlueprintRepository.save(bp);
        return "OK";
    }

    @RequestMapping(value="/editor/building/setPrice", method = RequestMethod.GET)
    @ResponseBody
    public String setBuildingPrice(@RequestParam("id") int id, @RequestParam("value") int value)
    {
        BuildingBlueprint bp = buildingBlueprintRepository.findOne(id);
        bp.setPrice(value);
        buildingBlueprintRepository.save(bp);
        return "OK";
    }

    @RequestMapping(value="/editor/unit/setDesc", method = RequestMethod.GET)
    @ResponseBody
    public String setUnitDesc(@RequestParam("id") int id, @RequestParam("value") String value)
    {
        UnitBlueprint bp = unitBlueprintRepository.findOne(id);
        bp.setDescription(value);
        unitBlueprintRepository.save(bp);
        return "OK";
    }

    @RequestMapping(value="/editor/unit/setProduction", method = RequestMethod.GET)
    @ResponseBody
    public String setUnitProduction(@RequestParam("id") int id, @RequestParam("value") int value)
    {
        UnitBlueprint bp = unitBlueprintRepository.findOne(id);
        bp.setRequiredProduction(value);
        unitBlueprintRepository.save(bp);
        return "OK";
    }

    @RequestMapping(value="/editor/unit/setPrice", method = RequestMethod.GET)
    @ResponseBody
    public String setUnitPrice(@RequestParam("id") int id, @RequestParam("value") int value)
    {
        UnitBlueprint bp = unitBlueprintRepository.findOne(id);
        bp.setPrice(value);
        unitBlueprintRepository.save(bp);
        return "OK";
    }

    @RequestMapping(value="/editor/unit/setSpeed", method = RequestMethod.GET)
    @ResponseBody
    public String setUnitSpeed(@RequestParam("id") int id, @RequestParam("value") int value)
    {
        UnitBlueprint bp = unitBlueprintRepository.findOne(id);
        bp.setSpeed(value);
        unitBlueprintRepository.save(bp);
        return "OK";
    }
}
