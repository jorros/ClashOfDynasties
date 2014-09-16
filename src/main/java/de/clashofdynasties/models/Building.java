package de.clashofdynasties.models;

import de.clashofdynasties.repository.BuildingBlueprintRepository;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Building {
    @Id
    private ObjectId id;

    private int blueprint;

    private int health;

    public Building() {
        id = new ObjectId();
    }

    public BuildingBlueprint getBlueprint() {
        return BuildingBlueprintRepository.get().findById(blueprint);
    }

    public void setBlueprint(BuildingBlueprint blueprint) {
        this.blueprint = blueprint.getId();
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }
}
