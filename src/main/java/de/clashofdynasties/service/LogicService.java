package de.clashofdynasties.service;

import de.clashofdynasties.logic.CityLogic;
import de.clashofdynasties.logic.PlayerLogic;
import de.clashofdynasties.models.*;
import de.clashofdynasties.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LogicService {
    @Autowired
    private CityLogic cityLogic;

    @Autowired
    private PlayerLogic playerLogic;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private PlayerRepository playerRepository;

    private long tick = 599;

    @Scheduled(fixedDelay = 1000)
    public void Worker() {
        try {
            processCities();
            processPlayer();
            processFormations();
            processCaravans();
            processWar();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    private void processCities() {
        List<City> cities = cityRepository.findAll();

        for(City city : cities) {
            cityLogic.processPopulation(city);
            cityLogic.processProduction(city);
            cityLogic.processCoins(city);
            cityLogic.processConstruction(city);
            cityLogic.processType(city);

            cityRepository.save(city);
        }
    }

    private void processPlayer() {
        List<Player> players = playerRepository.findAll();
        tick++;

        if(tick == 600) {
        for(Player player : players) {
                playerLogic.processStatistics(player);
                playerRepository.save(player);
            }
            tick = 0;
            playerLogic.processRanking();
        }
    }

    private void processFormations() {

    }

    private void processCaravans() {

    }

    private void processWar() {

    }
}
