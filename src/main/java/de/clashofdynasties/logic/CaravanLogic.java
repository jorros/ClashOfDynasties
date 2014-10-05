package de.clashofdynasties.logic;

import de.clashofdynasties.models.Caravan;
import de.clashofdynasties.models.City;
import de.clashofdynasties.models.Event;
import de.clashofdynasties.models.Road;
import de.clashofdynasties.repository.CaravanRepository;
import de.clashofdynasties.repository.CityRepository;
import de.clashofdynasties.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CaravanLogic {
    @Autowired
    private CaravanRepository caravanRepository;

    @Autowired
    private EventRepository eventRepository;

    public void processMovement(Caravan caravan) {
        City next = caravan.getRoute().getNext();

        double vecX = next.getX() - caravan.getX();
        double vecY = next.getY() - caravan.getY();
        double distance = Math.sqrt(Math.pow(next.getX() - caravan.getX(), 2) + Math.pow(next.getY() - caravan.getY(), 2));

        if(distance <= 70) {
            if(next.equals(caravan.getPoint1())) {
                City city = caravan.getPoint1();

                if(caravan.getPoint2StoreItem() != null) {
                    double excess = city.getStoredItem(caravan.getPoint2StoreItem().getId()) + caravan.getPoint2Store() - 100;

                    // Ausladen
                    if (excess > 0) {
                        city.setStoredItem(caravan.getPoint2StoreItem().getId(), 100);
                        caravan.setPoint2Store(excess);
                    } else {
                        city.setStoredItem(caravan.getPoint2StoreItem().getId(), city.getStoredItem(caravan.getPoint2StoreItem().getId()) + caravan.getPoint2Store());
                        caravan.setPoint2Store(0);
                        caravan.setPoint2StoreItem(null);
                    }
                }

                if(caravan.getPoint1StoreItem() != null) {
                    double oldLoad = caravan.getPoint1Store() + city.getStoredItem(caravan.getPoint1StoreItem().getId());

                    if(oldLoad > 100)
                        oldLoad = 100;

                    city.setStoredItem(caravan.getPoint1StoreItem().getId(), oldLoad);
                    caravan.setPoint1StoreItem(null);
                    caravan.setPoint1Store(0);
                }

                if(caravan.isTerminate()) {
                    caravanRepository.remove(caravan);
                    return;
                }

                // Einladen
                if(caravan.getPoint1Item() != null && Math.floor(city.getStoredItem(caravan.getPoint1Item().getId())) > 0) {
                    caravan.setPoint1StoreItem(caravan.getPoint1Item());

                    double amount = city.getStoredItem(caravan.getPoint1Item().getId());

                    if(amount - caravan.getPoint1Load() > 0)
                        amount = caravan.getPoint1Load();
                    else if(!caravan.getPoint1().getPlayer().equals(caravan.getPoint2().getPlayer())) {
                        eventRepository.add(new Event("Trade", caravan.getPoint1().getName() + " erfüllt den Handelsvertrag nicht!", "In der Stadt " + caravan.getPoint1().getName() + " wurde für die Karawane " + caravan.getName() + " statt der vereinbarten Menge an Ware (" + caravan.getPoint1Load() + "x " + caravan.getPoint1Item().getName() + ") nur " + new Double(amount).intValue() + "x " + caravan.getPoint1StoreItem().getName() + " eingeladen.", caravan.getPoint1(), caravan.getPoint2().getPlayer()));
                    }

                    city.setStoredItem(caravan.getPoint1Item().getId(), city.getStoredItem(caravan.getPoint1Item().getId()) - amount);
                    caravan.setPoint1Store(amount);
                }

                Road nextRoad = caravan.getRoute().getRoads().get(0);
                caravan.getRoute().setCurrentRoad(nextRoad);
                caravan.getRoute().setNext(nextRoad.getPoint1().equals(caravan.getPoint1()) ? nextRoad.getPoint2() : nextRoad.getPoint1());
                caravan.setDirection(2);
            } else if(next.equals(caravan.getPoint2())) {
                City city = caravan.getPoint2();

                if(caravan.getPoint1StoreItem() != null) {
                    double excess = city.getStoredItem(caravan.getPoint1StoreItem().getId()) + caravan.getPoint1Store() - 100;

                    // Ausladen
                    if (excess > 0) {
                        city.setStoredItem(caravan.getPoint1StoreItem().getId(), 100);
                        caravan.setPoint1Store(excess);
                    } else {
                        city.setStoredItem(caravan.getPoint1StoreItem().getId(), city.getStoredItem(caravan.getPoint1StoreItem().getId()) + caravan.getPoint1Store());
                        caravan.setPoint1Store(0);
                        caravan.setPoint1StoreItem(null);
                    }
                }

                if(caravan.getPoint2StoreItem() != null) {
                    double oldLoad = caravan.getPoint2Store() + city.getStoredItem(caravan.getPoint2StoreItem().getId());

                    if(oldLoad > 100)
                        oldLoad = 100;

                    city.setStoredItem(caravan.getPoint2StoreItem().getId(), oldLoad);
                    caravan.setPoint2StoreItem(null);
                    caravan.setPoint2Store(0);
                }

                if(caravan.isTerminate()) {
                    if(caravan.getPoint1().getPlayer().equals(caravan.getPoint2().getPlayer()))
                        caravanRepository.remove(caravan);
                    return;
                }

                // Einladen
                if(caravan.getPoint2Item() != null && Math.floor(city.getStoredItem(caravan.getPoint2Item().getId())) > 0) {
                    caravan.setPoint2StoreItem(caravan.getPoint2Item());

                    double amount = city.getStoredItem(caravan.getPoint2Item().getId());

                    if(amount - caravan.getPoint2Load() > 0)
                        amount = caravan.getPoint2Load();
                    else if(!caravan.getPoint1().getPlayer().equals(caravan.getPoint2().getPlayer())) {
                        eventRepository.add(new Event("Trade", caravan.getPoint2().getName() + " erfüllt den Handelsvertrag nicht!", "In der Stadt " + caravan.getPoint2().getName() + " wurde für die Karawane " + caravan.getName() + " statt der vereinbarten Menge an Ware (" + caravan.getPoint2Load() + "x " + caravan.getPoint2Item().getName() + ") nur " + new Double(amount).intValue() + "x " + caravan.getPoint2StoreItem().getName() + " eingeladen.", caravan.getPoint2(), caravan.getPoint1().getPlayer()));
                    }

                    city.setStoredItem(caravan.getPoint2Item().getId(), city.getStoredItem(caravan.getPoint2Item().getId()) - amount);
                    caravan.setPoint2Store(amount);
                }

                Road nextRoad = caravan.getRoute().getRoads().get(caravan.getRoute().getRoads().size() - 1);
                caravan.getRoute().setCurrentRoad(nextRoad);
                caravan.getRoute().setNext(nextRoad.getPoint2().equals(caravan.getPoint2()) ? nextRoad.getPoint1() : nextRoad.getPoint2());
                caravan.setDirection(1);
            } else {
                Road nextRoad = null;
                if(caravan.getDirection() == 1)
                    nextRoad = caravan.getRoute().getRoads().get(caravan.getRoute().getRoads().indexOf(caravan.getRoute().getCurrentRoad()) - 1);
                else
                    nextRoad = caravan.getRoute().getRoads().get(caravan.getRoute().getRoads().indexOf(caravan.getRoute().getCurrentRoad()) + 1);

                caravan.setX(next.getX());
                caravan.setY(next.getY());

                caravan.getRoute().setCurrentRoad(nextRoad);
                caravan.getRoute().setNext(nextRoad.getPoint1().equals(next) ? nextRoad.getPoint2() : nextRoad.getPoint1());

                caravan.move(70);
            }
        } else {
            double multiplier = 0.1 / distance;

            caravan.setX(caravan.getX() + (vecX * multiplier * caravan.getRoute().getCurrentRoad().getWeight()));
            caravan.setY(caravan.getY() + (vecY * multiplier * caravan.getRoute().getCurrentRoad().getWeight()));
        }
    }

    public void processMaintenance(Caravan caravan) {
        caravan.getPlayer().addCoins(-1 / 3600);
    }
}
