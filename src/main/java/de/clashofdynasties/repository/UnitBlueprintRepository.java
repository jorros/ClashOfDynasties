package de.clashofdynasties.repository;

import de.clashofdynasties.models.City;
import de.clashofdynasties.models.UnitBlueprint;
import org.springframework.data.mongodb.repository.MongoRepository;
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

    public synchronized UnitBlueprint findById(int id) {
        return items.stream().filter(u -> u.getId() == id).findFirst().orElse(null);
    }
}
