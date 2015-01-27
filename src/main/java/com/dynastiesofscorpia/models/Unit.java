package com.dynastiesofscorpia.models;

import com.dynastiesofscorpia.repository.UnitBlueprintRepository;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Unit {
    @Id
    private ObjectId id;

    private int blueprint;

    private int health;

    public Unit() {
        this.id = new ObjectId();
    }

    public UnitBlueprint getBlueprint() {
        return UnitBlueprintRepository.get().findById(blueprint);
    }

    public void setBlueprint(UnitBlueprint blueprint) {
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

    public double getSpeed() {
        double speed = getBlueprint().getSpeed();

        return speed / 2 + (speed / 2) * (health / 100);
    }

    public boolean equals(Object other) {
        if (other instanceof Unit && ((Unit) other).getId().equals(this.id))
            return true;
        else
            return false;
    }
}
