package de.clashofdynasties.models;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.clashofdynasties.repository.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;
import java.util.stream.Collectors;

@Document
public class City implements MapNode {
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

    private int strength;

    private Report report;

    private int capacity;
    private String name;

    private List<Integer> requiredItemTypes;

    private List<Integer> stopConsumption;

    private List<ObjectId> buildings;

    private BuildingConstruction buildingConstruction;

    private List<ObjectId> units;

    @Transient
    private List<Unit> unitObjects;

    private Map<Integer, Double> items;

    private long timestamp;

    private List<ObjectId> visibility;

    private boolean fire;

    private boolean plague;

    private String alias;

    public City() {
        units = Collections.synchronizedList(new ArrayList<>());
        items = new HashMap<>();
        buildings = Collections.synchronizedList(new ArrayList<>());
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

    public double getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public double getY() {
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

    public boolean isFire() {
        return fire;
    }

    public void setFire(boolean fire) {
        this.fire = fire;
    }

    public boolean isPlague() {
        return plague;
    }

    public void setPlague(boolean plague) {
        this.plague = plague;
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

    public void clearStoppedConsumption() {
        this.stopConsumption.clear();
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

    public void clearBuildings(boolean remove) {
        if(remove)
            BuildingRepository.get().remove(getBuildings());
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

    public void rebuildUnitList() {
        unitObjects = units.stream().map(u -> UnitRepository.get().findById(u)).collect(Collectors.toList());
    }

    public List<Unit> getUnits() {
        if(unitObjects == null)
            rebuildUnitList();

        return unitObjects;
    }

    public void addUnit(Unit unit) {
        if(!units.contains(unit.getId())) {
            units.add(unit.getId());
            unitObjects.add(unit);
        }
    }

    public void clearUnits(boolean remove) {
        if(remove)
            UnitRepository.get().remove(getUnits());
        units.clear();
        unitObjects.clear();
        recalculateStrength();
    }

    public void removeUnit(Unit unit) {
        units.remove(unit.getId());
        unitObjects.remove(unit);
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
            return population * 0.1 * getType().getProductionRate() * (getPlayer().getLevel() <= 5 ? 10 : 1);
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
        return buildings.stream().map(b -> BuildingRepository.get().findById(b)).filter(b -> b.getBlueprint().getId() == blueprint).count();
    }

    public long countBuildings(BuildingBlueprint blueprint) {
        return countBuildings(blueprint.getId());
    }

    public long countUnits(long blueprint) {
        return unitObjects.stream().filter(b -> b.getBlueprint().getId() == blueprint).count();
    }

    public long countUnits(UnitBlueprint blueprint) {
        return countUnits(blueprint.getId());
    }

    public double calculateCoins() {
        return (this.getPopulation() * ((double) this.getSatisfaction() / 100)) / 3600;
    }

    public double calculateMaintenance() {
        double modifier = 1.0;
        if(getBuildings().size() > getCapacity())
            modifier = 2.0;

        return ((double)this.getBuildings().size() / 3600) * modifier;
    }

    public int getIncome() {
        return (int) Math.ceil(this.calculateCoins() * 3600);
    }

    public int getOutcome() {
        return (int) Math.ceil(this.calculateMaintenance() * 3600);
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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void recalculateStrength() {
        strength = 0;
        if(getBuildings() != null)
            strength += getBuildings().stream().mapToInt(b -> b.getBlueprint().getDefencePoints()).sum();

        if(!units.isEmpty())
            strength += getUnits().stream().mapToInt(b -> b.getBlueprint().getStrength()).sum();
    }

    public int getDefencePoints() {
        int defence = strength;

        for(Formation formation : FormationRepository.get().findByCity(this)) {
            if(formation.getPlayer().equals(getPlayer()))
                defence += formation.getStrength();
        }

        return defence;
    }

    public List<Player> getVisibility() {
        return visibility.stream().map(p -> PlayerRepository.get().findById(p)).collect(Collectors.toList());
    }

    public boolean isVisible(Player player) {
        return visibility.contains(player.getId());
    }

    public void removeVisibility(Player player) {
        visibility.removeIf(v -> v.equals(player.getId()));
    }

    public void addVisibility(Player player) {
        if(!visibility.contains(player.getId()))
            visibility.add(player.getId());
    }

    public boolean isWonder() {
        return buildingConstruction != null && buildingConstruction.getBlueprint() instanceof BuildingBlueprint && ((BuildingBlueprint) buildingConstruction.getBlueprint()).getId() == 5;
    }

    public ObjectNode toJSON(boolean editor, long timestamp, Player player) {
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode node = factory.objectNode();

        if (getTimestamp() >= timestamp) {
            node.put("x", getX());
            node.put("y", getY());
            if(isVisible(player) || getType().getId() == 4)
                node.put("type", getType().getId());
            else
                node.put("type", 1);
            node.put("visible", isVisible(player));

            if(isVisible(player) || editor || isWonder()) {
                node.put("name", getName());
                node.put("nn", false);
                node.put("color", getPlayer().getColor());

                if (editor) {
                    node.put("resource", getResource().getId());
                    node.put("capacity", getRawCapacity());
                } else {
                    node.put("satisfaction", getSatisfaction());
                    node.put("population", getPopulation());
                    node.put("defence", getDefencePoints());
                    node.put("war", getReport() != null);
                    node.put("disease", isPlague());
                    node.put("fire", isFire());
                    node.put("wonder", isWonder());
                    node.put("build", player.equals(getPlayer()) && getType().getId() <= 3);

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

                    node.set("formations", formationNodes);
                }
            }
        } else
            node.put("nn", true);

        return node;
    }
}
