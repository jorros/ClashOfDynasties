package de.clashofdynasties.game;

import de.clashofdynasties.models.City;
import de.clashofdynasties.models.Formation;
import de.clashofdynasties.models.ItemType;
import de.clashofdynasties.models.Player;
import de.clashofdynasties.repository.*;
import de.clashofdynasties.service.CounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/game/cities")
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

    @Autowired
    ItemTypeRepository itemTypeRepository;

    @Autowired
    CityTypeRepository cityTypeRepository;

    @Autowired
    ResourceRepository resourceRepository;

    @RequestMapping(method = RequestMethod.GET)
	public @ResponseBody
    Map<Integer, City> getCities(Principal principal)
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
            else if(city.getPlayer().getId() == 1)
            {
                city.setDiplomacy(4);
                city.setSatisfaction(-1);
            }
            else
            {
                city.setDiplomacy(3);
            }

            data.put(city.getId(), city);
        }

		return data;
	}

    @RequestMapping(value = "/{city}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void remove(Principal principal, @PathVariable("city") int id)
    {
        cityRepository.delete(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void create(Principal principal, @RequestParam("name") String name, @RequestParam("type") int type, @RequestParam("capacity") int capacity, @RequestParam("resource") int resource)
    {
        // ToDo: Defaultwerte für Create
        save(principal, 0, name, type, capacity, resource);
    }

    @RequestMapping(value = "/{city}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void save(Principal principal, @PathVariable("city") int id, @RequestParam("name") String name, @RequestParam("type") int type, @RequestParam("capacity") int capacity, @RequestParam("resource") int resource)
    {
        // ToDo: nur gesetzte Werte einspeichern
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
    }
}
