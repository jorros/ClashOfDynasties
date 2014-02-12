package de.clashofdynasties.repository;

import de.clashofdynasties.models.UnitBlueprint;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UnitBlueprintRepository extends MongoRepository<UnitBlueprint, Integer>
{
}
