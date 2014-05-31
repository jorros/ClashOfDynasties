package de.clashofdynasties.repository;

import de.clashofdynasties.models.Clan;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClanRepository extends MongoRepository<Clan, Integer> {
}
