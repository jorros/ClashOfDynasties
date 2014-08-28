package de.clashofdynasties.logic;

import de.clashofdynasties.models.City;
import de.clashofdynasties.models.Formation;
import de.clashofdynasties.models.Unit;
import de.clashofdynasties.repository.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FormationLogic {
    @Autowired
    private UnitRepository unitRepository;

    public void processMovement(Formation formation) {
        if(formation.getRoute() != null) {
            City next = formation.getRoute().getNext();

            double vecX = next.getX() - formation.getX();
            double vecY = next.getY() - formation.getY();
            double distance = Math.sqrt(Math.pow(next.getX() - formation.getX(), 2) + Math.pow(next.getY() - formation.getY(), 2));

            if(distance <= 70) {
                if(formation.getRoute().getRoads().size() > 0) {
                    // Nächste Station ermitteln
                    City to = formation.getRoute().getRoads().get(0).getPoint1().equals(next) ? formation.getRoute().getRoads().get(0).getPoint2() : formation.getRoute().getRoads().get(0).getPoint1();

                    formation.setX(next.getX());
                    formation.setY(next.getY());
                    formation.getRoute().setNext(to);
                    formation.getRoute().setCurrentRoad(formation.getRoute().getRoads().get(0));
                    formation.getRoute().getRoads().remove(0);
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

    public void processHealing(Formation formation) {
        if(formation.isDeployed() && formation.getLastCity().getReport() == null) {
            for (Unit unit : formation.getUnits()) {
                if(Math.random() < 0.01 && unit.getHealth() < 100) {
                    unit.setHealth(unit.getHealth() + 1);
                }

                unitRepository.save(unit);
            }
        }
    }
}
