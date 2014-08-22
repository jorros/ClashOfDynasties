package de.clashofdynasties.logic;

import de.clashofdynasties.models.City;
import de.clashofdynasties.models.Formation;
import org.springframework.stereotype.Component;

@Component
public class FormationLogic {
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
                    formation.setCurrentRoad(formation.getRoute().getRoads().get(0));
                    formation.getRoute().getRoads().remove(0);
                    formation.move(70);
                } else {
                    // Ziel erreicht
                    formation.setLastCity(next);
                    formation.setCurrentRoad(null);
                    formation.setRoute(null);
                    formation.setX(next.getX());
                    formation.setY(next.getY());
                }
            } else {
                double multiplier = formation.getSpeed() / distance;

                formation.setX(formation.getX() + (vecX * multiplier * formation.getCurrentRoad().getWeight()));
                formation.setY(formation.getY() + (vecY * multiplier * formation.getCurrentRoad().getWeight()));
            }

            formation.updateTimestamp();
        }
    }
}
