package de.clashofdynasties.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Building {
    @Id
    private String id;

    @DBRef
    private BuildingBlueprint blueprint;

    private int health;

    public BuildingBlueprint getBlueprint() {
        return blueprint;
    }

    public void setBlueprint(BuildingBlueprint blueprint) {
        this.blueprint = blueprint;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ObjectId getOId() {
        return new ObjectId(this.id);
    }
}
