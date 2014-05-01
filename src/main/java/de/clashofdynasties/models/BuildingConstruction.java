package de.clashofdynasties.models;

import org.springframework.data.mongodb.core.mapping.DBRef;

public class BuildingConstruction {
    @DBRef
    private BuildingBlueprint blueprint;

    private double production;

    public BuildingBlueprint getBlueprint() {
        return blueprint;
    }

    public void setBlueprint(BuildingBlueprint blueprint) {
        this.blueprint = blueprint;
    }

    public double getProduction() {
        return production;
    }

    public void setProduction(double production) {
        this.production = production;
    }

    public void addProduction(double production) {
        this.production += production;
    }
}
