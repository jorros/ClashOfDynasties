package com.dynastiesofscorpia.logic;

import com.dynastiesofscorpia.models.*;
import com.dynastiesofscorpia.repository.CaravanRepository;
import com.dynastiesofscorpia.repository.EventRepository;
import com.dynastiesofscorpia.repository.RelationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CaravanLogic {
    @Autowired
    private CaravanRepository caravanRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private RelationRepository relationRepository;

    public void processMovement(Caravan caravan, double delta) {
        City next = caravan.getRoute().getNext();

        double vecX = next.getX() - caravan.getX();
        double vecY = next.getY() - caravan.getY();
        double distance = Math.sqrt(Math.pow(next.getX() - caravan.getX(), 2) + Math.pow(next.getY() - caravan.getY(), 2));

        if(distance <= 70) {
            if(next.equals(caravan.getPoint1())) {
                City city = caravan.getPoint1();

                int rel = 1;
                Relation relation = null;

                if(city.getPlayer().equals(caravan.getPlayer()))
                    rel = 4;
                else {
                    relation = relationRepository.findByPlayers(city.getPlayer(), caravan.getPlayer());

                    if(relation != null)
                        rel = relation.getRelation();
                }

                if(caravan.getPoint2StoreItem() != null && rel != 1) {
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

                if(caravan.isTerminate() || rel == 0) {
                    if(relation != null) {
                        if(relation.getCaravans().contains(caravan)) {
                            relation.removeCaravan(caravan);
                        }
                    }
                }

                if(caravan.isTerminate()) {
                    caravanRepository.remove(caravan);
                    return;
                } else if(rel == 0) {
                    caravanRepository.remove(caravan);
                    return;
                }

                // Einladen
                if(caravan.getPoint1Item() != null && Math.floor(city.getStoredItem(caravan.getPoint1Item().getId())) > 0 && rel >= 2) {
                    boolean otherItem = true;

                    if(caravan.getPoint1StoreItem() != null && caravan.getPoint1Item() != null && caravan.getPoint1Item().equals(caravan.getPoint1StoreItem()))
                        otherItem = false;
                    caravan.setPoint1StoreItem(caravan.getPoint1Item());

                    double total = city.getStoredItem(caravan.getPoint1Item().getId());

                    if(!otherItem)
                        total += caravan.getPoint1Store();

                    double amount = total;

                    if(amount - caravan.getPoint1Load() > 0) {
                        amount = caravan.getPoint1Load();
                    }
                    else if(!caravan.getPoint1().getPlayer().equals(caravan.getPoint2().getPlayer())) {
                        eventRepository.add(new Event("TradeNotEnoughLoaded", caravan.getPoint1().getPlayer().getName() + " erfüllt den Handelsvertrag nicht!", "In der Stadt " + caravan.getPoint1().getName() + " wurde für die Karawane " + caravan.getName() + " statt der vereinbarten Menge an Ware (" + caravan.getPoint1Load() + "t " + caravan.getPoint1Item().getName() + ") nur " + new Double(amount).intValue() + "t " + caravan.getPoint1StoreItem().getName() + " eingeladen.", caravan.getPoint1(), caravan.getPoint2().getPlayer()));
                    }

                    city.setStoredItem(caravan.getPoint1Item().getId(), total - amount);
                    caravan.setPoint1Store(amount);
                }

                Road nextRoad = caravan.getRoute().getRoads().get(0);
                caravan.getRoute().setCurrentRoad(nextRoad);
                caravan.getRoute().setNext(nextRoad.getPoint1().equals(caravan.getPoint1()) ? nextRoad.getPoint2() : nextRoad.getPoint1());
                caravan.setDirection(2);
            } else if(next.equals(caravan.getPoint2())) {
                City city = caravan.getPoint2();

                int rel = 1;

                if(city.getPlayer().equals(caravan.getPlayer()))
                    rel = 4;
                else {
                    Relation relation = relationRepository.findByPlayers(city.getPlayer(), caravan.getPlayer());

                    if(relation != null)
                        rel = relation.getRelation();
                }

                if(caravan.getPoint1StoreItem() != null && rel != 1) {
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

                if(city.getPlayer().equals(caravan.getPlayer())) {
                    if(caravan.isTerminate()) {
                        caravanRepository.remove(caravan);
                        return;
                    }
                } else if(rel == 0) {
                    caravanRepository.remove(caravan);
                    return;
                }

                // Einladen
                if(caravan.getPoint2Item() != null && Math.floor(city.getStoredItem(caravan.getPoint2Item().getId())) > 0 && rel >= 2) {
                    boolean otherItem = true;

                    if(caravan.getPoint2StoreItem() != null && caravan.getPoint2Item() != null && caravan.getPoint2Item().equals(caravan.getPoint2StoreItem()))
                        otherItem = false;
                    caravan.setPoint2StoreItem(caravan.getPoint2Item());

                    double total = city.getStoredItem(caravan.getPoint2Item().getId());

                    if(!otherItem)
                        total += caravan.getPoint2Store();

                    double amount = total;

                    if(amount - caravan.getPoint2Load() > 0) {
                        amount = caravan.getPoint2Load();
                    }
                    else if(!caravan.getPoint1().getPlayer().equals(caravan.getPoint2().getPlayer())) {
                        eventRepository.add(new Event("TradeNotEnoughLoaded", caravan.getPoint2().getPlayer().getName() + " erfüllt den Handelsvertrag nicht!", "In der Stadt " + caravan.getPoint2().getName() + " wurde für die Karawane " + caravan.getName() + " statt der vereinbarten Menge an Ware (" + caravan.getPoint2Load() + "t " + caravan.getPoint2Item().getName() + ") nur " + new Double(amount).intValue() + "t " + caravan.getPoint2StoreItem().getName() + " eingeladen.", caravan.getPoint2(), caravan.getPoint1().getPlayer()));
                    }

                    city.setStoredItem(caravan.getPoint2Item().getId(), total - amount);
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
            double multiplier = 0.1 / distance * delta;

            caravan.setX(caravan.getX() + (vecX * multiplier * caravan.getRoute().getCurrentRoad().getWeight()));
            caravan.setY(caravan.getY() + (vecY * multiplier * caravan.getRoute().getCurrentRoad().getWeight()));
        }
    }

    public void processMaintenance(Caravan caravan, double delta) {
        caravan.getPlayer().addCoins(-((double)caravan.getCost() / 3600d) * delta);
    }
}
