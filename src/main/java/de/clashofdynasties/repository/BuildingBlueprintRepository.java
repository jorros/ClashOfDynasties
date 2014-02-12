package de.clashofdynasties.repository;

import de.clashofdynasties.models.BuildingBlueprint;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BuildingBlueprintRepository extends MongoRepository<BuildingBlueprint, Integer>
{
}
