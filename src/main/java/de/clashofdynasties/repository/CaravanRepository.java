package de.clashofdynasties.repository;

import de.clashofdynasties.models.Caravan;
import de.clashofdynasties.models.Player;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CaravanRepository extends MongoRepository<Caravan, ObjectId> {
    List<Caravan> findByPlayer(Player player);
}
