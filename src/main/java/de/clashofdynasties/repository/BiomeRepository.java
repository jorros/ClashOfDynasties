package de.clashofdynasties.repository;

import de.clashofdynasties.models.Biome;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BiomeRepository extends MongoRepository<Biome, Integer>
{
}
