package de.clashofdynasties.game;

import de.clashofdynasties.models.Caravan;
import de.clashofdynasties.models.City;
import de.clashofdynasties.models.Formation;
import de.clashofdynasties.repository.*;
import de.clashofdynasties.service.RoutingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/game/commands")
public class CommandController {
    @Autowired
    FormationRepository formationRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    CityRepository cityRepository;

    @Autowired
    CityTypeRepository cityTypeRepository;

    @Autowired
    ResourceRepository resourceRepository;

    @Autowired
    CaravanRepository caravanRepository;

    @Autowired
    BiomeRepository biomeRepository;

    @Autowired
    RoutingService routing;

    @RequestMapping(value = "/formation", method = RequestMethod.GET)
    public String showFormation(ModelMap map, Principal principal, @RequestParam("formation") int id) {
        Formation formation = formationRepository.findOne(id);

        String time = "";

        if (!formation.isDeployed()) {
            int seconds = routing.calculateTime(formation, formation.getRoute());
            int hr = (int) (seconds / 3600);
            int rem = (int) (seconds % 3600);
            int mn = rem / 60;

            if (hr > 0)
                time += hr + " Stunden ";

            if (mn > 0)
                time += mn + " Minuten";
        }

        map.addAttribute("player", playerRepository.findByName(principal.getName()));
        map.addAttribute("formation", formation);
        map.addAttribute("time", time);

        return "command/formation";
    }

    @RequestMapping(value = "/caravan", method = RequestMethod.GET)
    public String showCaravan(ModelMap map, Principal principal, @RequestParam("caravan") int id) {
        Caravan caravan = caravanRepository.findOne(id);

        String time = "";

        int seconds = routing.calculateTime(caravan.getRoute());
        int hr = (int) (seconds / 3600);
        int rem = (int) (seconds % 3600);
        int mn = rem / 60;

        if (hr > 0)
            time += hr + " Stunden ";

        if (mn > 0)
            time += mn + " Minuten";

        map.addAttribute("player", playerRepository.findByName(principal.getName()));
        map.addAttribute("caravan", caravan);
        map.addAttribute("time", time);

        return "command/caravan";
    }

    @RequestMapping(value = "/city", method = RequestMethod.GET)
    public String showCity(ModelMap map, Principal principal, @RequestParam("city") int id) {
        City city = cityRepository.findOne(id);

        int maxSlots = Math.round((int)(city.getCapacity() * city.getType().getCapacity()));
        int freeSlots = maxSlots;

        if (city.getBuildings() != null)
            freeSlots -= city.getBuildings().size();

        String smiley = "Happy";

        if (city.getPopulation() > 0) {
            if (city.getSatisfaction() < 80 && city.getSatisfaction() >= 60)
                smiley = "Satisfied";
            else if (city.getSatisfaction() < 60 && city.getSatisfaction() >= 30)
                smiley = "Unhappy";
            else
                smiley = "Angry";
        } else
            smiley = null;

        map.addAttribute("satisfaction", smiley);
        map.addAttribute("player", playerRepository.findByName(principal.getName()));
        map.addAttribute("city", city);
        map.addAttribute("freeSlots", freeSlots);
        map.addAttribute("maxSlots", maxSlots);

        return "command/city";
    }

    @RequestMapping(value = "/editcity", method = RequestMethod.GET)
    public String showEditCity(ModelMap map, @RequestParam("city") int id) {
        City city = cityRepository.findOne(id);

        map.addAttribute("city", city);

        map.addAttribute("types", cityTypeRepository.findAll());
        map.addAttribute("resources", resourceRepository.findAll());
        map.addAttribute("biomes", biomeRepository.findAll());
        map.addAttribute("players", playerRepository.findAll());

        return "command/editcity";
    }
}
