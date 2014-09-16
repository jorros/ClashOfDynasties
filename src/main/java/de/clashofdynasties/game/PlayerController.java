package de.clashofdynasties.game;

import de.clashofdynasties.logic.PlayerLogic;
import de.clashofdynasties.models.*;
import de.clashofdynasties.repository.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/game/players")
public class PlayerController {
    @Autowired
    private FormationRepository formationRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private CaravanRepository caravanRepository;

    @Autowired
    private RelationRepository relationRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private PlayerLogic playerLogic;

    @RequestMapping(value = "/{player}", method = RequestMethod.DELETE)
    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.OK)
    public void remove(@PathVariable("player") ObjectId playerId) {
        reset(playerId);
        playerRepository.remove(playerRepository.findById(playerId));
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.OK)
    public void create() {
        int num = playerRepository.getList().size() - 1;
        Player player = new Player();
        player.setActivated(false);
        player.setCoins(100);
        player.setName("Neuer Spieler #" + num);
        player.setLastScrollX(-1);
        player.setLastScrollY(-1);

        playerRepository.add(player);
    }

    private void updateCityTimestamps(Player player) {
        List<City> cities = cityRepository.findByPlayer(player);

        cities.forEach(City::updateTimestamp);
    }

    @RequestMapping(value = "/relation", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void setRelation(Principal principal, String pid, int pendingRelation, boolean accept) {
        Player player = playerRepository.findByName(principal.getName());
        ObjectId otherId = new ObjectId(pid);
        Player other = playerRepository.findById(otherId);

        Relation relation = relationRepository.findByPlayers(player, other);
        Event event = null;

        if(relation.getTicksLeft() == null) {
            switch(pendingRelation) {
                case 0:
                    if(relation.getRelation() == 1) {
                        relation.setRelation(0);

                        event = new Event("DiplomaticWar", "Kriegserklärung", player.getName() + " hat euch den Krieg erklärt!", "diplomacy?pid=" + player.getId(), other);
                    }
                    else if(relation.getRelation() == 0 && (relation.getPendingRelation() == null || relation.getPendingRelation() != 1)) {
                        relation.setPendingRelation(1);
                        relation.setPendingRelationPlayer(player);

                        event = new Event("DiplomaticPeace", "Friedensangebot", player.getName() + " möchte diesen Krieg beenden und unterbreitet euch einen Friedensvorschlag.", "diplomacy?pid=" + player.getId(), other);
                    }
                    else if(relation.getRelation() == 0 && !relation.getPendingRelationPlayer().equals(player)) {
                        if(accept) {
                            relation.setRelation(1);
                            event = new Event("DiplomaticPeace", "Friedensangebot angenommen", player.getName() + " hat euer Friedensangebot angenommen.", "diplomacy?pid=" + player.getId(), other);
                        }
                        else
                            event = new Event("DiplomaticPeace", "Friedensangebot abgelehnt", player.getName() + " hat euer Friedensangebot abgelehnt.", "diplomacy?pid=" + player.getId(), other);

                        relation.setPendingRelation(null);
                        relation.setPendingRelationPlayer(null);
                    }
                    else if(relation.getRelation() == 0) {
                        relation.setPendingRelation(null);
                        relation.setPendingRelationPlayer(null);
                    }
                    break;

                case 2:
                    if(relation.getRelation() > 0) {
                        if(relation.getRelation() != 2 && (relation.getPendingRelation() == null || relation.getPendingRelation() != 2)) {
                            relation.setPendingRelation(2);
                            relation.setPendingRelationPlayer(player);

                            event = new Event("DiplomaticTrade", "Handelsabkommen vorgeschlagen", player.getName() + " hat euch einen Vorschlag gemacht einen Handelsabkommen zu unterzeichen.", "diplomacy?pid=" + player.getId(), other);
                        }
                        else if(relation.getRelation() != 2 && !relation.getPendingRelationPlayer().equals(player)) {
                            if(accept) {
                                relation.setRelation(2);
                                event = new Event("DiplomaticTrade", "Handelsabkommen angenommen", player.getName() + " hat ein Handelsabkommen mit euch unterzeichnet", "diplomacy?pid=" + player.getId(), other);
                            }
                            else
                                event = new Event("DiplomaticTrade", "Handelsabkommen abgelehnt", player.getName() + " hat euren Vorschlag zur Unterzeichnung eines Handelsabkommens nicht angenommen.", "diplomacy?pid=" + player.getId(), other);

                            relation.setPendingRelation(null);
                            relation.setPendingRelationPlayer(null);
                        }
                        else if(relation.getRelation() != 2) {
                            relation.setPendingRelation(null);
                            relation.setPendingRelationPlayer(null);
                        }
                        else {
                            relation.setTicksLeft(86400);
                            relation.setPendingRelation(null);
                            relation.setPendingRelationPlayer(null);
                            event = new Event("DiplomaticTrade", "Handelsabkommen aufgelöst", player.getName() + " hat den bestehenden Handelsvertrag aufgelöst. Der Vertrag wird in 24 Stunden endgültig aufgelöst!", "diplomacy?pid=" + player.getId(), other);
                        }
                    }
                    break;

                case 3:
                    if(relation.getRelation() > 0) {
                        if(relation.getRelation() != 3 && (relation.getPendingRelation() == null || relation.getPendingRelation() != 3)) {
                            relation.setPendingRelation(3);
                            relation.setPendingRelationPlayer(player);

                            event = new Event("DiplomaticAlliance", "Allianz vorgeschlagen", player.getName() + " will sich mit euch verbünden, um gemeinsam gegen das BÖÖSE der Welt zu kämpfen.", "diplomacy?pid=" + player.getId(), other);
                        }
                        else if(relation.getRelation() != 3 && !relation.getPendingRelationPlayer().equals(player)) {
                            if(accept) {
                                relation.setRelation(3);
                                event = new Event("DiplomaticAlliance", "Allianz akzeptiert", player.getName() + " befindet sich absofort in einer Allianz mit euch.", "diplomacy?pid=" + player.getId(), other);
                            }
                            else
                                event = new Event("DiplomaticAlliance", "Allianz abgelehnt", player.getName() + " wünscht keine Allianz mit euch!", "diplomacy?pid=" + player.getId(), other);

                            relation.setPendingRelation(null);
                            relation.setPendingRelationPlayer(null);
                        }
                        else if(relation.getRelation() != 3) {
                            relation.setPendingRelation(null);
                            relation.setPendingRelationPlayer(null);
                        }
                        else {
                            relation.setTicksLeft(86400);
                            relation.setPendingRelation(null);
                            relation.setPendingRelationPlayer(null);
                            event = new Event("DiplomaticAlliance", "Allianz ausgetreten", player.getName() + " hat die Allianz mit euch aufgelöst. Der Vertrag wird in 24 Stunden endgültig aufgelöst!", "diplomacy?pid=" + player.getId(), other);
                        }
                    }
                    break;
            }

            if(event != null) {
                eventRepository.add(event);
                updateCityTimestamps(player);
                updateCityTimestamps(other);

                player.setSightUpdate(true);
                other.setSightUpdate(true);
            }
        }
    }

    @RequestMapping(value = "/{player}/reset", method = RequestMethod.DELETE)
    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.OK)
    public void reset(@PathVariable("player") ObjectId playerId) {
        Player player = playerRepository.findById(playerId);

        List<Player> players = playerRepository.getList();
        List<City> cities = cityRepository.findByPlayer(player);
        List<Formation> formations = formationRepository.findByPlayer(player);
        List<Caravan> caravans = caravanRepository.findByPlayer(player);

        caravanRepository.remove(caravans);
        formationRepository.remove(formations);

        for(City city : cities) {
            city.clearBuildings();
            city.setItems(new HashMap<>());
            city.setHealth(100);
            city.setPlayer(players.stream().filter(Player::isComputer).findFirst().get());
            city.setPopulation(5);
            city.setSatisfaction(100);
            city.clearUnits();
            city.updateTimestamp();
        }

        players.forEach(p -> p.setSightUpdate(true));

        player.setLastScrollX(-1);
        player.setLastScrollY(-1);
        player.setCoins(100);
    }
}
