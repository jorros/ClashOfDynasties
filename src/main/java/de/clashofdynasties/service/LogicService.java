package de.clashofdynasties.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class LogicService {


    @Scheduled(fixedDelay = 1000)
    public void Worker() {
        processCities();
        processRanking();
        processFormations();
        processCaravans();
        processDiplomacy();
        processWar();
    }

    private void processCities() {

    }

    private void processRanking() {

    }

    private void processFormations() {

    }

    private void processCaravans() {

    }

    private void processDiplomacy() {

    }

    private void processWar() {

    }
}
