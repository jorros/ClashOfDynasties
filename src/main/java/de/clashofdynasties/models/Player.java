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
	private int coins;
	private int clan;

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
		return coins;
	}

	public void setCoins(int coins)
	{
		this.coins = coins;
	}

	public int getClan()
	{
		return clan;
	}

	public void setClan(int clan)
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

    public boolean equals(Object other)
    {
        if(other instanceof Player && ((Player)other).getId() == this.id)
            return true;
        else
            return false;
    }
}
