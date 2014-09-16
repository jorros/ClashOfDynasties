package de.clashofdynasties.repository;

import de.clashofdynasties.models.City;
import de.clashofdynasties.models.ItemType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ItemTypeRepository extends Repository<ItemType> {
    private static ItemTypeRepository instance;

    @PostConstruct
    public void initialize() {
        load(ItemType.class);
        instance = this;
    }

    public static ItemTypeRepository get() {
        return instance;
    }

    public ItemType findById(int id) {
        return items.stream().filter(i -> i.getId() == id).findFirst().orElse(null);
    }
}
