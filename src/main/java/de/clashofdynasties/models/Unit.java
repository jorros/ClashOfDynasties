package de.clashofdynasties.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Unit
{
    @Id
    private int id;

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

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public double getSpeed()
    {
        double speed = blueprint.getSpeed();

        return speed / 2 + (speed / 2) * (health / 100);
    }

    public boolean equals(Object other)
    {
        if(other instanceof Unit && ((Unit)other).getId() == this.id)
            return true;
        else
            return false;
    }

}
