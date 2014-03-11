package de.clashofdynasties.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Building
{
    @Id
    private int id;

	@DBRef
	private BuildingBlueprint blueprint;

	private int health;

	public BuildingBlueprint getBlueprint()
	{
		return blueprint;
	}

	public void setBlueprint(BuildingBlueprint blueprint)
	{
		this.blueprint = blueprint;
	}

	public int getHealth()
	{
		return health;
	}

	public void setHealth(int health)
	{
		this.health = health;
	}

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }
}
