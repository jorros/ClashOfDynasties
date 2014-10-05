package de.clashofdynasties.models;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.clashofdynasties.repository.CityRepository;
import de.clashofdynasties.repository.ItemRepository;
import de.clashofdynasties.repository.PlayerRepository;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Caravan {
    @Id
    private ObjectId id;

    private String name;

    private ObjectId point1;

    private ObjectId point2;

    private Route route;

    private double x;
    private double y;

    private ObjectId player;

    private int point1Item;
    private int point1Load;

    private int point1StoreItem;
    private double point1Store;

    private int point2Item;
    private int point2Load;

    private int point2StoreItem;
    private double point2Store;

    private int direction;

    private boolean terminate;

    private long timestamp;

    private boolean paused;

    public Caravan() {
        id = new ObjectId();
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public City getPoint1() {
        return CityRepository.get().findById(point1);
    }

    public void setPoint1(City point1) {
        this.point1 = point1.getId();
    }

    public City getPoint2() {
        return CityRepository.get().findById(point2);
    }

    public void setPoint2(City point2) {
        this.point2 = point2.getId();
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Player getPlayer() {
        return PlayerRepository.get().findById(player);
    }

    public void setPlayer(Player player) {
        this.player = player.getId();
    }

    public Item getPoint1Item() {
        return ItemRepository.get().findById(point1Item);
    }

    public void setPoint1Item(Item point1Item) {
        if(point1Item != null)
            this.point1Item = point1Item.getId();
        else
            this.point1Item = 0;
    }

    public double getPoint1Store() {
        return point1Store;
    }

    public void setPoint1Store(double point1Store) {
        this.point1Store = point1Store;
    }

    public int getPoint1Load() {
        return point1Load;
    }

    public void setPoint1Load(int point1Load) {
        this.point1Load = point1Load;
    }

    public Item getPoint2Item() {
        return ItemRepository.get().findById(point2Item);
    }

    public void setPoint2Item(Item point2Item) {
        if(point2Item != null)
            this.point2Item = point2Item.getId();
        else
            this.point2Item = 0;
    }

    public double getPoint2Store() {
        return point2Store;
    }

    public void setPoint2Store(double point2Store) {
        this.point2Store = point2Store;
    }

    public int getPoint2Load() {
        return point2Load;
    }

    public void setPoint2Load(int point2Load) {
        this.point2Load = point2Load;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name.length() > 14)
            name = name.substring(0, 14);

        this.name = name;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public boolean isTerminate() {
        return terminate;
    }

    public void setTerminate(boolean terminate) {
        this.terminate = terminate;
    }

    public Item getPoint1StoreItem() {
        return ItemRepository.get().findById(point1StoreItem);
    }

    public void setPoint1StoreItem(Item point1StoreItem) {
        if(point1StoreItem != null)
            this.point1StoreItem = point1StoreItem.getId();
        else
            this.point1StoreItem = 0;
    }

    public Item getPoint2StoreItem() {
        return ItemRepository.get().findById(point2StoreItem);
    }

    public void setPoint2StoreItem(Item point2StoreItem) {
        if(point2StoreItem != null)
            this.point2StoreItem = point2StoreItem.getId();
        else
            this.point2StoreItem = 0;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void updateTimestamp() {
        this.timestamp = System.currentTimeMillis();
    }

    public void move(int pixel) {
        City to = route.getNext();

        double vecX = to.getX() - this.getX();
        double vecY = to.getY() - this.getY();
        double distance = Math.sqrt(Math.pow(to.getX() - getX(), 2) + Math.pow(to.getY() - getY(), 2));

        double multiplier = pixel / distance;

        setX(new Double(getX() + vecX * multiplier).intValue());
        setY(new Double(getY() + vecY * multiplier).intValue());
    }

    public boolean isVisible(Player player) {
        if(isPaused())
            return false;

        Road current = getRoute().getCurrentRoad();

        return current.getPoint1().isVisible(player) && current.getPoint2().isVisible(player);
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean equals(Object other) {
        if (other instanceof Caravan && ((Caravan) other).getId().equals(this.id))
            return true;
        else
            return false;
    }

    public ObjectNode toJSON(long timestamp) {
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode node = factory.objectNode();

        node.put("x", Math.round(getX()));
        node.put("y", Math.round(getY()));
        node.put("name", getName());
        node.put("direction", getRoute().getNext().getX() - getX() < 0 ? "2" : "");

        if (getTimestamp() >= timestamp) {
            node.put("route", getRoute().toJSON());
            node.put("color", getPlayer().getColor());
        }

        return node;
    }
}
