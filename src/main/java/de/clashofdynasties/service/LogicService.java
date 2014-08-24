package de.clashofdynasties.service;

import de.clashofdynasties.logic.CaravanLogic;
import de.clashofdynasties.logic.CityLogic;
import de.clashofdynasties.logic.FormationLogic;
import de.clashofdynasties.logic.PlayerLogic;
import de.clashofdynasties.models.Caravan;
import de.clashofdynasties.models.City;
import de.clashofdynasties.models.Formation;
import de.clashofdynasties.models.Player;
import de.clashofdynasties.repository.CaravanRepository;
import de.clashofdynasties.repository.CityRepository;
import de.clashofdynasties.repository.FormationRepository;
import de.clashofdynasties.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Autowired
    private FormationRepository formationRepository;

    @Autowired
    private FormationLogic formationLogic;

    @Autowired
    private CaravanRepository caravanRepository;

    @Autowired
    private CaravanLogic caravanLogic;

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
        List<Formation> formations = formationRepository.findAll();

        for(Formation formation : formations) {
            formationLogic.processMovement(formation);

            formationRepository.save(formation);
        }
    }

    private void processCaravans() {
        List<Caravan> caravans = caravanRepository.findAll();

        for(Caravan caravan : caravans) {
            caravanLogic.processMovement(caravan);

            caravanRepository.save(caravan);
        }
    }

    private void processWar() {

    }
}
