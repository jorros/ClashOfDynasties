package de.clashofdynasties.repository;

import de.clashofdynasties.models.City;
import de.clashofdynasties.models.Player;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.stream.Collectors;

@Component
public class PlayerRepository extends Repository<Player> {
    private static PlayerRepository instance;

    @PostConstruct
    public void initialize() {
        load(Player.class);
        instance = this;
    }

    public static PlayerRepository get() {
        return instance;
    }

    public Player findById(ObjectId id) {
        return items.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
    }

    public Player findByName(String name) {
        return items.stream().filter(p -> p.getName().equals(name)).findFirst().orElse(null);
    }

    public Player findByNameIgnoreCase(String name) {
        return items.stream().filter(p -> p.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public boolean exists(ObjectId id) {
        return items.stream().filter(p -> p.getId().equals(id)).count() > 0;
    }

    public Player findComputer() {
        return items.stream().filter(Player::isComputer).findFirst().orElse(null);
    }
}
