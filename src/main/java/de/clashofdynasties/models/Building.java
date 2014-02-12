package de.clashofdynasties.models;

import org.springframework.data.mongodb.core.mapping.DBRef;

public class Building
{
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
}
