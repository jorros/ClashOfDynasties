package de.clashofdynasties.repository;

import de.clashofdynasties.models.Item;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ItemRepository extends MongoRepository<Item, Integer>
{
}
