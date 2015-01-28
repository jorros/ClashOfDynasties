package de.clashofdynasties.repository;

import de.clashofdynasties.models.Biome;
import de.clashofdynasties.models.BuildingBlueprint;
import org.springframework.data.mongodb.repository.MongoRepository;
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

    public synchronized BuildingBlueprint findById(int id) {
        return items.stream().filter(b -> b.getId() == id).findFirst().orElse(null);
    }
}
