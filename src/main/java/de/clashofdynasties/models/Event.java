package de.clashofdynasties.models;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Event {
    @Id
    String id;

    @DBRef(lazy = true)
    private City city;

    @DBRef
    private Player player;

    private String action;
    private String type;
    private String description;
    private String title;
    private long timestamp;

    public Event() {

    }

    public Event(String type, String title, String description, String action) {
        this.action = action;
        this.type = type;
        this.title = title;
        this.description = description;

        updateTimestamp();
    }

    public Event(String type, String title, String description, City city) {
        this.type = type;
        this.title = title;
        this.description = description;
        this.city = city;

        updateTimestamp();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public City getCity() {
        return city;
    }



    public void setCity(City city) {
        this.city = city;
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
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public ObjectNode toJSON(long timestamp) {
        if (getTimestamp() >= timestamp) {
            JsonNodeFactory factory = JsonNodeFactory.instance;
            ObjectNode node = factory.objectNode();

            if(city != null)
                node.put("city", getCity().getId());

            if(action != null)
                node.put("action", action);

            node.put("id", getId());
            node.put("type", getType());
            node.put("description", getDescription());
            node.put("title", getTitle());
            node.put("timestamp", getTimestamp());

            return node;
        }

        return null;
    }
}
