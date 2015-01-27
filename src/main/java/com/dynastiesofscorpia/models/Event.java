package com.dynastiesofscorpia.models;

import com.dynastiesofscorpia.repository.CityRepository;
import com.dynastiesofscorpia.repository.PlayerRepository;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Event {
    @Id
    ObjectId id;

    private ObjectId city;

    private ObjectId player;

    private String action;
    private String type;
    private String description;
    private String title;
    private long timestamp;

    public Event() {
        this.id = new ObjectId();
    }

    public Event(String type, String title, String description, String action, Player player) {
        this.action = action;
        this.type = type;
        this.title = title;
        this.description = description;
        this.player = player.getId();
        this.id = new ObjectId();

        updateTimestamp();
    }

    public Event(String type, String title, String description, City city, Player player) {
        this.type = type;
        this.title = title;
        this.description = description;
        this.city = city.getId();
        this.player = player.getId();
        this.id = new ObjectId();

        updateTimestamp();
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public City getCity() {
        return CityRepository.get().findById(city);
    }

    public void setCity(City city) {
        this.city = city.getId();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Player getPlayer() {
        return PlayerRepository.get().findById(player);
    }

    public void setPlayer(Player player) {
        this.player = player.getId();
    }

    public ObjectNode toJSON(long timestamp) {
        if (getTimestamp() >= timestamp) {
            JsonNodeFactory factory = JsonNodeFactory.instance;
            ObjectNode node = factory.objectNode();

            if(city != null && getCity().getId() != null)
                node.put("city", getCity().getId().toHexString());

            if(action != null)
                node.put("action", action);

            node.put("id", getId().toString());
            node.put("type", getType());
            node.put("description", getDescription());
            node.put("title", getTitle());
            node.put("timestamp", getTimestamp());

            return node;
        }

        return null;
    }
}
