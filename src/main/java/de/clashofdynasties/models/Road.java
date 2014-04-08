package de.clashofdynasties.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Road
{
	@Id
	private int id;

	@DBRef
	private City point1;

	@DBRef
	private City point2;

	private float weight;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public City getPoint1()
	{
		return point1;
	}

	public void setPoint1(City point1)
	{
		this.point1 = point1;
	}

	public City getPoint2()
	{
		return point2;
	}

	public void setPoint2(City point2)
	{
		this.point2 = point2;
	}

	public float getWeight()
	{
		return weight;
	}

	public void setWeight(float weight)
	{
		this.weight = weight;
	}

    public double getLength()
    {
        return Math.sqrt(Math.pow(point1.getX() - point2.getX(), 2) + Math.pow(point1.getY() - point2.getY(), 2));
    }

    public boolean equals(Object other)
    {
        if(other instanceof Road && ((Road)other).getId() == this.id)
            return true;
        else
            return false;
    }
}
