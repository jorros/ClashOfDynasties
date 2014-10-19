package de.clashofdynasties.repository;

import de.clashofdynasties.models.City;
import de.clashofdynasties.models.Player;
import de.clashofdynasties.models.Relation;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RelationRepository extends Repository<Relation> {
    private static RelationRepository instance;

    @PostConstruct
    public void initialize() {
        load(Relation.class);
        instance = this;
    }

    public static RelationRepository get() {
        return instance;
    }

    public Relation findById(ObjectId id) {
        return items.stream().filter(r -> r.getId().equals(id)).findFirst().orElse(null);
    }

    public Relation findByPlayers(Player player1, Player player2) {
        if(player1 != null && player2 != null)
            return items.stream().filter(r -> r.getPlayer1().equals(player1) && r.getPlayer2().equals(player2) || r.getPlayer2().equals(player1) && r.getPlayer1().equals(player2)).findFirst().orElse(null);
        else
            return null;
    }

    public List<Relation> finyByPlayer(Player player) {
        return items.stream().filter(r -> r.getPlayer1().equals(player) || r.getPlayer2().equals(player)).collect(Collectors.toList());
    }
}
