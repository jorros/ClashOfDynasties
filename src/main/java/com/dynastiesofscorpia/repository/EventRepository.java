package com.dynastiesofscorpia.repository;

import com.dynastiesofscorpia.models.Event;
import com.dynastiesofscorpia.models.Player;
import com.dynastiesofscorpia.service.MailService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void add(Event item) {
        super.add(item);
        if(item.getPlayer().hasNotification(item.getType()))
            mailService.sendMail(item.getPlayer(), item.getTitle(), item.getDescription());
    }

    public Event findById(ObjectId id) {
        return items.stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null);
    }

    public List<Event> findByPlayer(Player player) {
        return items.stream().filter(e -> e.getPlayer().equals(player)).collect(Collectors.toList());
    }
}
