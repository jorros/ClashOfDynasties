package com.dynastiesofscorpia.game;

import com.dynastiesofscorpia.models.*;
import com.dynastiesofscorpia.repository.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/game/players")
@Secured("ROLE_USER")
public class PlayerController {
    @Autowired
    private FormationRepository formationRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CaravanRepository caravanRepository;

    @Autowired
    private RelationRepository relationRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private MessageRepository messageRepository;

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
        player.setCoins(200);
        player.setName("Neuer Spieler #" + num);
        player.setLastScrollX(-1);
        player.setLastScrollY(-1);

        playerRepository.add(player);
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void save(Principal principal, String email, String oldpw, String newpw, String newpw2, boolean CityConquered, boolean CityLost, boolean CityUpgrade, boolean DiplomaticAlliance, boolean DiplomaticPeace, boolean DiplomaticTrade, boolean DiplomaticWar, boolean Disease, boolean Fire, boolean Loss, boolean NewMessage, boolean ProductionReady, boolean Trade, boolean TradeNotEnoughLoaded, boolean War, boolean Wonder) {
        Player player = playerRepository.findByName(principal.getName());

        player.setEmail(email);

        Md5PasswordEncoder encoder = new Md5PasswordEncoder();

        if(!oldpw.equals("") && !newpw.equals("") && !newpw2.equals("")) {
            if(player.getPassword().equals(encoder.encodePassword(oldpw, null)) && newpw.equals(newpw2)) {
                player.setPassword(encoder.encodePassword(newpw, null));
            }
        }

        player.setNotification("CityConquered", CityConquered);
        player.setNotification("CityLost", CityLost);
        player.setNotification("CityUpgrade", CityUpgrade);
        player.setNotification("DiplomaticAlliance", DiplomaticAlliance);
        player.setNotification("DiplomaticPeace", DiplomaticPeace);
        player.setNotification("DiplomaticTrade", DiplomaticTrade);
        player.setNotification("DiplomaticWar", DiplomaticWar);
        player.setNotification("Disease", Disease);
        player.setNotification("Fire", Fire);
        player.setNotification("Loss", Loss);
        player.setNotification("NewMessage", NewMessage);
        player.setNotification("ProductionReady", ProductionReady);
        player.setNotification("Trade", Trade);
        player.setNotification("TradeNotEnoughLoaded", TradeNotEnoughLoaded);
        player.setNotification("War", War);
        player.setNotification("Wonder", Wonder);
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
                            relation.setTicksLeft(86400d);
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
                            relation.setTicksLeft(86400d);
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

    @RequestMapping(value = "/{player}/message", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void sendMessage(Principal principal, @PathVariable("player") ObjectId playerId, @RequestParam String content) {
        Player player = playerRepository.findByName(principal.getName());
        Player other = playerRepository.findById(playerId);

        Message message = new Message();
        message.setFrom(player);
        message.setTo(other);
        message.setMessage(content);
        message.setUnread(true);

        eventRepository.add(new Event("NewMessage", "Neue Nachricht von " + player.getName(), "Du hast eine neue Nachricht erhalten!", "messages?pid=" + player.getId(), other));

        messageRepository.add(message);
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
        List<Relation> relations = relationRepository.finyByPlayer(player);
        List<Event> events = eventRepository.findByPlayer(player);
        List<Message> messages = messageRepository.findByPlayer(player);

        if(!events.isEmpty())
            eventRepository.remove(events);

        if(!relations.isEmpty())
            relationRepository.remove(relations);

        if(!messages.isEmpty())
            messageRepository.remove(messages);

        if(caravans.size() > 0)
            caravanRepository.remove(caravans);

        if(formations.size() > 0)
            formationRepository.remove(formations);

        for(City city : cities) {
            city.clearBuildings(true);
            city.setItems(new HashMap<>());
            city.setHealth(100);
            city.setPlayer(players.stream().filter(Player::isComputer).findFirst().get());
            city.setPopulation(5);
            city.setSatisfaction(100);
            city.clearUnits(true);
            city.updateTimestamp();
        }

        player.setLevel(0);
        player.getObjectives().clear();
        players.forEach(p -> p.setSightUpdate(true));

        player.setLastScrollX(-1);
        player.setLastScrollY(-1);
        player.setCoins(200);
    }
}
