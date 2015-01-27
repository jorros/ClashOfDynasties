package de.dynastiesofscorpia.logic;

import de.dynastiesofscorpia.models.Event;
import de.dynastiesofscorpia.models.Player;
import de.dynastiesofscorpia.models.Relation;
import de.dynastiesofscorpia.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DiplomacyLogic {
    @Autowired
    private EventRepository eventRepository;

    public void processTimer(Relation relation, double delta) {
        if(relation.getTicksLeft() != null) {
            relation.setTicksLeft(relation.getTicksLeft() - (1 * delta) );

            if(relation.getTicksLeft() <= 0) {
                relation.setTicksLeft(null);
                Player pl1 = relation.getPlayer1();
                Player pl2 = relation.getPlayer2();

                relation.setRelation(relation.getRelation() - 1);

                if(relation.getRelation() == 2) {
                    eventRepository.add(new Event("DiplomaticAlliance", "Allianz aufgelöst", "Die Allianz mit " + pl2.getName() + " wurde endgültig aufgelöst! Ihr seid ab sofort nur noch Handelspartner.", "diplomacy?pid=" + pl2.getId(), pl1));
                    eventRepository.add(new Event("DiplomaticAlliance", "Allianz aufgelöst", "Die Allianz mit " + pl1.getName() + " wurde endgültig aufgelöst! Ihr seid ab sofort nur noch Handelspartner.", "diplomacy?pid=" + pl1.getId(), pl2));
                }
                else if(relation.getRelation() == 1) {
                    eventRepository.add(new Event("DiplomaticTrade", "Handelsvertrag aufgelöst", "Das Handelsabkommen mit " + pl2.getName() + " ist nicht mehr gültig!.", "diplomacy?pid=" + pl2.getId(), pl1));
                    eventRepository.add(new Event("DiplomaticTrade", "Handelsvertrag aufgelöst", "Das Handelsabkommen mit " + pl1.getName() + " ist nicht mehr gültig!.", "diplomacy?pid=" + pl1.getId(), pl2));
                }

                pl1.setSightUpdate(true);
                pl2.setSightUpdate(true);
            }
        }
    }
}
