package de.clashofdynasties.repository;

import de.clashofdynasties.models.Unit;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UnitRepository extends MongoRepository<Unit, ObjectId> {
}
