package de.clashofdynasties.repository;

import de.clashofdynasties.models.Formation;
import de.clashofdynasties.models.Player;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface FormationRepository extends MongoRepository<Formation, Integer> {
    @Query("{lastCity.$id: ?0, route: null}")
    List<Formation> findByCity(int city);
    List<Formation> findByPlayer(Player player);
}
