package de.clashofdynasties.repository;

import de.clashofdynasties.models.City;
import de.clashofdynasties.models.Event;
import de.clashofdynasties.models.Player;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EventRepository extends Repository<Event> {
    private static EventRepository instance;

    @PostConstruct
    public void initialize() {
        load(Event.class);
        instance = this;
    }

    public static EventRepository get() {
        return instance;
    }

    public Event findById(ObjectId id) {
        return items.parallelStream().filter(e -> e.getId().equals(id)).findFirst().orElse(null);
    }

    public List<Event> findByPlayer(Player player) {
        return items.parallelStream().filter(e -> e.getPlayer().equals(player)).collect(Collectors.toList());
    }
}
