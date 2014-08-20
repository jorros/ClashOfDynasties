package de.clashofdynasties.models;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Document
public class City {
    @Id
    private ObjectId id;

    private int x;
    private int y;

    @DBRef
    private Player player;

    private int population;
    private double satisfaction;
    private int health;

    @DBRef
    private Resource resource;

    @DBRef
    private Biome biome;

    @DBRef
    private CityType type;

    private Report report;

    @Transient
    private List<Formation> formations;

    @Transient
    private int diplomacy;

    private int capacity;
    private String name;

    @DBRef
    private List<ItemType> requiredItemTypes;

    @DBRef
    private List<Item> stopConsumption;

    @DBRef
    private List<Building> buildings;

    private BuildingConstruction buildingConstruction;

    @DBRef
    private List<Unit> units;

    private Map<Integer, Double> items;

    private boolean sightUpdate;

    private long timestamp;

    @DBRef
    private List<Player> visibility;

    public City() {
        units = new ArrayList<>();
        items = new HashMap<>();
        buildings = new ArrayList<>();
        requiredItemTypes = new ArrayList<>();
        stopConsumption = new ArrayList<>();
        formations = new ArrayList<>();
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
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
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
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
        updateTimestamp();
    }

    public Biome getBiome() {
        return biome;
    }

    public void setBiome(Biome biome) {
        this.biome = biome;
        updateTimestamp();
    }

    public int getCapacity() {
        return (int)Math.ceil(capacity * type.getCapacity());
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
        return requiredItemTypes;
    }

    public void setRequiredItemTypes(List<ItemType> requiredItemTypes) {
        this.requiredItemTypes = requiredItemTypes;
    }

    public Map<Integer, Double> getItems() {
        return items;
    }

    public void setItems(Map<Integer, Double> items) {
        this.items = items;
    }

    public List<Building> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<Building> buildings) {
        this.buildings = buildings;
    }

    public CityType getType() {
        return type;
    }

    public void setType(CityType type) {
        this.type = type;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
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
            return population * type.getProductionRate();
        else
            return type.getProductionRate();
    }

    public List<Formation> getFormations() {
        return formations;
    }

    public void setFormations(List<Formation> formations) {
        this.formations = formations;
    }

    public int getDiplomacy() {
        return diplomacy;
    }

    public void setDiplomacy(int diplomacy) {
        this.diplomacy = diplomacy;
    }

    public double getStoredItem(int id) {
        if (getItems() == null)
            return 0;

        if (getItems().get(id) == null)
            return 0;

        return getItems().get(id);
    }

    public List<Item> getStopConsumption() {
        return stopConsumption;
    }

    public void setStopConsumption(List<Item> stopConsumption) {
        this.stopConsumption = stopConsumption;
    }

    public void toggleConsumption(Item item) {
        if(getStopConsumption() == null)
            setStopConsumption(new ArrayList<>());

        if(getStopConsumption().contains(item))
            getStopConsumption().remove(item);
        else
            getStopConsumption().add(item);
    }

    public boolean equals(Object other) {
        if (other instanceof City && ((City) other).getId().equals(this.id))
            return true;
        else
            return false;
    }

    public int countBuildings(int blueprint) {
        int counter = 0;
        if (buildings != null) {
            for (Building building : buildings) {
                if (building.getBlueprint().getId() == blueprint)
                    counter++;
            }
        }

        return counter;
    }

    public int countUnits(int blueprint) {
        int counter = 0;
        if (units != null) {
            for (Unit unit : units) {
                if (unit.getBlueprint().getId() == blueprint)
                    counter++;
            }
        }

        return counter;
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
        int defence = getType().getDefence();

        if(getBuildings() != null)
            defence += getBuildings().stream().mapToInt(b -> b.getBlueprint().getDefencePoints()).sum();

        return defence;
    }

    public List<Player> getVisibility() {
        return visibility;
    }

    public void setVisibility(List<Player> visibility) {
        this.visibility = visibility;
    }

    public boolean isSightUpdate() {
        return sightUpdate;
    }

    public void setSightUpdate(boolean sightUpdate) {
        this.sightUpdate = sightUpdate;
    }

    public ObjectNode toJSON(boolean editor, long timestamp, Player player) {
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode node = factory.objectNode();

        if (getTimestamp() >= timestamp) {
            node.put("x", getX());
            node.put("y", getY());
            node.put("type", getType().getId());
            node.put("visible", visibility.contains(player));

            if(visibility.contains(player) || editor) {
                node.put("diplomacy", getDiplomacy());
                node.put("name", getName());
                node.put("nn", false);

                if (editor) {
                    node.put("resource", getResource().getId());
                    node.put("capacity", getCapacity());
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
