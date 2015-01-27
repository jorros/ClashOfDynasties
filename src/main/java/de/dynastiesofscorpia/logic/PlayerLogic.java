package de.dynastiesofscorpia.logic;

import de.dynastiesofscorpia.models.*;
import de.dynastiesofscorpia.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PlayerLogic {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CaravanRepository caravanRepository;

    @Autowired
    private FormationRepository formationRepository;

    @Autowired
    private RoadRepository roadRepository;

    @Autowired
    private RelationRepository relationRepository;

    public void processStatistics(Player player) {
        Statistic stat = player.getStatistic();

        if(stat == null && !player.isComputer() && player.isActivated()) {
            stat = new Statistic();
        }

        if(stat != null) {
            List<City> cities = cityRepository.findByPlayer(player);
            List<Formation> formations = formationRepository.findByPlayer(player);
            stat.setDemography(0);
            stat.setMilitary(0);
            stat.setEconomy(0);

            for(City city : cities) {
                stat.addDemography(city.getPopulation());
                stat.addDemography(city.getSatisfaction());
                stat.addDemography(10 * city.getType().getId() < 4 ? city.getType().getId() : 1);

                stat.addMilitary(city.getDefencePoints());

                stat.addEconomy(city.getIncome() - city.getOutcome());

                if(city.getBuildings() != null) {
                    for(Building building : city.getBuildings()) {
                        stat.addEconomy(new Double(building.getBlueprint().getProducePerStep() * 3600).intValue());
                    }
                }
            }

            stat.addEconomy(caravanRepository.findByPlayer(player).size() * 10);

            for(Formation formation : formations) {
                stat.addMilitary(formation.getStrength());
                stat.addEconomy(-formation.getCosts());
            }

            stat.addEconomy(player.getCoins() / 10);

            player.setStatistic(stat);
        }
    }

    public void processRanking() {
        List<Player> ranking = playerRepository.getList().stream().filter(p -> p.getStatistic() != null).collect(Collectors.toList());
        Collections.sort(ranking, (Player p1, Player p2) -> Integer.compare(p2.getStatistic().getTotal(), p1.getStatistic().getTotal()));

        for(int i = 1; i <= ranking.size(); i++) {
            ranking.get(i - 1).getStatistic().setRank(i);
        }
    }

    public void updateFOW(Player player) {
        List<City> cities = cityRepository.getList();
        cities.forEach(c -> c.removeVisibility(player));

        cities.forEach(c -> c.removeVisibility(player));

        for(City city : cities) {
            List<Road> roads = roadRepository.findByCity(city);

            if(city.getPlayer().equals(player))
                setVisible(city, city.getType().getId(), cities, player);
            else {
                Relation relation = relationRepository.findByPlayers(player, city.getPlayer());

                if(relation != null) {
                    if(relation.getRelation() == 3)
                        setVisible(city, city.getType().getId(), cities, player);
                    else if(relation.getRelation() == 2)
                        city.addVisibility(player);
                }
            }

            roads.forEach(Road::updateTimestamp);
        }
    }

    private void setVisible(City city, int level, List<City> cities, Player player) {
        city.addVisibility(player);

        for (Road r : roadRepository.findByCity(city)) {
            City other;

            if(r.getPoint1().equals(city))
                other = cities.get(cities.indexOf(r.getPoint2()));
            else
                other = cities.get(cities.indexOf(r.getPoint1()));

            if(!other.isVisible(player)) {
                other.addVisibility(player);
            }

            if(level > 1)
                setVisible(other, level - 1, cities, player);
        }
    }
}
