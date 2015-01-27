package com.dynastiesofscorpia.models;

import com.dynastiesofscorpia.repository.CityRepository;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Road {
    @Id
    private ObjectId id;

    private ObjectId point1;

    private ObjectId point2;

    private float weight;

    private long timestamp;

    public Road() {
        this.id = new ObjectId();
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

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void updateTimestamp() {
        this.timestamp = System.currentTimeMillis();
    }

    public double getLength() {
        return Math.sqrt(Math.pow(getPoint1().getX() - getPoint2().getX(), 2) + Math.pow(getPoint1().getY() - getPoint2().getY(), 2));
    }

    public boolean isVisible(Player player) {
        return getPoint1().isVisible(player) && getPoint2().isVisible(player);
    }

    public boolean equals(Object other) {
        if (other instanceof Road && ((Road) other).getId().equals(this.id))
            return true;
        else
            return false;
    }

    public ObjectNode toJSON(long timestamp) {
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode node = factory.objectNode();

        if (getTimestamp() >= timestamp) {
            node.put("point1", getPoint1().getId().toHexString());
            node.put("point2", getPoint2().getId().toHexString());
            node.put("nn", false);
        } else
            node.put("nn", true);

        return node;
    }
}
