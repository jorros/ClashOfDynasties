package de.clashofdynasties.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document
public class BuildingBlueprint implements IBlueprint {
    @Id
    private int id;

    private int price;

    private int requiredProduction;

    @DBRef
    private Nation nation;

    @DBRef
    private Resource requiredResource;

    private String name;

    private String description;

    private Map<String, Float> effects;

    @DBRef
    private List<Biome> requiredBiomes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getRequiredProduction() {
        return requiredProduction;
    }

    public void setRequiredProduction(int requiredProduction) {
        this.requiredProduction = requiredProduction;
    }

    public Nation getNation() {
        return nation;
    }

    public void setNation(Nation nation) {
        this.nation = nation;
    }

    public Resource getRequiredResource() {
        return requiredResource;
    }

    public void setRequiredResource(Resource requiredResource) {
        this.requiredResource = requiredResource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Float> getEffects() {
        return effects;
    }

    public void setEffects(Map<String, Float> effects) {
        this.effects = effects;
    }

    public List<Biome> getRequiredBiomes() {
        return requiredBiomes;
    }

    public void setRequiredBiomes(List<Biome> requiredBiomes) {
        this.requiredBiomes = requiredBiomes;
    }

    public boolean equals(Object other) {
        if (other instanceof BuildingBlueprint && ((BuildingBlueprint) other).getId() == this.id)
            return true;
        else
            return false;
    }
}
