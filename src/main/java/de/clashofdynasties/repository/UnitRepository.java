package de.clashofdynasties.repository;

import de.clashofdynasties.models.Unit;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UnitRepository extends MongoRepository<Unit, String> {
}
