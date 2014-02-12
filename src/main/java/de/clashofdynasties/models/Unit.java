package de.clashofdynasties.models;

import org.springframework.data.mongodb.core.mapping.DBRef;

public class Unit
{
	@DBRef
	private UnitBlueprint blueprint;

	private int health;

	public UnitBlueprint getBlueprint()
	{
		return blueprint;
	}

	public void setBlueprint(UnitBlueprint blueprint)
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
