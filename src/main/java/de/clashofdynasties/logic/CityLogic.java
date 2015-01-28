package de.clashofdynasties.logic;

import de.clashofdynasties.models.*;
import de.clashofdynasties.repository.*;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Component
public class CityLogic {
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

    private double calculateSatisfaction(int cityType, double base, double level1, double level2, double level3, double general) {
        double satisfaction = 0;
        switch(cityType) {
            case 1:
                satisfaction = 1/3f * general + 2/3f * level1;
                break;

            case 2:
                satisfaction = 2/5f * general + 2/5f * level1 + 1/5f * level2;
                break;

            case 3:
                satisfaction = 4/10f * general + 2/10f * level1 + 2/10f * level2 + 2/10f * level3;
                break;
        }

        return satisfaction * base;
    }

    public void processPopulation(City city, double delta) {
        if(city.getPlayer().isComputer() || city.getType().getId() == 4)
            city.setSatisfaction(100);
        else {
            CityType cityType = city.getType();
            Map<Integer, Double> store = city.getItems();
            List<ItemType> requiredItems = city.getRequiredItemTypes();

            double maxSatisfaction = 0;
            double generalSatisfaction = 0;
            double level1Satisfaction = 0;
            double level2Satisfaction = 0;
            double level3Satisfaction = 0;
            double baseSatisfaction = 0;

            for (ItemType itemType : requiredItems) {
                double consumRate = cityType.getConsumeLuxury1();
                double satisfied = 1;

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
                    if (store.containsKey(item.getId()) && store.get(item.getId()) > 0 && !city.getStopConsumption().contains(item) && satisfied > 0) {
                        double amount = store.get(item.getId());

                        amount -= city.getPopulation() * consumRate * satisfied;

                        if (amount < 0) {
                            amount /= city.getPopulation() * consumRate * satisfied;
                            satisfied -= amount;
                            amount = 0;
                        } else
                            satisfied = 0;

                        store.put(item.getId(), amount);
                    }
                }

                if(itemType.getType() == 1)
                    baseSatisfaction = 1 - satisfied;
                if(itemType.getType() == 2)
                    level1Satisfaction += 1 - satisfied;
                else if(itemType.getType() == 3)
                    level2Satisfaction += 1 - satisfied;
                else if(itemType.getType() == 4)
                    level3Satisfaction = 1 - satisfied;
            }

            if(baseSatisfaction < 0)
                baseSatisfaction = 0;

            if(level1Satisfaction < 0)
                level1Satisfaction = 0;

            if(level2Satisfaction < 0)
                level2Satisfaction = 0;

            if(level3Satisfaction < 0)
                level3Satisfaction = 0;

            if (city.getBuildings().stream().filter(b -> b.getBlueprint().getId() == 4).count() > 0) {
                if(city.getType().getId() == 1)
                    generalSatisfaction = 1.0;
                else
                    generalSatisfaction += (5.0 / 14);
            }

            if (city.getBuildings().stream().filter(b -> b.getBlueprint().getId() == 6).count() > 0)
                generalSatisfaction += 4.0 / 14;

            if (city.getBuildings().stream().filter(b -> b.getBlueprint().getId() == 11).count() > 0)
                generalSatisfaction += 3.0 / 14;

            if (city.getBuildings().stream().filter(b -> b.getBlueprint().getId() == 13).count() > 0)
                generalSatisfaction += 2.0 / 14;

            if (city.getBuildings().stream().filter(b -> b.getBlueprint().getId() == 14).count() > 0)
                generalSatisfaction += 2.0 / 14;

            if (generalSatisfaction > 1)
                generalSatisfaction = 1;

            maxSatisfaction = calculateSatisfaction(city.getType().getId(), baseSatisfaction, level1Satisfaction, level2Satisfaction, level3Satisfaction, generalSatisfaction) * 100;

            city.setCalculatedSatisfaction(maxSatisfaction);

            double computedSatisfaction;
            if (maxSatisfaction > city.getRawSatisfaction() && !city.isPlague())
                computedSatisfaction = (city.getRawSatisfaction() + (1.0 / 120 * delta));
            else if (maxSatisfaction < city.getRawSatisfaction() || city.isPlague())
                computedSatisfaction = (city.getRawSatisfaction() - (1.0 / 360 * delta));
            else
                computedSatisfaction = city.getRawSatisfaction();

            if (computedSatisfaction > 100)
                computedSatisfaction = 100;
            else if (computedSatisfaction < 0)
                computedSatisfaction = 0;

            city.setSatisfaction(computedSatisfaction);

            long maxPeople = (city.getBuildings().stream().filter(b -> b.getBlueprint().getId() == 1).count() * 10) + 10;

            if (city.getSatisfaction() >= 80 && Math.random() < 0.01 && city.getPopulation() < maxPeople) {
                city.setPopulation(city.getPopulation() + 1);

                for(Objective objective : city.getPlayer().getObjectives()) {
                    if(objective.getCity() != null && objective.getCity().equals(city) && objective.getPopulation() != null && city.getPopulation() >= objective.getPopulation())
                        objective.setCompleted(true);
                }
            } else if (((city.getSatisfaction() < 30 && Math.random() < 0.02) || maxPeople < city.getPopulation()) && city.getPopulation() > 10) {
                city.setPopulation(city.getPopulation() - 1);
            }
        }

