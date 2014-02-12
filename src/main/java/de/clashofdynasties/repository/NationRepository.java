package de.clashofdynasties.repository;

import de.clashofdynasties.models.Nation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NationRepository extends MongoRepository<Nation, Integer>
{
}
