package de.clashofdynasties.repository;

import de.clashofdynasties.models.Resource;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ResourceRepository extends MongoRepository<Resource, Integer>
{
}
