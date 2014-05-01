package de.clashofdynasties.models;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.data.mongodb.core.mapping.DBRef;

public class Event {
    @DBRef(lazy = true)
    private City city;

    private String type;
    private String description;
    private String title;
    private long timestamp;

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

    public ObjectNode toJSON(long timestamp) {
        if (getTimestamp() >= timestamp) {
            JsonNodeFactory factory = JsonNodeFactory.instance;
            ObjectNode node = factory.objectNode();

            node.put("city", getCity().getId());

            node.put("type", getType());
            node.put("description", getDescription());
            node.put("title", getTitle());
            node.put("timestamp", getTimestamp());

            return node;
        }

        return null;
    }
}
