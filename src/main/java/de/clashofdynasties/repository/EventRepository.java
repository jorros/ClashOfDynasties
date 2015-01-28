package de.clashofdynasties.repository;

import de.clashofdynasties.models.City;
import de.clashofdynasties.models.Event;
import de.clashofdynasties.models.Player;
import de.clashofdynasties.service.MailService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EventRepository extends Repository<Event> {
    private static EventRepository instance;

    @Autowired
    private MailService mailService;

    @PostConstruct
    public void initialize() {
        load(Event.class);
        instance = this;
    }

    public static EventRepository get() {
        return instance;
    }

    @Override
    public synchronized void add(Event item) {
        super.add(item);
        if(item.getPlayer().hasNotification(item.getType()))
            mailService.sendMail(item.getPlayer(), item.getTitle(), item.getDescription());
    }

    public synchronized Event findById(ObjectId id) {
        return items.stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null);
    }

    public synchronized List<Event> findByPlayer(Player player) {
        return items.stream().filter(e -> e.getPlayer().equals(player)).collect(Collectors.toList());
    }
}
