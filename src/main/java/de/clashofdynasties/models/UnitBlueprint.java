package de.clashofdynasties.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document
public class UnitBlueprint
{
	@Id
	private int id;

	private String name;
	private int price;
    private int requiredProduction;
    private double speed;

	@DBRef
	private Nation nation;

	private String description;
	private Map<String, Double> effects;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getPrice()
	{
		return price;
	}

	public void setPrice(int price)
	{
		this.price = price;
	}

	public Nation getNation()
	{
		return nation;
	}

	public void setNation(Nation nation)
	{
		this.nation = nation;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public Map<String, Double> getEffects()
	{
		return effects;
	}

	public void setEffects(Map<String, Double> effects)
	{
		this.effects = effects;
	}

    public int getRequiredProduction()
    {
        return requiredProduction;
    }

    public void setRequiredProduction(int requiredProduction)
    {
        this.requiredProduction = requiredProduction;
    }

    public double getSpeed()
    {
        return speed;
    }

    public void setSpeed(double speed)
    {
        this.speed = speed;
    }

    public boolean equals(Object other)
    {
        if(other instanceof UnitBlueprint && ((UnitBlueprint)other).getId() == this.id)
            return true;
        else
            return false;
    }
}
