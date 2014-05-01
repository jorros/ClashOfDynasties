package de.clashofdynasties.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Biome {
    @Id
    private int id;

    private String name;
    private float productionFactor;
    private float fertilityFactor;

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

    public float getProductionFactor() {
        return productionFactor;
    }

    public void setProductionFactor(float productionFactor) {
        this.productionFactor = productionFactor;
    }

    public float getFertilityFactor() {
        return fertilityFactor;
    }

    public void setFertilityFactor(float fertilityFactor) {
        this.fertilityFactor = fertilityFactor;
    }

    public boolean equals(Object other) {
        if (other instanceof Biome && ((Biome) other).getId() == this.id)
            return true;
        else
            return false;
    }
}
