package de.clashofdynasties.repository;

import de.clashofdynasties.models.Item;
import de.clashofdynasties.models.ItemType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ItemRepository extends MongoRepository<Item, Integer> {
    List<Item> findByType(ItemType itemType);
}
