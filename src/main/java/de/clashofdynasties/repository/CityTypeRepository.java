package de.clashofdynasties.repository;

import de.clashofdynasties.models.CityType;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CityTypeRepository extends MongoRepository<CityType, Integer>
{
}
