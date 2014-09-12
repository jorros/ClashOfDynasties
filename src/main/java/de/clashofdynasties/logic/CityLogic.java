package de.clashofdynasties.logic;

import de.clashofdynasties.models.*;
import de.clashofdynasties.repository.*;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CityLogic {
    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private CityTypeRepository cityTypeRepository;

    @Autowired
    private FormationRepository formationRepository;

    @Autowired
    private RelationRepository relationRepository;

    @Autowired
    private PlayerLogic playerLogic;

    private double getSatisfactionModifier(int itemType, int cityType) {
        switch(cityType) {
            case 1:
                if(itemType == 1)
                    return 2.0/3;
                else if(itemType == 2)
                    return 1.0/3;
                else
                    return 0;

            case 2:
                if(itemType == 1)
                    return 3.0/6;
                else if(itemType == 2)
                    return 2.0/6;
                else if(itemType == 3)
                    return 1.0/6;
                else
                    return 0;

            case 3:
                if(itemType == 1)
                    return 10.0/27;
                else if(itemType == 2)
                    return 7.0/27;
                else if(itemType == 3)
                    return 6.0/27;
                else
                    return 4.0/27;

        }

        return 0;
    }

    public void processPopulation(City city) {
        if(city.getPlayer().isComputer() || city.getType().getId() == 4)
            city.setSatisfaction(100);
        else {
            CityType cityType = city.getType();
            Map<Integer, Double> store = city.getItems();
            List<ItemType> requiredItems = city.getRequiredItemTypes();

            double maxSatisfaction = 0;
            double firstLevelSatisfaction = 0;
            double secondLevelSatisfaction = 0;

            for (ItemType itemType : requiredItems) {
                boolean provided = false;

                double consumRate = cityType.getConsumeBasic();

                switch (itemType.getType()) {
                    case 1:
                        consumRate = cityType.getConsumeBasic();
                        break;

                    case 2:
                        consumRate = cityType.getConsumeLuxury1();
                        break;

                    case 3:
                        consumRate = cityType.getConsumeLuxury2();
                        break;

                    case 4:
                        consumRate = cityType.getConsumeLuxury3();
                        break;
                }

                List<Item> items = itemRepository.findByType(itemType);
                for (Item item : items) {
                    if (store.containsKey(item.getId()) && (city.getStopConsumption() == null || !city.getStopConsumption().contains(item))) {
                        double amount = store.get(item.getId());
                        double satisfied = 1;

                        amount -= city.getPopulation() * consumRate;

                        if (amount < 0) {
                            amount /= city.getPopulation() * consumRate;
                            satisfied -= amount;
                            amount = 0;
                        }

                        store.put(item.getId(), amount);
                        satisfied *= getSatisfactionModifier(itemType.getId(), cityType.getId());
                        firstLevelSatisfaction += satisfied;

                        provided = true;
                        break;
                    }
                }
            }

            if (city.getBuildings().size() <= city.getCapacity()) {
                if (city.getBuildings().stream().filter(b -> b.getBlueprint().getId() == 4).count() > 0)
                    secondLevelSatisfaction += (5.0 / 14);

                if (city.getBuildings().stream().filter(b -> b.getBlueprint().getId() == 6).count() > 0)
                    secondLevelSatisfaction += 4.0 / 14;

                if (city.getBuildings().stream().filter(b -> b.getBlueprint().getId() == 11).count() > 0)
                    secondLevelSatisfaction += 3.0 / 14;

                if (city.getBuildings().stream().filter(b -> b.getBlueprint().getId() == 13).count() > 0)
                    secondLevelSatisfaction += 2.0 / 14;

                if (city.getBuildings().stream().filter(b -> b.getBlueprint().getId() == 14).count() > 0)
                    secondLevelSatisfaction += 2.0 / 14;

                if (secondLevelSatisfaction > 1)
                    secondLevelSatisfaction = 1;
            }

            maxSatisfaction = (firstLevelSatisfaction * 2.0 / 3 + secondLevelSatisfaction * 1.0 / 3) * 100;

            double computedSatisfaction;
            if (maxSatisfaction > city.getRawSatisfaction())
                computedSatisfaction = city.getRawSatisfaction() + 1.0 / 360;
            else if (maxSatisfaction < city.getRawSatisfaction())
                computedSatisfaction = city.getRawSatisfaction() - 1.0 / 360;
            else
                computedSatisfaction = city.getRawSatisfaction();

            if (computedSatisfaction > 100)
                computedSatisfaction = 100;
            else if (computedSatisfaction < 0)
                computedSatisfaction = 0;

            city.setSatisfaction(computedSatisfaction);

            long maxPeople = (city.getBuildings().stream().filter(b -> b.getBlueprint().getId() == 1).count() * 10) + 5;

            if (city.getSatisfaction() >= 60 && Math.random() < 0.01 && city.getPopulation() < maxPeople) {
                city.setPopulation(city.getPopulation() + 1);
            } else if (((city.getSatisfaction() < 30 && Math.random() < 0.02) || maxPeople < city.getPopulation()) && city.getPopulation() > 0) {
                city.setPopulation(city.getPopulation() - 1);
            }
        }

        if(city.getPopulation() < 5)
            city.setPopulation(5);
    }

    public void processProduction(City city) {
        List<Building> productionBuildings = city.getBuildings().stream().filter(b -> b.getBlueprint().getProduceItem() != null).collect(Collectors.toList());

        for(Building b : productionBuildings) {
            double sum = 0;

            if(city.getItems().containsKey(b.getBlueprint().getProduceItem().getId()))
                sum = city.getItems().get(b.getBlueprint().getProduceItem().getId());

            sum += b.getBlueprint().getProducePerStep();

            if(sum > 100)
                sum = 100;

            city.getItems().put(b.getBlueprint().getProduceItem().getId(), sum);
        }
    }

    public void processCoins(City city) {
        Player player = playerRepository.findOne(city.getPlayer().getId());
        player.addCoins(city.calculateCoins() - city.calculateMaintenance());
        playerRepository.save(player);
    }

    public void processConstruction(City city) {
        BuildingConstruction construction = city.getBuildingConstruction();
        if (construction != null) {
            if (city.getCapacity() < city.getBuildings().size())
                city.setBuildingConstruction(null);
            else {
                double production = 0;

                if (city.getReport() == null) {
                    production = city.getProductionRate();
                }

                construction.addProduction(production);

                city.setBuildingConstruction(construction);

                if (construction.getProduction() >= construction.getRequiredProduction()) {
                    if (construction.getBlueprint() instanceof BuildingBlueprint) {
                        Building building = new Building();
                        building.setBlueprint((BuildingBlueprint) construction.getBlueprint());
                        building.setHealth(100);
                        buildingRepository.save(building);

                        city.getBuildings().add(building);

                        eventRepository.save(new Event("ProductionReady", "Neues Gebäude errichtet", "In " + city.getName() + " wurde das Gebäude " + building.getBlueprint().getName() + " errichtet!", city, city.getPlayer()));
                    } else if (construction.getBlueprint() instanceof UnitBlueprint) {
                        for(int i = 0; i < construction.getCount(); i++) {
                            Unit unit = new Unit();
                            unit.setBlueprint((UnitBlueprint) construction.getBlueprint());
                            unit.setHealth(100);
                            unitRepository.save(unit);

                            city.getUnits().add(unit);
                        }

                        if(construction.getCount() > 1)
                            eventRepository.save(new Event("ProductionReady", "Neue Einheiten ausgebildet", "In " + city.getName() + " wurden " + construction.getCount() + " Einheiten " + construction.getBlueprint().getName() + " ausgebildet!", city, city.getPlayer()));
                        else
                            eventRepository.save(new Event("ProductionReady", "Neue Einheit ausgebildet", "In " + city.getName() + " wurde die Einheit " + construction.getBlueprint().getName() + " ausgebildet!", city, city.getPlayer()));
                    }

                    city.setBuildingConstruction(null);
                }
            }
        }
    }

    public void processType(City city) {
        if(city.getType().getId() != 4) {
            if(city.getPopulation() < 50)
                city.setType(cityTypeRepository.findOne(1));
            else if(city.getPopulation() < 200)
                city.setType(cityTypeRepository.findOne(2));
            else
                city.setType(cityTypeRepository.findOne(3));
        }
    }

    public void processHealing(City city) {
        if(city.getReport() == null) {
            for (Unit unit : city.getUnits()) {
                if(Math.random() < 0.01 && unit.getHealth() < 100) {
                    unit.setHealth(unit.getHealth() + 1);
                }

                unitRepository.save(unit);
            }

            for(Building building : city.getBuildings()) {
                if(Math.random() < 0.01 && building.getHealth() < 100) {
                    building.setHealth(building.getHealth() + 1);
                }

                buildingRepository.save(building);
            }

            if(city.getHealth() < 100) {
                if(Math.random() < 0.01) {
                    city.setHealth(city.getHealth() + 1);
                }
            }
        }
    }

    private boolean isCounter(UnitBlueprint first, UnitBlueprint second) {
        if(first.getType() == 1 && second.getType() == 3 || first.getType() == 3 && second.getType() == 1)
            return true;

        if(first.getType() == 2 && second.getType() == 1 || first.getType() == 1 && second.getType() == 2)
            return true;

        if(first.getType() == 3 && second.getType() == 2 || first.getType() == 2 && second.getType() == 3)
            return true;

        return false;
    }

    private List<Pair<Unit, Double>> getUnitProbabilities(List<Unit> units, Unit from) {
        ArrayList<Pair<Unit, Double>> probabilites = new ArrayList<>();

        for(Unit unit : units) {
            double probability = 0.1;

            if(isCounter(unit.getBlueprint(), from.getBlueprint()))
                probability += 0.2;

            if(unit.getHealth() < 100)
                probability += 0.3;

            probabilites.add(new Pair<>(unit, probability));
        }

        return probabilites;
    }

    private List<Pair<Building, Double>> getBuildingProbabilities(List<Building> buildings) {
        ArrayList<Pair<Building, Double>> probabilites = new ArrayList<>();

        for(Building building : buildings) {
            double probability = 0.1;

            if(building.getHealth() < 100)
                probability += 0.3;

            if(building.getBlueprint().getDefencePoints() > 0)
                probability += 0.3;

            probabilites.add(new Pair<>(building, probability));
        }

        return probabilites;
    }

    public void processWar(City city) {
        List<Formation> formations = formationRepository.findByCity(city.getId());

        if(!formations.isEmpty()) {
            boolean isWar = false;
            for(Formation formation : formations) {
                Relation relation = relationRepository.findByPlayers(city.getPlayer().getId(), formation.getPlayer().getId());

                if((relation != null && relation.getRelation() == 0) || city.getPlayer().isComputer()) {
                    isWar = true;
                    break;
                }
            }

            if(city.getReport() == null && isWar) {
                city.setReport(new Report());
            } else if(!isWar)
                city.setReport(null);
        }

        if(city.getReport() != null) {
            if(city.getReport().getParties() == null)
                city.getReport().setParties(new ArrayList<>());

            Report report = city.getReport();

            List<Player> players = formations.stream().map(f -> f.getPlayer()).distinct().collect(Collectors.toList());

            if(!players.contains(city.getPlayer()))
                players.add(city.getPlayer());

            report.getParties().forEach(p -> p.setLost(true));
            for(Player player : players) {
                if(report.getParties().stream().filter(p -> p.getPlayer().equals(player)).count() == 0) {
                    Party party = new Party();
                    party.setPlayer(player);
                    party.setLost(false);
                    report.getParties().add(party);
                } else {
                    report.getParties().stream().filter(p -> p.getPlayer().equals(player)).findFirst().get().setLost(false);
                }
            }

            for(Player player : players) {
                List<Formation> playerFormations = formations.stream().filter(f -> f.getPlayer().equals(player)).collect(Collectors.toList());
                List<Formation> enemyFormations = new ArrayList<>();
                List<Unit> enemyUnits = new ArrayList<>();
                List<Building> enemyBuildings = city.getBuildings();

                for(Formation formation : formations) {
                    if(!formation.getPlayer().equals(player)) {
                        Relation relation = relationRepository.findByPlayers(player.getId(), formation.getPlayer().getId());

                        if(relation == null || relation.getRelation() <= 2) {
                            enemyFormations.add(formation);

                            if(formation.getUnits() != null)
                                enemyUnits.addAll(formation.getUnits());
                        }
                    }
                }

                if(enemyUnits.size() > 0 || !city.getPlayer().equals(player)) {
                    for (Formation formation : playerFormations) {
                        for (Unit unit : formation.getUnits()) {
                            if(enemyUnits.size() > 0) {
                                Unit selected = new EnumeratedDistribution<>(getUnitProbabilities(enemyUnits, unit)).sample();

                                int newHealth = selected.getHealth();

                                int counter = isCounter(selected.getBlueprint(), unit.getBlueprint()) ? unit.getBlueprint().getStrength() : 0;

                                newHealth -= unit.getBlueprint().getStrength() + counter;

                                selected.setHealth(newHealth);
                            } else if(enemyBuildings.stream().filter(b -> b.getBlueprint().getDefencePoints() > 0).count() > 0) {
                                Building selected = new EnumeratedDistribution<>(getBuildingProbabilities(enemyBuildings)).sample();

                                int newHealth = selected.getHealth();

                                if(unit.getBlueprint().getType() == 4)
                                    newHealth -= unit.getBlueprint().getStrength();
                                else
                                    newHealth -= unit.getBlueprint().getStrength() / 2;
                            } else {
                                if(unit.getBlueprint().getType() == 4)
                                    city.setHealth(city.getHealth() - unit.getBlueprint().getStrength());
                                else
                                    city.setHealth(city.getHealth() - unit.getBlueprint().getStrength() / 2);
                            }
                        }
                    }
                }
            }

            for(Formation formation : formations) {
                Party party = report.getParties().stream().filter(p -> p.getPlayer().equals(formation.getPlayer())).findFirst().get();

                for(Unit unit : formation.getUnits()) {
                    if(unit.getHealth() <= 0) {
                        formation.getUnits().remove(unit);
                        party.setLosses(party.getLosses() + 1);
                        unitRepository.delete(unit);
                    }
                }

                unitRepository.save(formation.getUnits());
            }

            for(Building building : city.getBuildings()) {
                if(building.getHealth() <= 0) {
                    city.getBuildings().remove(building);
                    buildingRepository.delete(building);
                }

                buildingRepository.save(city.getBuildings());
            }

            if(city.getHealth() <= 0) {
                if(!city.getPlayer().isComputer()) {
                    city.getPlayer().setSightUpdate(true);
                    playerRepository.save(city.getPlayer());
                }
                if(formations.size() == 0 || formations.stream().map(f -> f.getPlayer()).distinct().count() > 1)
                    city.setPlayer(playerRepository.findByName("Freies Volk"));
                else {
                    city.setPlayer(formations.get(0).getPlayer());
                    city.setReport(null);
                    city.getPlayer().setSightUpdate(true);
                    playerRepository.save(city.getPlayer());
                }
            }

            formationRepository.save(formations);
        }
    }
}
