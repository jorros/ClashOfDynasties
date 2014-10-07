package de.clashofdynasties.models;

import de.clashofdynasties.repository.NationRepository;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Document
public class Player {
    @Id
    private ObjectId id;

    @Indexed
    private String name;

    private String password;
    private double coins;

    private int nation;
    private String email;

    private int lastScrollX;
    private int lastScrollY;

    private boolean computer;
    private boolean activated;

    private int color;

    private Statistic statistic;

    private boolean sightUpdate;

    private Map<String, Boolean> notification;

    public Player() {
        this.id = new ObjectId();
        notification = new HashMap<>();
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public int getCoins() {
        return (int) Math.floor(this.coins);
    }

    public void setCoins(double coins) {
        this.coins = coins;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Nation getNation() {
        return NationRepository.get().findById(nation);
    }

    public void setNation(Nation nation) {
        this.nation = nation.getId();
    }

    public double getRawCoins() {
        return this.coins;
    }

    public void addCoins(double coins) {
        this.coins += coins;
    }

    public boolean isComputer() {
        return computer;
    }

    public void setComputer(boolean isComputer) {
        this.computer = isComputer;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public int getLastScrollX() {
        return lastScrollX;
    }

    public void setLastScrollX(int lastScrollX) {
        this.lastScrollX = lastScrollX;
    }

    public int getLastScrollY() {
        return lastScrollY;
    }

    public void setLastScrollY(int lastScrollY) {
        this.lastScrollY = lastScrollY;
    }

    public Statistic getStatistic() {
        return statistic;
    }

    public void setStatistic(Statistic statistic) {
        this.statistic = statistic;
    }

    public boolean isSightUpdate() {
        return sightUpdate;
    }

    public void setSightUpdate(boolean sightUpdate) {
        this.sightUpdate = sightUpdate;
    }

    public boolean hasNotification(String type) {
        if(notification.containsKey(type))
            return notification.get(type);
        else
            return true;
    }

    public void setNotification(String type, boolean value) {
        notification.put(type, value);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Player && ((Player) other).getId().equals(this.id))
            return true;
        else
            return false;
    }
}
