package de.clashofdynasties.logic;

import de.clashofdynasties.models.Event;
import de.clashofdynasties.models.Player;
import de.clashofdynasties.models.Relation;
import de.clashofdynasties.repository.EventRepository;
import de.clashofdynasties.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DiplomacyLogic {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private PlayerLogic playerLogic;

    public void processTimer(Relation relation) {
        if(relation.getTicksLeft() != null) {
            relation.setTicksLeft(relation.getTicksLeft() - 1);

            if(relation.getTicksLeft() <= 0) {
                relation.setTicksLeft(null);
                Player pl1 = playerRepository.findOne(relation.getPlayer1().getId());
                Player pl2 = playerRepository.findOne(relation.getPlayer2().getId());

                if(relation.getRelation() == 3) {
                    eventRepository.save(new Event("DiplomaticAlliance", "Allianz aufgelöst", "Die Allianz mit " + pl2.getName() + " wurde endgültig aufgelöst! Ihr seid ab sofort nur noch Handelspartner.", "diplomacy?pid=" + pl2.getId(), pl1));
                    eventRepository.save(new Event("DiplomaticAlliance", "Allianz aufgelöst", "Die Allianz mit " + pl1.getName() + " wurde endgültig aufgelöst! Ihr seid ab sofort nur noch Handelspartner.", "diplomacy?pid=" + pl1.getId(), pl2));
                }
                else if(relation.getRelation() == 2) {
                    eventRepository.save(new Event("DiplomaticTrade", "Handelsvertrag aufgelöst", "Das Handelsabkommen mit " + pl2.getName() + " ist nicht mehr gültig!.", "diplomacy?pid=" + pl2.getId(), pl1));
                    eventRepository.save(new Event("DiplomaticTrade", "Handelsvertrag aufgelöst", "Das Handelsabkommen mit " + pl1.getName() + " ist nicht mehr gültig!.", "diplomacy?pid=" + pl1.getId(), pl2));

                    playerLogic.updateFOW(pl1);
                    playerLogic.updateFOW(pl2);
                }

                relation.setRelation(relation.getRelation() - 1);
            }
        }
    }
}
