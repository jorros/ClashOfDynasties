package de.clashofdynasties.models;

import de.clashofdynasties.repository.RoadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.List;

public class Route
{
    @DBRef
    private City next;

    @DBRef
    private List<Road> roads;

    @DBRef
    private City target;

    @Transient
    private int time;

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

    public int getTime()
    {
        return time;
    }

    public void setTime(int time)
    {
        this.time = time;
    }

    public City getTarget()
    {
        return target;
    }

    public void setTarget(City target)
    {
        this.target = target;
    }
}
