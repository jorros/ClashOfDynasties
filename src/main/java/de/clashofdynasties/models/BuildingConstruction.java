package de.clashofdynasties.models;

import org.springframework.data.mongodb.core.mapping.DBRef;

public class BuildingConstruction
{
    @DBRef
    private BuildingBlueprint blueprint;

    private int production;

    public BuildingBlueprint getBlueprint()
    {
        return blueprint;
    }

    public void setBlueprint(BuildingBlueprint blueprint)
    {
        this.blueprint = blueprint;
    }

    public int getProduction()
    {
        return production;
    }

    public void setProduction(int production)
    {
        this.production = production;
    }
}
