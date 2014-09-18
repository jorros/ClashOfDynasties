package de.clashofdynasties.models;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.clashofdynasties.repository.CityRepository;
import de.clashofdynasties.repository.PlayerRepository;
import de.clashofdynasties.repository.UnitRepository;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Document
public class Formation {
    @Id
    private ObjectId id;

    private double x;
    private double y;

    private ObjectId player;

    private ObjectId lastCity;

    @Transient
    private int health;

    private String name;

    private List<ObjectId> units;

    private Route route;

    public Formation() {
        this.id = new ObjectId();
        this.units = new ArrayList<>();
    }

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

        for (Unit unit : getUnits()) {
            maxHealth += 100;
            health += unit.getHealth();
        }

        return health / maxHealth * 100;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name.length() > 14)
            name = name.substring(0, 14);

        this.name = name;
    }

    public Player getPlayer() {
        return PlayerRepository.get().findById(player);
    }

    public void setPlayer(Player player) {
        this.player = player.getId();
    }

    public City getLastCity() {
        return CityRepository.get().findById(lastCity);
    }

    public void setLastCity(City lastCity) {
        this.lastCity = lastCity.getId();
    }

    public List<Unit> getUnits() {
        return units.parallelStream().map(u -> UnitRepository.get().findById(u)).collect(Collectors.toList());
    }

    public void addUnit(Unit unit) {
        units.add(unit.getId());
    }

    public void removeUnit(Unit unit) {
        units.remove(unit.getId());
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

    public double getSpeed() {
        double speed = Double.MAX_VALUE;

        for (Unit unit : getUnits()) {
            if (unit.getSpeed() < speed)
                speed = unit.getSpeed();
        }

        return speed;
    }

    public long getTimestamp() {
        return timestamp;
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

    public boolean isVisible(Player player) {
        if(getRoute() != null) {
            Road current = getRoute().getCurrentRoad();

            return current.getPoint1().isVisible(player) && current.getPoint2().isVisible(player);
        }
        else
            return getLastCity().isVisible(player);
    }

    public ObjectNode toJSON(long timestamp, Player player) {
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode node = factory.objectNode();

        if (getTimestamp() >= timestamp) {
            node.put("x", Math.round(getX()));
            node.put("y", Math.round(getY()));
            node.put("deployed", isDeployed());
            node.put("color", getPlayer().getColor());
            node.put("name", getName());
            node.put("mvbl", player.equals(getPlayer()));

            if (getRoute() != null)
                node.put("route", getRoute().toJSON());
        } else
            node.put("nn", true);

        return node;
    }
}
