package de.clashofdynasties.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
public class Player {
    @Id
    private String id;

    @Indexed
    private String name;

    private String password;
    private double coins;

    @DBRef
    private Nation nation;
    private String email;

    private List<Event> events;

    private int lastScrollX;
    private int lastScrollY;

    private boolean computer;
    private boolean activated;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCoins() {
        return (int) Math.floor(this.coins);
    }

    public void setCoins(double coins) {
        this.coins = coins;
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
        return nation;
    }

    public void setNation(Nation nation) {
        this.nation = nation;
    }

    public double getRawCoins() {
        return this.coins;
    }

    public void addCoins(double coins) {
        this.coins += coins;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
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

    public void addEvent(Event event) {
        if(getEvents() == null) {
            ArrayList<Event> events = new ArrayList<>();
            setEvents(events);
        }

        getEvents().add(event);
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

    public boolean equals(Object other) {
        if (other instanceof Player && ((Player) other).getId().equals(this.id))
            return true;
        else
            return false;
    }
}
