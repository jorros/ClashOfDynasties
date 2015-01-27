package com.dynastiesofscorpia.repository;

import com.dynastiesofscorpia.models.UnitBlueprint;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class UnitBlueprintRepository extends Repository<UnitBlueprint> {
    private static UnitBlueprintRepository instance;

    @PostConstruct
    public void initialize() {
        load(UnitBlueprint.class);
        instance = this;
    }

    public static UnitBlueprintRepository get() {
        return instance;
    }

    public UnitBlueprint findById(int id) {
        return items.stream().filter(u -> u.getId() == id).findFirst().orElse(null);
    }
}
