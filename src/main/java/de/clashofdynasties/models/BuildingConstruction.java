package de.clashofdynasties.models;

import org.springframework.data.mongodb.core.mapping.DBRef;

public class BuildingConstruction {
    @DBRef
    private IBlueprint blueprint;

    private double production;

    private int count;

    public IBlueprint getBlueprint() {
        return blueprint;
    }

    public void setBlueprint(IBlueprint blueprint) {
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

    public double getRequiredProduction() {
        return blueprint.getRequiredProduction() * count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
