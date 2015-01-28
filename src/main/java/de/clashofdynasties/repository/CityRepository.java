package de.clashofdynasties.repository;

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
public class CityRepository extends Repository<City> {
    private static CityRepository instance;

    @PostConstruct
    public void initialize() {
        load(City.class);
        instance = this;
    }

    public synchronized City findById(ObjectId id) {
        return items.stream().filter(c -> c.getId().equals(id)).findFirst().orElse(null);
    }

    public synchronized List<City> findByPlayer(Player player) {
        return items.stream().filter(c -> c.getPlayer().equals(player)).collect(Collectors.toList());
    }

    public static CityRepository get() {
        return instance;
    }
}