        if(city.getPopulation() < 10)
            city.setPopulation(10);
    }

    public void processEvents(City city) {
        double infectionChance;
        double fireChance;

        if(city.getType().getId() < 4) {
            if (city.isPlague()) {
                if (city.getBuildings().stream().filter(b -> b.getBlueprint().getId() == 10).count() > 0)
                    infectionChance = 0.001;
                else
                    infectionChance = 0.000002;

                if (Math.random() < infectionChance)
                    city.setPlague(false);
            }

            if (city.isFire()) {
                if(city.getBuildings().isEmpty())
                    city.setFire(false);

                if (Math.random() < 0.0002) {
                    Building selected = new EnumeratedDistribution<>(getBuildingProbabilities(city.getBuildings(), false)).sample();

                    selected.setHealth(selected.getHealth() - 30);
                    if (selected.getHealth() < 0) {
                        city.removeBuilding(selected);

                        if(selected.getBlueprint().getDefencePoints() > 0)
                            city.recalculateStrength();

                        buildingRepository.remove(selected);
                    }
                }

                if (city.getBuildings().stream().filter(b -> b.getBlueprint().getId() == 2).count() > 0)
                    fireChance = 0.001;
                else
                    fireChance = 0.000002;

                if (Math.random() < fireChance)
                    city.setFire(false);
            }

            if(System.currentTimeMillis() - city.getPlayer().getRegistration() >= 1209600000) {
                if (city.getBuildings().stream().filter(b -> b.getBlueprint().getId() == 10).count() > 0)
                    infectionChance = 0.0000001;
                else
                    infectionChance = 0.0000005;

                if (!city.isPlague() && Math.random() < infectionChance && city.getPopulation() > 10) {
                    city.setPlague(true);
                    eventRepository.add(new Event("Disease", "Seuche in " + city.getName() + " ausgebrochen", "Es ist eine tödliche Seuche in " + city.getName() + " ausgebrochen. Errichte eine medizinische Einrichtung, um die Epidemie einzudämmen.", city, city.getPlayer()));
                }

                if (city.getBuildings().stream().filter(b -> b.getBlueprint().getId() == 2).count() > 0)
                    fireChance = 0.0000001;
                else
                    fireChance = 0.0000005;

                if (!city.isFire() && Math.random() < fireChance && city.getBuildings().size() > 0) {
                    city.setFire(true);
                    eventRepository.add(new Event("Fire", "Großbrand in " + city.getName(), "In " + city.getName() + " ist ein Großbrand ausgebrochen. Je länger dieser wütet, desto mehr Gebäude fallen ihm zum Opfer. Errichte eine Feuerwehr, um das Feuer zu stoppen.", city, city.getPlayer()));
                }
            }
        }
    }

    public void processProduction(City city, double delta) {
        List<Building> productionBuildings = city.getBuildings().stream().filter(b -> b.getBlueprint().getProduceItem() != null).collect(Collectors.toList());

        for(Building b : productionBuildings) {
            double sum = 0;

            if(city.getItems().containsKey(b.getBlueprint().getProduceItem().getId()))
                sum = city.getItems().get(b.getBlueprint().getProduceItem().getId());

            sum += b.getBlueprint().getProducePerStep() * delta;

            if(sum > 100)
                sum = 100;

            city.getItems().put(b.getBlueprint().getProduceItem().getId(), sum);
        }
    }

    public void processCoins(City city, double delta) {
        Player player = playerRepository.findById(city.getPlayer().getId());
        player.addCoins((city.calculateCoins() - city.calculateMaintenance()) * delta);
    }

    public void processConstruction(City city, double delta) {
        BuildingConstruction construction = city.getBuildingConstruction();
        if (construction != null) {
            if (city.getCapacity() < city.getBuildings().size() && construction.getBlueprint() instanceof BuildingBlueprint)
                city.setBuildingConstruction(null);
            else {
                double production = 0;

                if (city.getReport() == null) {
                    production = city.getProductionRate();
                }

                construction.addProduction(production * delta);

                city.setBuildingConstruction(construction);

                if (construction.getProduction() >= construction.getRequiredProduction()) {
                    if (construction.getBlueprint() instanceof BuildingBlueprint) {
                        Building building = new Building();
                        building.setBlueprint((BuildingBlueprint) construction.getBlueprint());
                        building.setHealth(100);
                        buildingRepository.add(building);

                        for(Objective objective : city.getPlayer().getObjectives()) {
                            if(objective.getCity() != null && objective.getCity().equals(city)) {
                                if(objective.getBuilding() != null && objective.getBuilding().equals(construction.getBlueprint()))
                                    objective.setCountReady(objective.getCountReady() + 1);

                                if(objective.getCountReady() >= objective.getCount())
                                    objective.setCompleted(true);
                            }
                        }

                        city.addBuilding(building);

                        if(building.getBlueprint().getDefencePoints() > 0)
                            city.recalculateStrength();

                        eventRepository.add(new Event("ProductionReady", "Neues Gebäude errichtet", "In " + city.getName() + " wurde das Gebäude " + building.getBlueprint().getName() + " errichtet!", city, city.getPlayer()));
                    } else if (construction.getBlueprint() instanceof UnitBlueprint) {
                        for(int i = 0; i < construction.getCount(); i++) {
                            Unit unit = new Unit();
                            unit.setBlueprint((UnitBlueprint) construction.getBlueprint());
                            unit.setHealth(100);
                            unitRepository.add(unit);

                            city.addUnit(unit);
                        }

                        if(construction.getCount() > 1)
                            eventRepository.add(new Event("ProductionReady", "Neue Einheiten ausgebildet", "In " + city.getName() + " wurden " + construction.getCount() + " Einheiten " + construction.getBlueprint().getName() + " ausgebildet!", city, city.getPlayer()));
                        else
                            eventRepository.add(new Event("ProductionReady", "Neue Einheit ausgebildet", "In " + city.getName() + " wurde die Einheit " + construction.getBlueprint().getName() + " ausgebildet!", city, city.getPlayer()));

                        city.recalculateStrength();
                    }

                    city.setBuildingConstruction(null);
                }
            }
        }
    }

    public void processType(City city) {
        if(city.getType().getId() != 4) {
            if(city.getPopulation() < 50 && city.getType().getId() != 1) {
                city.setType(cityTypeRepository.findById(1));
                city.getPlayer().setSightUpdate(true);
                eventRepository.add(new Event("CityUpgrade", city.getName() + " ist jetzt ein Dorf", "Deine Stadt hat zu wenig Bewohner und ist ab sofort ein Dorf. Sollte das Baulimit überschritten sein, dann verdoppeln sich die laufenden Kosten, bis genügend Gebäude abgerissen wurden.", city, city.getPlayer()));
            }
            else if(city.getPopulation() >= 50 && city.getPopulation() < 200 && city.getType().getId() != 2) {
                city.setType(cityTypeRepository.findById(2));
                city.getPlayer().setSightUpdate(true);
                eventRepository.add(new Event("CityUpgrade", city.getName() + " ist jetzt eine Stadt", "Bei einer Bürgerzahl von 50 bis max 200 handelt es sich um eine Stadt. Sollte das Baulimit überschritten sein, dann verdoppeln sich die laufenden Kosten, bis genügend Gebäude abgerissen wurden.", city, city.getPlayer()));
            }
            else if(city.getPopulation() >= 200 && city.getType().getId() != 3) {
                city.setType(cityTypeRepository.findById(3));
                city.getPlayer().setSightUpdate(true);
                eventRepository.add(new Event("CityUpgrade", city.getName() + " ist jetzt eine Großstadt", "Deine Stadt hat über 200 Bürger und gilt somit als Großstadt. Großstadt ist die höchste Stadtstufe.", city, city.getPlayer()));
            }
        }
    }

    public void processHealing(City city) {
        if(city.getReport() == null) {
            if(city.getBuildings().stream().filter(b -> b.getBlueprint().getId() == 10).count() > 0 || city.getUnits().stream().filter(u -> u.getBlueprint().getId() == 4).count() > 0) {
                List<Unit> units = city.getUnits().stream().filter(u -> u.getHealth() < 100).collect(Collectors.toList());

                for (Unit unit : units) {
                    if (Math.random() < 0.01) {
                        unit.setHealth(unit.getHealth() + 1);
                    }
                }
            }

            if(!city.isFire()) {
                List<Building> buildings = city.getBuildings().stream().filter(b -> b.getHealth() < 100).collect(Collectors.toList());

                for (Building building : buildings) {
                    if (Math.random() < 0.01) {
                        building.setHealth(building.getHealth() + 1);
                    }
                }
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
                probability += 0.7;

            probabilites.add(new Pair<>(unit, probability));
        }

        return probabilites;
    }

    private List<Pair<Building, Double>> getBuildingProbabilities(List<Building> buildings, boolean targetDefence) {
        ArrayList<Pair<Building, Double>> probabilites = new ArrayList<>();

        for(Building building : buildings) {
            double probability = 0.1;

            if(building.getHealth() < 100)
                probability += 0.5;

            if(building.getBlueprint().getDefencePoints() > 0 && targetDefence)
                probability += 0.3;

            probabilites.add(new Pair<>(building, probability));
        }

        return probabilites;
    }

    private List<Pair<Unit, Double>> getAttackerProbabilities(List<Unit> playerUnits, List<Unit> enemyUnits) {
        ArrayList<Pair<Unit, Double>> probabilites = new ArrayList<>();

        long infantry = enemyUnits.stream().filter(u -> u.getBlueprint().getType() == 1).count();
        long shooter = enemyUnits.stream().filter(u -> u.getBlueprint().getType() == 2).count();
        long rider = enemyUnits.stream().filter(u -> u.getBlueprint().getType() == 3).count();
        long siege = enemyUnits.stream().filter(u -> u.getBlueprint().getType() == 4).count();

        for(Unit unit : playerUnits) {
            double probability = 0.1;

            switch(unit.getBlueprint().getType()) {
                case 1:
                    if(Math.max(infantry, Math.max(shooter, Math.max(rider, siege))) == rider)
                        probability += 0.3;
                    break;

                case 2:
                    if(Math.max(infantry, Math.max(shooter, Math.max(rider, siege))) == infantry)
                        probability += 0.3;
                    break;

                case 3:
                    if(Math.max(infantry, Math.max(shooter, Math.max(rider, siege))) == shooter)
                        probability += 0.3;
                    break;

                case 4:
                    if(infantry + shooter + rider + siege < 50)
                        probability += 0.3;
                    if(infantry + shooter + rider + siege == 0)
                        probability += 0.9;
                    break;
            }

            probabilites.add(new Pair<>(unit, probability));
        }

        return probabilites;
    }

    public void processWar(City city) {
        Random random = new Random();
        List<Formation> formations = formationRepository.findByCity(city);

        if(!formations.isEmpty()) {
            boolean isWar = false;
            for(Formation formation : formations) {
                Relation relation = relationRepository.findByPlayers(city.getPlayer(), formation.getPlayer());

                if((relation != null && relation.getRelation() <= 2) || (relation == null && !city.getPlayer().equals(formation.getPlayer())) || city.getPlayer().isComputer()) {
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

            List<Player> players = formations.stream().map(Formation::getPlayer).distinct().collect(Collectors.toList());

            if(!players.contains(city.getPlayer()))
                players.add(city.getPlayer());

            report.getParties().forEach(p -> p.setLost(true));
            for(Player player : players) {
                if(report.getParties().stream().filter(p -> p.getPlayer().equals(player)).count() == 0) {
                    Party party = new Party();
                    party.setPlayer(player);
                    party.setLost(false);
                    report.getParties().add(party);
                    eventRepository.add(new Event("War", "Krieg in " + city.getName(), "Deine Truppen sind in " + city.getName() + " in Kämpfe verwickelt!", city, player));
                } else {
                    report.getParties().stream().filter(p -> p.getPlayer().equals(player)).findFirst().get().setLost(false);
                }
            }

            for(Player player : players) {
                List<Unit> playerUnits = new ArrayList<>();
                List<Unit> enemyUnits = new ArrayList<>();
                Relation cityRelation = relationRepository.findByPlayers(player, city.getPlayer());
                int rel;
                if(cityRelation != null)
                    rel = cityRelation.getRelation();
                else
                    rel = 1;

                if(player.equals(city.getPlayer()))
                    rel = 3;

                List<Building> playerBuildings = city.getPlayer().equals(player) ? city.getBuildings() : new ArrayList<>();
                List<Building> enemyBuildings = (!city.getPlayer().equals(player) && rel <= 2) ? city.getBuildings() : new ArrayList<>();

                for(Formation formation : formations) {
                    if(!formation.getPlayer().equals(player)) {
                        Relation relation = relationRepository.findByPlayers(player, formation.getPlayer());

                        if(relation == null || relation.getRelation() <= 2) {
                            enemyUnits.addAll(formation.getUnits());
                        }
                    } else {
                        playerUnits.addAll(formation.getUnits());
                    }
                }

                if(rel <= 2) {
                    enemyUnits.addAll(city.getUnits());
                } else if(player.equals(city.getPlayer())) {
                    playerUnits.addAll(city.getUnits());
                }

                if((!enemyUnits.isEmpty() || !city.getPlayer().equals(player)) && !playerUnits.isEmpty()) {
                    EnumeratedDistribution<Unit> attackDistribution = new EnumeratedDistribution<>(getAttackerProbabilities(playerUnits, enemyUnits));
                    List<Unit> attackers = new ArrayList<>();
                    int max = Math.max(new Double((enemyUnits.size() + playerUnits.size()) / formations.size() * 0.1).intValue(), 30);
                    if(playerUnits.size() < max)
                        max = playerUnits.size();
                    for(int i = 0; i < max; i++) {
                        attackers.add(attackDistribution.sample());
                    }

                    for (Unit unit : attackers) {
                        if (unit.getBlueprint().getId() == 4) {
                            Unit selected = new EnumeratedDistribution<>(getUnitProbabilities(attackers, unit)).sample();

                            if (selected.getHealth() < 100) {
                                if (Math.random() < 0.01) {
                                    unit.setHealth(unit.getHealth() + 40);

                                    if (unit.getHealth() > 100)
                                        unit.setHealth(100);
                                }
                            }
                        } else {
                            if (enemyUnits.size() > 0) {
                                Unit selected = new EnumeratedDistribution<>(getUnitProbabilities(enemyUnits, unit)).sample();

                                int newHealth = selected.getHealth();

                                int counter = isCounter(selected.getBlueprint(), unit.getBlueprint()) ? unit.getBlueprint().getStrength() : 0;

                                newHealth -= unit.getBlueprint().getStrength() + counter;

                                selected.setHealth(newHealth);
                            } else if (enemyBuildings.stream().filter(b -> b.getBlueprint().getDefencePoints() > 0).count() > 0) {
                                Building selected = new EnumeratedDistribution<>(getBuildingProbabilities(enemyBuildings, true)).sample();

                                int newHealth = selected.getHealth();

                                if (unit.getBlueprint().getType() == 4)
                                    newHealth -= unit.getBlueprint().getStrength();
                                else
                                    newHealth -= unit.getBlueprint().getStrength() / 2;

                                selected.setHealth(newHealth);
                            } else {
                                if (unit.getBlueprint().getType() == 4)
                                    city.setHealth(city.getHealth() - unit.getBlueprint().getStrength());
                                else
                                    city.setHealth(city.getHealth() - unit.getBlueprint().getStrength() / 2);
                            }

                            if (unit.getBlueprint().getId() == 7) {
                                unit.setHealth(unit.getHealth() - unit.getBlueprint().getStrength() / 2);
                            }
                        }
                    }
                }

                if((!enemyUnits.isEmpty() || !city.getPlayer().equals(player)) && !playerBuildings.isEmpty()) {
                    for(Building building : playerBuildings) {
                        if(enemyUnits.size() > 0) {
                            Unit selected = enemyUnits.get(random.nextInt(enemyUnits.size()));

                            int newHealth = selected.getHealth();

                            newHealth -= building.getBlueprint().getDefencePoints();

                            selected.setHealth(newHealth);
                        }
                    }
                }
            }

            for(Formation formation : formations) {
                Party party = report.getParties().stream().filter(p -> p.getPlayer().equals(formation.getPlayer())).findFirst().get();

                try {
                    for (Unit unit : formation.getUnits()) {
                        if (unit.getHealth() <= 0) {
                            formation.removeUnit(unit);
                            party.setLosses(party.getLosses() + 1);
                            unitRepository.remove(unit);
                        }
                    }
                }
                catch(Exception ignored) {

                }
            }

            Party cityParty = report.getParties().stream().filter(p -> p.getPlayer().equals(city.getPlayer())).findFirst().get();
            for(Unit unit : city.getUnits()) {
                try {
                    if (unit.getHealth() <= 0) {
                        city.removeUnit(unit);
                        cityParty.setLosses(cityParty.getLosses() + 1);
                        unitRepository.remove(unit);
                    }
                }
                catch(Exception ignored) {

                }
            }

            formations.stream().filter(f -> f.getUnits().size() == 0).forEach(f -> {
                eventRepository.add(new Event("Loss", f.getName() + " wurde vernichtet.", "Deine Formation " + f.getName() + " wurde bei der Schlacht um " + city.getName() + " vom Gegner ausradiert!", city, f.getPlayer()));
                formations.remove(f);
                formationRepository.remove(f);
            });

            for(Building building : city.getBuildings()) {
                if(building.getHealth() <= 0) {
                    city.removeBuilding(building);
                    buildingRepository.remove(building);
                }
            }

            city.recalculateStrength();
            formations.forEach(Formation::recalculateStrength);
            formations.forEach(Formation::recalculateHealth);
            formations.forEach(Formation::recalculateSpeed);

            if(city.getHealth() <= 0) {
                if(!city.getPlayer().isComputer()) {
                    city.getPlayer().setSightUpdate(true);
                }
                eventRepository.add(new Event("CityLost", "Deine Stadt " + city.getName() + " wurde eingenommen.", "Deine Stadt " + city.getName() + " wurde vom Gegner eingenommen!", city, city.getPlayer()));
                if(formations.size() == 0 || formations.stream().map(Formation::getPlayer).distinct().count() > 1)
                    city.setPlayer(playerRepository.findByName("Freies Volk"));
                else {
                    Player conqueror = formations.get(0).getPlayer();

                    city.setPlayer(conqueror);
                    city.setReport(null);
                    city.getPlayer().setSightUpdate(true);

                    for(Objective objective : conqueror.getObjectives()) {
                        if(objective.getConquerCity() != null && objective.getConquerCity().equals(city))
                            objective.setCompleted(true);
                    }

                    if(conqueror.getNation().getId() == 1) {
                        List<Building> buildings = city.getBuildings().stream().filter(b -> b.getBlueprint().getId() == 14 || b.getBlueprint().getId() == 15).collect(Collectors.toList());

                        for(Building building : buildings) {
                            city.removeBuilding(building);

                            buildingRepository.remove(building);
                        }
                    } else if(conqueror.getNation().getId() == 2) {
                        List<Building> buildings = city.getBuildings().stream().filter(b -> b.getBlueprint().getId() == 7 || b.getBlueprint().getId() == 8 || b.getBlueprint().getId() == 12 || b.getBlueprint().getId() == 13).collect(Collectors.toList());

                        for(Building building : buildings) {
                            city.removeBuilding(building);
                            buildingRepository.remove(building);
                        }
                    }

                    eventRepository.add(new Event("CityConquered", "Du hast " + city.getName() + " eingenommen.", "Die Stadt " + city.getName() + " wurde deinem Imperium einverleibt!", city, conqueror));
                }
            }
        }
    }
}
