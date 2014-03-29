package de.clashofdynasties.models;

import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

public class Route
{
    @DBRef
    private City next;

    @DBRef
    private List<Road> roads;

    public City getNext()
    {
        return next;
    }

    public void setNext(City next)
    {
        this.next = next;
    }

    public List<Road> getRoads()
    {
        return roads;
    }

    public void setRoads(List<Road> roads)
    {
        this.roads = roads;
    }
}
