package de.clashofdynasties.models;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.clashofdynasties.repository.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Document
public class City {
    @Id
    private ObjectId id;

    private int x;
    private int y;

    private ObjectId player;

    private int population;
    private double satisfaction;
    private int health;

    private int resource;

    private int biome;

    private int type;

    private Report report;

    private int capacity;
    private String name;

    private List<Integer> requiredItemTypes;

    private List<Integer> stopConsumption;

    private List<ObjectId> buildings;

    private BuildingConstruction buildingConstruction;

    private List<ObjectId> units;

    private Map<Integer, Double> items;

    private long timestamp;

    private List<ObjectId> visibility;

    public City() {
        units = new ArrayList<>();
        items = new HashMap<>();
        buildings = new ArrayList<>();
        requiredItemTypes = new ArrayList<>();
        stopConsumption = new ArrayList<>();
        visibility = new ArrayList<>();
        id = new ObjectId();
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Player getPlayer() {
        return PlayerRepository.get().findById(player);
    }

    public void setPlayer(Player player) {
        this.player = player.getId();
        updateTimestamp();
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
        updateTimestamp();
    }

    public int getSatisfaction() {
        return (int)Math.round(satisfaction);
    }

    public double getRawSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(double satisfaction) {
        this.satisfaction = satisfaction;
        updateTimestamp();
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
        updateTimestamp();
    }

    public Resource getResource() {
        return ResourceRepository.get().findById(resource);
    }

    public void setResource(Resource resource) {
        this.resource = resource.getId();
        updateTimestamp();
    }

    public Biome getBiome() {
        return BiomeRepository.get().findById(biome);
    }

    public void setBiome(Biome biome) {
        this.biome = biome.getId();
        updateTimestamp();
    }

    public int getCapacity() {
        return (int)Math.ceil(capacity * getType().getCapacity());
    }

    public int getRawCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
        updateTimestamp();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        updateTimestamp();
    }

    public List<ItemType> getRequiredItemTypes() {
        return requiredItemTypes.stream().map(r -> ItemTypeRepository.get().findById(r)).collect(Collectors.toList());
    }

    public void addRequiredItemType(ItemType requiredItemType) {
        this.requiredItemTypes.add(requiredItemType.getId());
    }

    public void clearRequiredItemTypes() {
        this.requiredItemTypes.clear();
    }

    public Map<Integer, Double> getItems() {
        return items;
    }

    public void setItems(Map<Integer, Double> items) {
        this.items = items;
    }

    public List<Building> getBuildings() {
        return buildings.stream().map(b -> BuildingRepository.get().findById(b)).collect(Collectors.toList());
    }

    public void clearBuildings() {
        buildings.clear();
    }

    public void addBuilding(Building building) {
        this.buildings.add(building.getId());
    }

    public void removeBuilding(Building building) {
        this.buildings.remove(building.getId());
    }

    public CityType getType() {
        return CityTypeRepository.get().findById(type);
    }

    public void setType(CityType type) {
        this.type = type.getId();
    }

    public List<Unit> getUnits() {
        return units.stream().map(u -> UnitRepository.get().findById(u)).collect(Collectors.toList());
    }

    public void clearUnits() {
        units.clear();
    }

    public void addUnit(Unit unit) {
        units.add(unit.getId());
    }

    public void removeUnit(Unit unit) {
        units.remove(unit.getId());
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
        updateTimestamp();
    }

    public BuildingConstruction getBuildingConstruction() {
        return buildingConstruction;
    }

    public void setBuildingConstruction(BuildingConstruction buildingConstruction) {
        this.buildingConstruction = buildingConstruction;
    }

    public double getProductionRate() {
        if(population > 0)
            return population * getType().getProductionRate();
        else
            return getType().getProductionRate();
    }

    public List<Formation> getFormations() {
        return FormationRepository.get().findByCity(this);
    }

    public double getStoredItem(int id) {
        if (getItems() == null)
            return 0;

        if (getItems().get(id) == null)
            return 0;

        return getItems().get(id);
    }

    public void setStoredItem(int id, double amount) {
        getItems().put(id, amount);
    }

    public List<Item> getStopConsumption() {
        return stopConsumption.stream().map(i -> ItemRepository.get().findById(i)).collect(Collectors.toList());
    }

    public void toggleConsumption(Item item) {
        if(getStopConsumption() == null)
            stopConsumption = new ArrayList<>();

        if(getStopConsumption().contains(item))
            stopConsumption.remove((Integer)item.getId());
        else
            stopConsumption.add(item.getId());
    }

    public boolean equals(Object other) {
        return (other instanceof City && ((City) other).getId().equals(this.id));
    }

    public long countBuildings(long blueprint) {
        return getBuildings().stream().filter(b -> b.getBlueprint().getId() == blueprint).count();
    }

    public long countBuildings(BuildingBlueprint blueprint) {
        return countBuildings(blueprint.getId());
    }

    public long countUnits(long blueprint) {
        return getUnits().stream().filter(b -> b.getBlueprint().getId() == blueprint).count();
    }

    public long countUnits(UnitBlueprint blueprint) {
        return countUnits(blueprint.getId());
    }

    public double calculateCoins() {
        return (this.getPopulation() * ((double) this.getSatisfaction() / 100)) / 360;
    }

    public double calculateMaintenance() { return ((double)this.getBuildings().size() / 360); }

    public int getIncome() {
        return (int) Math.ceil(this.calculateCoins() * 360);
    }

    public int getOutcome() {
        return (int) Math.ceil(this.calculateMaintenance() * 360);
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void updateTimestamp() {
        this.timestamp = System.currentTimeMillis();
    }

    public int getDefencePoints() {
        int defence = 0;

        if(getBuildings() != null)
            defence += getBuildings().stream().mapToInt(b -> b.getBlueprint().getDefencePoints()).sum();

        return defence;
    }

    public List<Player> getVisibility() {
        return visibility.stream().map(p -> PlayerRepository.get().findById(p)).collect(Collectors.toList());
    }

    public boolean isVisible(Player player) {
        return visibility.contains(player.getId());
    }

    public void removeVisibility(Player player) {
        visibility.remove(player.getId());
    }

    public void addVisibility(Player player) {
        visibility.add(player.getId());
    }

    public ObjectNode toJSON(boolean editor, long timestamp, Player player) {
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode node = factory.objectNode();

        if (getTimestamp() >= timestamp) {
            node.put("x", getX());
            node.put("y", getY());
            node.put("type", getType().getId());
            node.put("visible", isVisible(player));

            if(isVisible(player) || editor) {
                node.put("name", getName());
                node.put("nn", false);
                node.put("color", getPlayer().getColor());

                if(!editor)
                    node.put("build", player.equals(getPlayer()) && getType().getId() <= 3);

                if (editor) {
                    node.put("resource", getResource().getId());
                    node.put("capacity", getRawCapacity());
                } else {
                    node.put("satisfaction", getSatisfaction());
                    node.put("population", getPopulation());
                    node.put("defence", getDefencePoints());

                    List<Formation> formations = getFormations();
                    ArrayNode formationNodes = factory.arrayNode();

                    if (formations != null) {
                        for (Formation formation : formations) {
                            ObjectNode formationNode = factory.objectNode();
                            formationNode.put("id", formation.getId().toHexString());
                            formationNode.put("name", formation.getName());
                            formationNodes.add(formationNode);
                        }
                    }

                    node.put("formations", formationNodes);
                }
            }
        } else
            node.put("nn", true);

        return node;
    }
}
