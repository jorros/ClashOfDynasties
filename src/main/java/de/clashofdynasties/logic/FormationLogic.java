package de.clashofdynasties.logic;

import de.clashofdynasties.models.City;
import de.clashofdynasties.models.Formation;
import org.springframework.stereotype.Component;

@Component
public class FormationLogic {
    public void processMovement(Formation formation) {
        if(formation.getRoute() != null) {
            City next = formation.getRoute().getNext();

            double angle = Math.atan2(next.getY() - formation.getY(), next.getX() - formation.getY());
            formation.setX((int)Math.round(formation.getX() + formation.getSpeed() * Math.cos(angle)));
            formation.setY((int) Math.round(formation.getY() + formation.getSpeed() * Math.sin(angle)));

            int vecX = next.getX() - formation.getX();
            int vecY = next.getY() - formation.getY();
            double distance = Math.sqrt(Math.pow(next.getX() - formation.getX(), 2) + Math.pow(next.getY() - formation.getY(), 2));

            if(distance <= 70) {
                if(formation.getRoute().getRoads().size() > 1) {
                    // Nächste Station ermitteln
                    formation.getRoute().getRoads().remove(0);
                    City to = formation.getRoute().getRoads().get(0).getPoint1().equals(next) ? formation.getRoute().getRoads().get(0).getPoint2() : formation.getRoute().getRoads().get(0).getPoint1();

                    formation.setX(next.getX());
                    formation.setY(next.getY());
                    formation.getRoute().setNext(to);
                    formation.move(70);
                } else {
                    // Ziel erreicht
                    formation.setLastCity(next);
                    formation.setCurrentRoad(null);
                    formation.setRoute(null);
                }
            } else {
                double multiplier = formation.getSpeed() / distance;

                formation.setX(new Double(formation.getX() + vecX * multiplier).intValue());
                formation.setY(new Double(formation.getY() + vecY * multiplier).intValue());
            }

            formation.updateTimestamp();
        }
    }
}
