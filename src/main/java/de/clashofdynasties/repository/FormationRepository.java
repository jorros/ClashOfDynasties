package de.clashofdynasties.repository;

import de.clashofdynasties.models.Formation;
import de.clashofdynasties.models.Player;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface FormationRepository extends MongoRepository<Formation, ObjectId> {
    @Query("{lastCity.$id: ?0, route: null}")
    List<Formation> findByCity(ObjectId city);
    List<Formation> findByPlayer(Player player);
}
