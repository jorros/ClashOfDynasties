package de.clashofdynasties.repository;

import de.clashofdynasties.models.Building;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BuildingRepository extends MongoRepository<Building, ObjectId> {
}
