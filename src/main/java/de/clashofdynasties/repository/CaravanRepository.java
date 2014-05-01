package de.clashofdynasties.repository;

import de.clashofdynasties.models.Caravan;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CaravanRepository extends MongoRepository<Caravan, Integer> {

}
