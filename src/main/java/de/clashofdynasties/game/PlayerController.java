package de.clashofdynasties.game;

import de.clashofdynasties.models.Event;
import de.clashofdynasties.models.Formation;
import de.clashofdynasties.models.Player;
import de.clashofdynasties.models.Relation;
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

    @Autowired
    RelationRepository relationRepository;

    @Autowired
    EventRepository eventRepository;

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

    @RequestMapping(value = "/{player}/relation", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void setRelation(@PathVariable("player") String playerId, String otherId, int pendingRelation, boolean accept) {
        Player player = playerRepository.findOne(playerId);

        Relation relation = relationRepository.findByPlayers(new ObjectId(playerId), new ObjectId(otherId));
        Event event = null;

        if(relation.getTicksLeft() == null) {
            switch(pendingRelation) {
                case 0:
                    if(relation.getRelation() == 1) {
                        relation.setRelation(0);

                        event = new Event("DiplomaticWar", "Kriegserklärung", player.getName() + " hat euch den Krieg erklärt!", "diplomacy?pid=" + playerId);
                    }
                    else if(relation.getRelation() == 0 && (relation.getPendingRelation() == null || relation.getPendingRelation() != 1)) {
                        relation.setPendingRelation(1);
                        relation.setPendingRelationPlayer(player);

                        event = new Event("DiplomaticPeace", "Friedensangebot", player.getName() + " möchte diesen Krieg beenden und unterbreitet euch einen Friedensvorschlag.", "diplomacy?pid=" + playerId);
                    }
                    else if(relation.getRelation() == 0 && !relation.getPendingRelationPlayer().equals(player)) {
                        if(accept) {
                            relation.setRelation(1);
                            event = new Event("DiplomaticPeace", "Friedensangebot angenommen", player.getName() + " hat euer Friedensangebot angenommen.", "diplomacy?pid=" + playerId);
                        }
                        else
                            event = new Event("DiplomaticPeace", "Friedensangebot abgelehnt", player.getName() + " hat euer Friedensangebot abgelehnt.", "diplomacy?pid=" + playerId);

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

                            event = new Event("DiplomaticTrade", "Handelsabkommen vorgeschlagen", player.getName() + " hat euch einen Vorschlag gemacht einen Handelsabkommen zu unterzeichen.", "diplomacy?pid=" + playerId);
                        }
                        else if(relation.getRelation() != 2 && !relation.getPendingRelationPlayer().equals(player)) {
                            if(accept) {
                                relation.setRelation(2);
                                event = new Event("DiplomaticTrade", "Handelsabkommen angenommen", player.getName() + " hat ein Handelsabkommen mit euch unterzeichnet", "diplomacy?pid=" + playerId);
                            }
                            else
                                event = new Event("DiplomaticTrade", "Handelsabkommen abgelehnt", player.getName() + " hat euren Vorschlag zur Unterzeichnung eines Handelsabkommens nicht angenommen.", "diplomacy?pid=" + playerId);

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
                            event = new Event("DiplomaticTrade", "Handelsabkommen aufgelöst", player.getName() + " hat den bestehenden Handelsvertrag aufgelöst. Der Vertrag wird in 24 Stunden endgültig aufgelöst!", "diplomacy?pid=" + playerId);
                        }
                    }
                    break;

                case 3:
                    if(relation.getRelation() > 0) {
                        if(relation.getRelation() != 3 && (relation.getPendingRelation() == null || relation.getPendingRelation() != 3)) {
                            relation.setPendingRelation(3);
                            relation.setPendingRelationPlayer(player);

                            event = new Event("DiplomaticAlliance", "Allianz vorgeschlagen", player.getName() + " will sich mit euch verbünden, um gemeinsam gegen das BÖÖSE der Welt zu kämpfen.", "diplomacy?pid=" + playerId);
                        }
                        else if(relation.getRelation() != 3 && !relation.getPendingRelationPlayer().equals(player)) {
                            if(accept) {
                                relation.setRelation(3);
                                event = new Event("DiplomaticAlliance", "Allianz akzeptiert", player.getName() + " befindet sich absofort in einer Allianz mit euch.", "diplomacy?pid=" + playerId);
                            }
                            else
                                event = new Event("DiplomaticAlliance", "Allianz abgelehnt", player.getName() + " wünscht keine Allianz mit euch!", "diplomacy?pid=" + playerId);

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
                            event = new Event("DiplomaticAlliance", "Allianz ausgetreten", player.getName() + " hat die Allianz mit euch aufgelöst. Der Vertrag wird in 24 Stunden endgültig aufgelöst!", "diplomacy?pid=" + playerId);
                        }
                    }
                    break;
            }

            relationRepository.save(relation);

            if(event != null) {
                Player other = playerRepository.findOne(otherId);
                event.setPlayer(other);
                eventRepository.save(event);
            }
        }
    }

    @RequestMapping(value = "/reset/{player}", method = RequestMethod.PUT)
    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.OK)
    public void reset(@PathVariable("player") String playerId) {
    }
}
