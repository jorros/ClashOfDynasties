package de.clashofdynasties.repository;

import de.clashofdynasties.models.Formation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FormationRepository extends MongoRepository<Formation, Integer>
{
}
