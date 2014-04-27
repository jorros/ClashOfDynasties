package de.clashofdynasties.models;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document
public class Caravan
{
    @Id
    private int id;

    private String name;

    @DBRef
    private City point1;

    @DBRef
    private City point2;

    private Route route;

    private int x;
    private int y;

    @Transient
    private int diplomacy;

    @DBRef
    private Player player;

    @DBRef
    private Item point1Item;
    private int point1Load;

    @DBRef Item point1StoreItem;
    private int point1Store;

    @DBRef
    private Item point2Item;
    private int point2Load;

    @DBRef Item point2StoreItem;
    private int point2Store;

    private boolean terminate;

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

    public Route getRoute()
    {
        return route;
    }

    public void setRoute(Route route)
    {
        this.route = route;
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

    public Player getPlayer()
    {
        return player;
    }

    public void setPlayer(Player player)
    {
        this.player = player;
    }

    public Item getPoint1Item()
    {
        return point1Item;
    }

    public void setPoint1Item(Item point1Item)
    {
        this.point1Item = point1Item;
    }

    public int getPoint1Store()
    {
        return point1Store;
    }

    public void setPoint1Store(int point1Store)
    {
        this.point1Store = point1Store;
    }

    public int getPoint1Load()
    {
        return point1Load;
    }

    public void setPoint1Load(int point1Load)
    {
        this.point1Load = point1Load;
    }

    public Item getPoint2Item()
    {
        return point2Item;
    }

    public void setPoint2Item(Item point2Item)
    {
        this.point2Item = point2Item;
    }

    public int getPoint2Store()
    {
        return point2Store;
    }

    public void setPoint2Store(int point2Store)
    {
        this.point2Store = point2Store;
    }

    public int getPoint2Load()
    {
        return point2Load;
    }

    public void setPoint2Load(int point2Load)
    {
        this.point2Load = point2Load;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getDiplomacy()
    {
        return diplomacy;
    }

    public void setDiplomacy(int diplomacy)
    {
        this.diplomacy = diplomacy;
    }

    public boolean isTerminate()
    {
        return terminate;
    }

    public void setTerminate(boolean terminate)
    {
        this.terminate = terminate;
    }

    public Item getPoint1StoreItem()
    {
        return point1StoreItem;
    }

    public void setPoint1StoreItem(Item point1StoreItem)
    {
        this.point1StoreItem = point1StoreItem;
    }

    public Item getPoint2StoreItem()
    {
        return point2StoreItem;
    }

    public void setPoint2StoreItem(Item point2StoreItem)
    {
        this.point2StoreItem = point2StoreItem;
    }

    public void move(int pixel)
    {
        City to = route.getNext();

        int vecX = to.getX() - this.getX();
        int vecY = to.getY() - this.getY();
        double distance = Math.sqrt(Math.pow(to.getX() - getX(), 2) + Math.pow(to.getY() - getY(), 2));

        double multiplier = pixel / distance;

        setX(new Double(getX() + vecX * multiplier).intValue());
        setY(new Double(getY() + vecY * multiplier).intValue());
    }

    public boolean equals(Object other)
    {
        if(other instanceof City && ((City) other).getId() == this.id)
            return true;
        else
            return false;
    }

    public ObjectNode toJSON(boolean editor, long timestamp)
    {
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode node = factory.objectNode();

        node.put("x", x);
        node.put("y", y);
        node.put("diplomacy", getDiplomacy());
        node.put("name", getName());
        node.put("route", getRoute().toJSON());

        return node;
    }
}
