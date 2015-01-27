package com.dynastiesofscorpia.models;

import com.dynastiesofscorpia.repository.BuildingBlueprintRepository;
import com.dynastiesofscorpia.repository.UnitBlueprintRepository;

public class BuildingConstruction {
    private int unitBlueprint;
    private int buildingBlueprint;

    private double production;

    private int count;

    public IBlueprint getBlueprint() {
        if(unitBlueprint > 0)
            return UnitBlueprintRepository.get().findById(unitBlueprint);
        else
            return BuildingBlueprintRepository.get().findById(buildingBlueprint);
    }

    public void setBlueprint(IBlueprint blueprint) {
        if(blueprint instanceof UnitBlueprint) {
            unitBlueprint = ((UnitBlueprint) blueprint).getId();
            buildingBlueprint = 0;
        }
        else {
            buildingBlueprint = ((BuildingBlueprint) blueprint).getId();
            unitBlueprint = 0;
        }
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
        return getBlueprint().getRequiredProduction() * count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
