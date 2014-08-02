package de.clashofdynasties.service;

import de.clashofdynasties.models.*;
import de.clashofdynasties.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LogicService {
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

    @Scheduled(fixedDelay = 1000)
    public void Worker() {
        try {
            processCities();
            processRanking();
            processFormations();
            processCaravans();
            processDiplomacy();
            processWar();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    private void processCities() {
        List<City> cities = cityRepository.findAll();

        for(City city : cities) {
            CityType cityType = city.getType();

            /*
                    Bevölkerungskonsum
             */
            Map<Integer, Double> store = city.getItems();
            List<ItemType> requiredItems = city.getRequiredItemTypes();

            double satisfaction = 0;

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

                        amount -= city.getPopulation() * consumRate;
                        if(amount < 0) {
                            satisfaction += amount;
                            amount = 0;
                        }

                        store.put(item.getId(), amount);

                        provided = true;
                        break;
                    }
                }

                if(!provided)
                    satisfaction -= city.getPopulation() * consumRate;
            }
            if(satisfaction == 0)
                satisfaction = 10;

            int computedSatisfaction = city.getSatisfaction() + (int)Math.ceil(satisfaction / 10);
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

            /*
                    Produktion
             */
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

            /*
                    Kosten berechnen
             */
            Player player = playerRepository.findOne(city.getPlayer().getId());
            player.addCoins(city.calculateCoins() - city.calculateMaintenance());
            playerRepository.save(player);

            /*
                    Gebäude und Einheitenproduktion
             */
            BuildingConstruction construction = city.getBuildingConstruction();
            if(construction != null) {
                if(city.getCapacity() < city.getBuildings().size())
                    city.setBuildingConstruction(null);
                else
                {
                    double production = 0;

                    if (city.getReport() == null) {
                        production = city.getProductionRate();
                    }

                    construction.addProduction(production);

                    city.setBuildingConstruction(construction);

                    if(construction.getProduction() >= construction.getBlueprint().getRequiredProduction()) {
                        if(construction.getBlueprint() instanceof BuildingBlueprint) {
                            Building building = new Building();
                            building.setBlueprint((BuildingBlueprint)construction.getBlueprint());
                            building.setHealth(100);
                            buildingRepository.save(building);

                            city.getBuildings().add(building);

                            eventRepository.save(new Event("ProductionReady", "Neues Gebäude errichtet", "In " + city.getName() + " wurde das Gebäude " + building.getBlueprint().getName() + " errichtet!", city, city.getPlayer()));
                        } else if(construction.getBlueprint() instanceof UnitBlueprint) {
                            Unit unit = new Unit();
                            unit.setBlueprint((UnitBlueprint)construction.getBlueprint());
                            unit.setHealth(100);
                            unitRepository.save(unit);

                            city.getUnits().add(unit);

                            eventRepository.save(new Event("ProductionReady", "Neue Einheit ausgebildet", "In " + city.getName() + " wurde die Einheit " + unit.getBlueprint().getName() + " ausgebildet!", city, city.getPlayer()));
                        }

                        city.setBuildingConstruction(null);
                    }
                }
            }

            /*
                    Stadtstatus ermitteln
             */
            if(city.getType().getId() != 4) {
                if(city.getPopulation() < 50)
                    city.setType(cityTypeRepository.findOne(1));
                else if(city.getPopulation() < 200)
                    city.setType(cityTypeRepository.findOne(2));
                else
                    city.setType(cityTypeRepository.findOne(3));

                if(city.getBuildings().size() > city.getCapacity())
                    city.setSatisfaction(20);
            }

            cityRepository.save(city);
        }
    }

    private void processRanking() {

    }

    private void processFormations() {

    }

    private void processCaravans() {

    }

    private void processDiplomacy() {

    }

    private void processWar() {

    }
}
