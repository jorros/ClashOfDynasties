package de.clashofdynasties.repository;

import de.clashofdynasties.models.Player;
import de.clashofdynasties.models.Relation;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface RelationRepository extends MongoRepository<Relation, ObjectId> {
    @Query("{ $or: [ {player1.$id: ?0, player2.$id: ?1}, {player1.$id: ?1, player2.$id: ?0} ]}")
    Relation findByPlayers(ObjectId player1, ObjectId player2);
}
