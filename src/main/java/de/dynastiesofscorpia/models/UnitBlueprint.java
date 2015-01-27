package de.dynastiesofscorpia.models;

import de.dynastiesofscorpia.repository.NationRepository;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class UnitBlueprint implements IBlueprint {
    @Id
    private int id;

    private String name;
    private int price;
    private int requiredProduction;
    private double speed;
    private int type;
    private int strength;

    private int nation;

    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Nation getNation() {
        return NationRepository.get().findById(nation);
    }

    public void setNation(Nation nation) {
        this.nation = nation.getId();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRequiredProduction() {
        return requiredProduction;
    }

    public void setRequiredProduction(int requiredProduction) {
        this.requiredProduction = requiredProduction;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public boolean equals(Object other) {
        if (other instanceof UnitBlueprint && ((UnitBlueprint) other).getId() == this.id)
            return true;
        else
            return false;
    }
}
