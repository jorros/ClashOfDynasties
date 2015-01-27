package de.dynastiesofscorpia.repository;

import de.dynastiesofscorpia.models.Building;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class BuildingRepository extends Repository<Building> {
    private static BuildingRepository instance;

    @PostConstruct
    public void initialize() {
        load(Building.class);
        instance = this;
    }

    public static BuildingRepository get() {
        return instance;
    }

    public Building findById(ObjectId id) {
        return items.stream().filter(b -> b.getId().equals(id)).findFirst().orElse(null);
    }
}
