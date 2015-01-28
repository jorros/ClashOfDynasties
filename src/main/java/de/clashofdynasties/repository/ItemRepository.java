package de.clashofdynasties.repository;

import de.clashofdynasties.models.City;
import de.clashofdynasties.models.Item;
import de.clashofdynasties.models.ItemType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemRepository extends Repository<Item> {
    private static ItemRepository instance;

    @PostConstruct
    public void initialize() {
        load(Item.class);
        instance = this;
    }

    public static ItemRepository get() {
        return instance;
    }

    public synchronized Item findById(int id) {
        return items.stream().filter(i -> i.getId() == id).findFirst().orElse(null);
    }

    public synchronized List<Item> findByType(ItemType itemType) {
        return items.stream().filter(i -> i.getType().equals(itemType)).collect(Collectors.toList());
    }
}
