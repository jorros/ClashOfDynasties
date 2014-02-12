package de.clashofdynasties.repository;

import de.clashofdynasties.models.City;
import de.clashofdynasties.models.Player;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CityRepository extends MongoRepository<City, Integer>
{
    List<City> findByPlayer(Player player);
}
