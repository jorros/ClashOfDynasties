package de.clashofdynasties.repository;

import de.clashofdynasties.models.Player;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PlayerRepository extends MongoRepository<Player, ObjectId> {
    public Player findByName(String name);
    public Player findByNameIgnoreCase(String name);
}
