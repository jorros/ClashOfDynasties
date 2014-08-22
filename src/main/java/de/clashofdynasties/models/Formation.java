package de.clashofdynasties.models;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class Formation {
    @Id
    private ObjectId id;

    private double x;
    private double y;

    @DBRef
    private Player player;

    @DBRef
    private City lastCity;

    @Transient
    private int health;

    private String name;

    @DBRef
    private List<Unit> units;

    private Route route;

    @Transient
    private boolean deployed;

    @Transient
    private int diplomacy;

    @DBRef
    private Road currentRoad;

    private long timestamp;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getHealth() {
        if (units == null || units.isEmpty())
            return 100;

        int maxHealth = 0;
        int health = 0;

        for (Unit unit : units) {
            maxHealth += 100;
            health += unit.getHealth();
        }

        return health / maxHealth * 100;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public City getLastCity() {
        return lastCity;
    }

    public void setLastCity(City lastCity) {
        this.lastCity = lastCity;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public boolean isDeployed() {
        return (this.getRoute() == null);
    }

    public void setDeployed(boolean deployed) {
        this.deployed = deployed;
    }

    public int getDiplomacy() {
        return diplomacy;
    }

    public void setDiplomacy(int diplomacy) {
        this.diplomacy = diplomacy;
    }

    public double getSpeed() {
        double speed = Double.MAX_VALUE;

        for (Unit unit : units) {
            if (unit.getSpeed() < speed)
                speed = unit.getSpeed();
        }

        return speed;
    }

    public Road getCurrentRoad() {
        return currentRoad;
    }

    public void setCurrentRoad(Road currentRoad) {
        this.currentRoad = currentRoad;
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

    public void move(int pixel) {
        if (!isDeployed()) {
            City to = route.getNext();

            double vecX = to.getX() - this.getX();
            double vecY = to.getY() - this.getY();
            double distance = Math.sqrt(Math.pow(to.getX() - getX(), 2) + Math.pow(to.getY() - getY(), 2));

            double multiplier = pixel / distance;

            setX(getX() + vecX * multiplier);
            setY(getY() + vecY * multiplier);
        }
    }

    public boolean equals(Object other) {
        if (other instanceof Formation && ((Formation) other).getId().equals(this.id))
            return true;
        else
            return false;
    }

    public ObjectNode toJSON(boolean editor, long timestamp) {
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode node = factory.objectNode();

        if (getTimestamp() >= timestamp) {
            node.put("x", Math.round(getX()));
            node.put("y", Math.round(getY()));
            node.put("deployed", isDeployed());
            node.put("diplomacy", getDiplomacy());
            node.put("name", getName());

            if (getRoute() != null)
                node.put("route", getRoute().toJSON());
        } else
            node.put("nn", true);

        return node;
    }
}
