package de.clashofdynasties.logic;

import de.clashofdynasties.models.City;
import de.clashofdynasties.models.Formation;
import de.clashofdynasties.models.Relation;
import de.clashofdynasties.models.Unit;
import de.clashofdynasties.repository.RelationRepository;
import de.clashofdynasties.repository.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FormationLogic {
    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private RelationRepository relationRepository;

    public void processMovement(Formation formation) {
        if(formation.getRoute() != null) {
            City next = formation.getRoute().getNext();

            double vecX = next.getX() - formation.getX();
            double vecY = next.getY() - formation.getY();
            double distance = Math.sqrt(Math.pow(next.getX() - formation.getX(), 2) + Math.pow(next.getY() - formation.getY(), 2));

            int relation;

            if(next.getPlayer().equals(formation.getPlayer()))
                relation = 4;
            else {
                Relation rel = relationRepository.findByPlayers(next.getPlayer(), formation.getPlayer());

                if(rel == null)
                    relation = 1;
                else
                    relation = rel.getRelation();
            }

            if(distance <= 70) {
                if(formation.getRoute().getRoads().size() > 0 && !formation.getRoute().getTarget().equals(next) && relation >= 3) {
                    // Nächste Station ermitteln
                    City to = formation.getRoute().getRoads().get(0).getPoint1().equals(next) ? formation.getRoute().getRoads().get(0).getPoint2() : formation.getRoute().getRoads().get(0).getPoint1();

                    formation.setX(next.getX());
                    formation.setY(next.getY());
                    formation.getRoute().setNext(to);
                    formation.getRoute().setCurrentRoad(formation.getRoute().getRoads().get(0));
                    formation.getRoute().removeRoad(0);
                    formation.move(70);
                } else {
                    // Ziel erreicht
                    formation.setLastCity(next);
                    formation.setRoute(null);
                    formation.setX(next.getX());
                    formation.setY(next.getY());
                }
            } else {
                double multiplier = formation.getSpeed() / distance;

                formation.setX(formation.getX() + (vecX * multiplier * formation.getRoute().getCurrentRoad().getWeight()));
                formation.setY(formation.getY() + (vecY * multiplier * formation.getRoute().getCurrentRoad().getWeight()));
            }

            formation.updateTimestamp();
        }
    }

    public void processMaintenance(Formation formation) {
        formation.getPlayer().addCoins(-2 / 3600);
    }

    public void processHealing(Formation formation) {
        if(formation.isDeployed() && formation.getLastCity().getReport() == null) {
            if(formation.getLastCity().getBuildings().stream().filter(b -> b.getBlueprint().getId() == 10).count() > 0) {
                List<Unit> units = formation.getUnits().parallelStream().filter(u -> u.getHealth() < 100).collect(Collectors.toList());

                for (Unit unit : units) {
                    if (Math.random() < 0.01) {
                        unit.setHealth(unit.getHealth() + 1);
                    }
                }

                formation.recalculateHealth();
            }
        }
    }
}
