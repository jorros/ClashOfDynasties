package com.dynastiesofscorpia.repository;

import com.dynastiesofscorpia.models.BuildingBlueprint;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class BuildingBlueprintRepository extends Repository<BuildingBlueprint> {
    private static BuildingBlueprintRepository instance;

    @PostConstruct
    public void initialize() {
        load(BuildingBlueprint.class);
        instance = this;
    }

    public static BuildingBlueprintRepository get() {
        return instance;
    }

    public BuildingBlueprint findById(int id) {
        return items.stream().filter(b -> b.getId() == id).findFirst().orElse(null);
    }
}
