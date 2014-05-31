package de.clashofdynasties.repository;

import de.clashofdynasties.models.Clan;
import de.clashofdynasties.models.Player;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PlayerRepository extends MongoRepository<Player, Integer> {
    public Player findByName(String name);
    public Player findByNameIgnoreCase(String name);
    public List<Player> findByClan(Clan clan);
}
