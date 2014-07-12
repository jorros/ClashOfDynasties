package de.clashofdynasties.game;

import de.clashofdynasties.models.Formation;
import de.clashofdynasties.models.Player;
import de.clashofdynasties.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/game/players")
public class PlayerController {
    @Autowired
    FormationRepository formationRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    CityRepository cityRepository;

    @Autowired
    UnitRepository unitRepository;

    @Autowired
    CaravanRepository caravanRepository;

    @RequestMapping(value = "/{player}/units", method = RequestMethod.DELETE)
    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.OK)
    public void removeUnits(@PathVariable("player") String playerId) {
        Player player = playerRepository.findOne(playerId);

        List<Formation> formations = formationRepository.findByPlayer(player);

        formations.forEach(f -> unitRepository.delete(f.getUnits()));

        formationRepository.delete(formations);
        caravanRepository.delete(caravanRepository.findByPlayer(player));
    }

    @RequestMapping(value = "/{player}", method = RequestMethod.DELETE)
    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.OK)
    public void remove(@PathVariable("player") String playerId) {
        playerRepository.delete(playerId);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.OK)
    public void create() {
        Player player = new Player();
        player.setActivated(false);
        player.setCoins(100);
        player.setName("Neuer Spieler");

        playerRepository.save(player);
    }

    @RequestMapping(value = "/reset/{player}", method = RequestMethod.PUT)
    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.OK)
    public void reset(@PathVariable("player") String playerId) {
    }
}
