package de.clashofdynasties.service;

import com.mongodb.Mongo;
import de.clashofdynasties.logic.*;
import de.clashofdynasties.models.*;
import de.clashofdynasties.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
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

    @Autowired
    private DiplomacyLogic diplomacyLogic;

    @Autowired
    private RelationRepository relationRepository;

    private long tick = 599;
    private long tickWar = 29;

    @Scheduled(fixedDelay = 1000)
    public void Worker() {
        try {
            processCities();
            processPlayer();
            processFormations();
            processCaravans();
            processDiplomacy();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    private void processCities() {
        List<City> cities = cityRepository.findAll();
        tickWar++;

        for(City city : cities) {
            if(tickWar == 30) {
                cityLogic.processWar(city);
            }
            cityLogic.processPopulation(city);
            cityLogic.processProduction(city);
            cityLogic.processCoins(city);
            cityLogic.processConstruction(city);
            cityLogic.processType(city);
            cityLogic.processHealing(city);

            cityRepository.save(city);
        }

        if(tickWar == 30) {
            tickWar = 0;
        }
    }

    private void processPlayer() {
        List<Player> players = playerRepository.findAll();
        tick++;

        for(Player player : players) {
            if(tick == 600) {
                playerLogic.processStatistics(player);
                playerLogic.updateFOW(player);
                playerRepository.save(player);
            } else if(player.isSightUpdate()) {
                playerLogic.updateFOW(player);
                player.setSightUpdate(false);
                playerRepository.save(player);
            }
        }

        if(tick == 600) {
            playerLogic.processRanking();
            tick = 0;
        }
    }

    private void processFormations() {
        List<Formation> formations = formationRepository.findAll();

        for(Formation formation : formations) {
            formationLogic.processMovement(formation);
            formationLogic.processHealing(formation);

            formationRepository.save(formation);
        }
    }

    private void processCaravans() {
        List<Caravan> caravans = caravanRepository.findAll();

        for(Caravan caravan : caravans) {
            caravanLogic.processMovement(caravan);

            if(caravan.isTerminate())
                caravanRepository.delete(caravan);
            else
                caravanRepository.save(caravan);
        }
    }

    private void processDiplomacy() {
        List<Relation> relations = relationRepository.findAll();

        for(Relation relation : relations) {
            diplomacyLogic.processTimer(relation);

            relationRepository.save(relation);
        }
    }
}
