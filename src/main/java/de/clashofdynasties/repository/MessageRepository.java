package de.clashofdynasties.repository;

import de.clashofdynasties.models.Message;
import de.clashofdynasties.models.Player;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MessageRepository extends Repository<Message> {
    private static MessageRepository instance;

    @PostConstruct
    public void initialize() {
        load(Message.class);
        instance = this;
    }

    public static MessageRepository get() {
        return instance;
    }

    public Message findById(ObjectId id) {
        return items.parallelStream().filter(m -> m.getId().equals(id)).findFirst().orElse(null);
    }

    public List<Message> findByPlayers(Player p1, Player p2) {
        return items.parallelStream().filter(m -> m.getFrom().equals(p1) && m.getTo().equals(p2) || m.getFrom().equals(p2) && m.getTo().equals(p1)).collect(Collectors.toList());
    }
}
