package de.clashofdynasties.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Player
{
	@Id
	private int id;

	@Indexed
	private String name;

	private String password;
	private double coins;

    @DBRef
	private Clan clan;

	@DBRef
	private Nation nation;
	private String email;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getCoins()
	{
		return (int)Math.floor(this.coins);
	}

	public void setCoins(double coins)
	{
		this.coins = coins;
	}

	public Clan getClan()
	{
		return clan;
	}

	public void setClan(Clan clan)
	{
		this.clan = clan;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public Nation getNation()
	{
		return nation;
	}

	public void setNation(Nation nation)
	{
		this.nation = nation;
	}

    public double getRawCoins()
    {
        return this.coins;
    }

    public void addCoins(double coins)
    {
        this.coins += coins;
    }

    public boolean equals(Object other)
    {
        if(other instanceof Player && ((Player)other).getId() == this.id)
            return true;
        else
            return false;
    }
}
