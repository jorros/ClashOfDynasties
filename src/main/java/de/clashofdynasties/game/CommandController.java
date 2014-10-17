package de.clashofdynasties.game;

import de.clashofdynasties.models.Caravan;
import de.clashofdynasties.models.City;
import de.clashofdynasties.models.Formation;
import de.clashofdynasties.models.Player;
import de.clashofdynasties.repository.*;
import de.clashofdynasties.service.RoutingService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/game/commands")
@Secured("ROLE_USER")
public class CommandController {
    @Autowired
    private FormationRepository formationRepository;

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
    RelationRepository relationRepository;

    @Autowired
    private RoadRepository roadRepository;

    @RequestMapping(value = "/formation", method = RequestMethod.GET)
    public String showFormation(ModelMap map, Principal principal, @RequestParam("formation") ObjectId id) {
        Formation formation = formationRepository.findById(id);
        RoutingService routing = new RoutingService(roadRepository, relationRepository);
        Player player = playerRepository.findByName(principal.getName());

        String time = "";

        if (!formation.isDeployed()) {

            routing.setRoute(formation.getRoute());
            routing.setFormation(formation);
            int seconds = routing.calculateTime();
            int hr = (int) (seconds / 3600);
            int rem = (int) (seconds % 3600);
            int mn = rem / 60;

            if (hr > 0)
                time += hr + " Stunden ";

            if (mn > 0)
                time += mn + " Minuten";

            if(seconds < 60)
                time = "Unter 1 Minute";
        }

        map.addAttribute("player", player);
        map.addAttribute("formation", formation);
        map.addAttribute("time", time);
        map.addAttribute("visible", formation.isVisible(player));

        return "command/formation";
    }

    @RequestMapping(value = "/caravan", method = RequestMethod.GET)
    public String showCaravan(ModelMap map, Principal principal, @RequestParam("caravan") ObjectId id) {
        Caravan caravan = caravanRepository.findById(id);
        RoutingService routing = new RoutingService(roadRepository, relationRepository);
        Player player = playerRepository.findByName(principal.getName());

        String time = "";

        routing.setRoute(caravan.getRoute());
        int seconds = routing.calculateTime();
        int hr = (int) (seconds / 3600);
        int rem = (int) (seconds % 3600);
        int mn = rem / 60;

        if (hr > 0)
            time += hr + " Stunden ";

        if (mn > 0)
            time += mn + " Minuten";

        map.addAttribute("player", player);
        map.addAttribute("caravan", caravan);
        map.addAttribute("time", time);
        map.addAttribute("visible", caravan.isVisible(player));

        return "command/caravan";
    }

    @RequestMapping(value = "/city", method = RequestMethod.GET)
    public String showCity(ModelMap map, Principal principal, @RequestParam("city") ObjectId id) {
        City city = cityRepository.findById(id);
        Player player = playerRepository.findByName(principal.getName());

        int maxSlots = city.getCapacity();
        int freeSlots = maxSlots;

        if (city.getBuildings() != null)
            freeSlots -= city.getBuildings().size();

        String smiley;

        if (city.getPopulation() > 0) {
            if(city.getSatisfaction() >= 80)
                smiley = "Happy";
            else if (city.getSatisfaction() < 80 && city.getSatisfaction() >= 60)
                smiley = "Satisfied";
            else if (city.getSatisfaction() < 60 && city.getSatisfaction() >= 30)
                smiley = "Unhappy";
            else
                smiley = "Angry";
        } else
            smiley = null;

        map.addAttribute("satisfaction", smiley);
        map.addAttribute("player", player);
        map.addAttribute("city", city);
        map.addAttribute("freeSlots", freeSlots);
        map.addAttribute("maxSlots", maxSlots);
        map.addAttribute("canTrade", city.getBuildings().stream().filter(b -> b.getBlueprint().getId() == 12).count() > 0);
        map.addAttribute("visible", city.isVisible(player));

        return "command/city";
    }

    @RequestMapping(value = "/editcity", method = RequestMethod.GET)
    public String showEditCity(ModelMap map, @RequestParam("city") ObjectId id) {
        City city = cityRepository.findById(id);

        map.addAttribute("city", city);

        map.addAttribute("types", cityTypeRepository.getList());
        map.addAttribute("resources", resourceRepository.getList());
        map.addAttribute("biomes", biomeRepository.getList());
        map.addAttribute("players", playerRepository.getList());

        return "command/editcity";
    }
}
