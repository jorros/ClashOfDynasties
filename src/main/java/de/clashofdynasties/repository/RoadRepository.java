package de.clashofdynasties.repository;

import de.clashofdynasties.models.Road;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface RoadRepository extends MongoRepository<Road, String> {
    @Query("{ $or: [ {point1.$id: ?0, point2.$id: ?1}, {point1.$id: ?1, point2.$id: ?0} ]}")
    Road findByCities(String city1, String city2);

    @Query("{ $or: [ {point1.$id: ?0}, {point2.$id: ?0} ]}")
    List<Road> findByCity(String city);
}
