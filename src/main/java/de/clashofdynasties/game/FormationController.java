package de.clashofdynasties.game;

import de.clashofdynasties.helper.Routing;
import de.clashofdynasties.models.*;
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
@RequestMapping("/game/formations")
public class FormationController
{
    @Autowired
    FormationRepository formationRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    CityRepository cityRepository;

    @Autowired
    UnitRepository unitRepository;

    @Autowired
    RoadRepository roadRepository;

    @Autowired
    CounterService counterService;

    @Autowired
    Routing routing;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    Map<Integer, Formation> getFormations(Principal principal) {
        Player player = playerRepository.findByName(principal.getName());

        List<Formation> formations = formationRepository.findAll();
        HashMap<Integer, Formation> data = new HashMap<Integer, Formation>();

        for (Formation formation : formations) {
            // Deploy Status ermitteln
            if (formation.getRoute() != null) {
                formation.setDeployed(false);
                formation.getRoute().setTime(routing.calculateTime(formation, formation.getRoute()));
            } else {
                formation.setDeployed(true);
            }

            // Diplomatie ermitteln
            if (player.equals(formation.getPlayer()))
                formation.setDiplomacy(1);

            // Wenn Spieler neutral
            if (formation.getPlayer().getId() == 1) {
                formation.setDiplomacy(4);
            }

            data.put(formation.getId(), formation);
        }

        return data;
    }

    @RequestMapping(value="/{formation}/route", method = RequestMethod.GET)
    public @ResponseBody Route calculateRoute(@PathVariable("formation") int id, @RequestParam int target)
    {
        Formation formation = formationRepository.findOne(id);
        City city = cityRepository.findOne(target);

        Route route = routing.calculateRoute(formation, city);
        route.setTime(routing.calculateTime(formation, route));

        return route;
    }

    @RequestMapping(value="/{formation}/move", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void move(Principal principal, @PathVariable("formation") int formationId, @RequestParam int target)
    {
        Formation formation = formationRepository.findOne(formationId);
        City city = cityRepository.findOne(target);
        Player player = playerRepository.findByName(principal.getName());

        if(player.equals(formation.getPlayer()))
        {
            Route route = routing.calculateRoute(formation, city);
            if(route != null)
            {
                if(formation.isDeployed())
                {
                    formation.setRoute(route);
                    formation.setCurrentRoad(roadRepository.findByCities(formation.getLastCity().getId(), route.getNext().getId()));
                    formation.move(70);
                }
                else
                    formation.setRoute(route);

                formationRepository.save(formation);
            }
        }
    }

    @RequestMapping(value="/{formation}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void remove(Principal principal, @PathVariable("formation") int formationId)
    {
        Player player = playerRepository.findByName(principal.getName());
        Formation formation = formationRepository.findOne(formationId);

        if(formation.getPlayer().equals(player) && formation.isDeployed() && formation.getLastCity().getPlayer().equals(formation.getPlayer()))
        {
            City city = formation.getLastCity();

            for(Unit unit : formation.getUnits())
            {
                city.getUnits().add(unit);
            }
            cityRepository.save(city);
            formationRepository.delete(formation);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void create(Principal principal, @RequestParam("name") String name, @RequestParam("city") int cityId, @RequestParam("units[]") List<Integer> unitsId)
    {
        save(principal, 0, name, cityId, unitsId);
    }

    @RequestMapping(value="/{formation}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void save(Principal principal, @PathVariable("formation") int formationId, @RequestParam("name") String name, @RequestParam("city") int cityId, @RequestParam("units[]") List<Integer> unitsId)
    {
        // Formation, City und Player vorbereiten
        City city = cityRepository.findOne(cityId);
        Player player = playerRepository.findByName(principal.getName());

        // Formation nur fetchen, wenn City dem Spieler gehört
        if(player.equals(city.getPlayer()))
        {
            Formation formation;
            if(formationId > 0)
                formation = formationRepository.findOne(formationId);
            else
            {
                formation = new Formation();
                formation.setId(counterService.getNextSequence("formation"));
                formation.setUnits(new ArrayList<Unit>());
                formation.setLastCity(city);
                formation.setRoute(null);
                formation.setPlayer(player);
                formation.setX(city.getX());
                formation.setY(city.getY());
            }

            if(player.equals(formation.getPlayer()))
            {
                if(formation.getRoute() == null)
                {
                    List<Unit> units = new ArrayList<Unit>();
                    for(int unitId : unitsId)
                    {
                        Unit unit = unitRepository.findOne(unitId);
                        if(city.getUnits().contains(unit))
                        {
                            city.getUnits().remove(unit);
                            formation.getUnits().add(unit);
                        }
                    }

                    formation.setName(name);

                    cityRepository.save(city);
                    formationRepository.save(formation);
                }
            }
        }
    }
}
