package de.clashofdynasties.repository;

import de.clashofdynasties.models.Caravan;
import de.clashofdynasties.models.City;
import de.clashofdynasties.models.Player;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Component
public class CaravanRepository extends Repository<Caravan> {
    private static CaravanRepository instance;

    @PostConstruct
    public void initialize() {
        load(Caravan.class);
        instance = this;
    }

    public List<Caravan> findByPlayer(Player player) {
        return items.parallelStream().filter(c -> c.getPlayer().equals(player)).collect(Collectors.toList());
    }

    public Caravan findById(ObjectId id) {
        return items.parallelStream().filter(c -> c.getId().equals(id)).findFirst().orElse(null);
    }

    public static CaravanRepository get() {
        return instance;
    }
}
