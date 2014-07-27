package de.clashofdynasties.repository;

import de.clashofdynasties.models.Event;
import de.clashofdynasties.models.Player;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EventRepository extends MongoRepository<Event, String> {
    List<Event> findByPlayer(Player player);
}
