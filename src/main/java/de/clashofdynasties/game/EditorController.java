package de.clashofdynasties.game;

import de.clashofdynasties.models.BuildingBlueprint;
import de.clashofdynasties.models.City;
import de.clashofdynasties.models.Road;
import de.clashofdynasties.models.UnitBlueprint;
import de.clashofdynasties.repository.*;
import de.clashofdynasties.service.CounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public String setUnitSpeed(@RequestParam("id") int id, @RequestParam("value") double value)
    {
        UnitBlueprint bp = unitBlueprintRepository.findOne(id);
        bp.setSpeed(value);
        unitBlueprintRepository.save(bp);
        return "OK";
    }
}
