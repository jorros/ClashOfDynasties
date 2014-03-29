package de.clashofdynasties.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class Formation
{
	@Id
	private int id;

	private int x;
	private int y;

	@DBRef
	private Player player;

	@DBRef
	private City lastCity;

	private int health;
	private String name;

	@DBRef
	private List<Unit> units;

	private Route route;

    @Transient
    private boolean deployed;

    @Transient
    private int diplomacy;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getX()
	{
		return x;
	}

	public void setX(int x)
	{
		this.x = x;
	}

	public int getY()
	{
		return y;
	}

	public void setY(int y)
	{
		this.y = y;
	}

	public int getHealth()
	{
		return health;
	}

	public void setHealth(int health)
	{
		this.health = health;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Player getPlayer()
	{
		return player;
	}

	public void setPlayer(Player player)
	{
		this.player = player;
	}

	public City getLastCity()
	{
		return lastCity;
	}

	public void setLastCity(City lastCity)
	{
		this.lastCity = lastCity;
	}

	public List<Unit> getUnits()
	{
		return units;
	}

	public void setUnits(List<Unit> units)
	{
		this.units = units;
	}

	public Route getRoute()
	{
		return route;
	}

	public void setRoute(Route route)
	{
		this.route = route;
	}

    public boolean isDeployed()
    {
        return deployed;
    }

    public void setDeployed(boolean deployed)
    {
        this.deployed = deployed;
    }

    public int getDiplomacy()
    {
        return diplomacy;
    }

    public void setDiplomacy(int diplomacy)
    {
        this.diplomacy = diplomacy;
    }

    public boolean equals(Object other)
    {
        if(other instanceof Formation && ((Formation)other).getId() == this.id)
            return true;
        else
            return false;
    }
}
