package de.clashofdynasties.repository;

import de.clashofdynasties.models.Player;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlayerRepository extends MongoRepository<Player, Integer> {
    public Player findByName(String name);
}
