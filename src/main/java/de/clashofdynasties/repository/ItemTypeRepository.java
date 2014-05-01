package de.clashofdynasties.repository;

import de.clashofdynasties.models.ItemType;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ItemTypeRepository extends MongoRepository<ItemType, Integer> {
}
