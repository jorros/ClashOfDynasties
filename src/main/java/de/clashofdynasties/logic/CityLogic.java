package de.clashofdynasties.logic;

import de.clashofdynasties.models.*;
import de.clashofdynasties.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        CityType cityType = city.getType();
        Map<Integer, Double> store = city.getItems();
        List<ItemType> requiredItems = city.getRequiredItemTypes();

        double maxSatisfaction = 0;
        double firstLevelSatisfaction = 0;
        double secondLevelSatisfaction = 0;

        for(ItemType itemType : requiredItems) {
            boolean provided = false;

            double consumRate = cityType.getConsumeBasic();

            switch(itemType.getType()) {
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
            for(Item item : items) {
                if(store.containsKey(item.getId()) && (city.getStopConsumption() == null || !city.getStopConsumption().contains(item))) {
                    double amount = store.get(item.getId());
                    double satisfied = 1;

                    amount -= city.getPopulation() * consumRate;

                    if(amount < 0) {
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

        if(city.getBuildings().size() <= city.getCapacity()) {
            if(city.getBuildings().stream().filter(b -> b.getBlueprint().getId() == 4).count() > 0)
                secondLevelSatisfaction += (5.0/14);

            if(city.getBuildings().stream().filter(b -> b.getBlueprint().getId() == 6).count() > 0)
                secondLevelSatisfaction += 4.0/14;

            if(city.getBuildings().stream().filter(b -> b.getBlueprint().getId() == 11).count() > 0)
                secondLevelSatisfaction += 3.0/14;

            if(city.getBuildings().stream().filter(b -> b.getBlueprint().getId() == 13).count() > 0)
                secondLevelSatisfaction += 2.0/14;

            if(city.getBuildings().stream().filter(b -> b.getBlueprint().getId() == 14).count() > 0)
                secondLevelSatisfaction += 2.0/14;

            if(secondLevelSatisfaction > 1)
                secondLevelSatisfaction = 1;
        }

        maxSatisfaction = (firstLevelSatisfaction * 2.0/3 + secondLevelSatisfaction * 1.0/3) * 100;

        double computedSatisfaction;
        if(maxSatisfaction > city.getRawSatisfaction())
            computedSatisfaction = city.getRawSatisfaction() + 1.0/360;
        else if(maxSatisfaction < city.getRawSatisfaction())
            computedSatisfaction = city.getRawSatisfaction() - 1.0/360;
        else
            computedSatisfaction = city.getRawSatisfaction();

        if(computedSatisfaction > 100)
            computedSatisfaction = 100;
        else if(computedSatisfaction < 0)
            computedSatisfaction = 0;

        city.setSatisfaction(computedSatisfaction);

        long maxPeople = city.getBuildings().stream().filter(b -> b.getBlueprint().getId() == 1).count() * 10;

        if((city.getSatisfaction() >= 60 || city.getPopulation() == 0) && Math.random() < 0.01 && city.getPopulation() < maxPeople) {
            city.setPopulation(city.getPopulation() + 1);
        } else if(((city.getSatisfaction() < 30 && Math.random() < 0.02) || maxPeople < city.getPopulation()) && city.getPopulation() > 0) {
            city.setPopulation(city.getPopulation() - 1);
        }
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

                if (construction.getProduction() >= construction.getBlueprint().getRequiredProduction()) {
                    if (construction.getBlueprint() instanceof BuildingBlueprint) {
                        Building building = new Building();
                        building.setBlueprint((BuildingBlueprint) construction.getBlueprint());
                        building.setHealth(100);
                        buildingRepository.save(building);

                        city.getBuildings().add(building);

                        eventRepository.save(new Event("ProductionReady", "Neues Gebäude errichtet", "In " + city.getName() + " wurde das Gebäude " + building.getBlueprint().getName() + " errichtet!", city, city.getPlayer()));
                    } else if (construction.getBlueprint() instanceof UnitBlueprint) {
                        Unit unit = new Unit();
                        unit.setBlueprint((UnitBlueprint) construction.getBlueprint());
                        unit.setHealth(100);
                        unitRepository.save(unit);

                        city.getUnits().add(unit);

                        eventRepository.save(new Event("ProductionReady", "Neue Einheit ausgebildet", "In " + city.getName() + " wurde die Einheit " + unit.getBlueprint().getName() + " ausgebildet!", city, city.getPlayer()));
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
}
