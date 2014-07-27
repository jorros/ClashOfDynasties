package de.clashofdynasties.repository;

import de.clashofdynasties.models.Event;
import de.clashofdynasties.models.Player;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EventRepository extends MongoRepository<Event, ObjectId> {
    List<Event> findByPlayer(Player player);
}
