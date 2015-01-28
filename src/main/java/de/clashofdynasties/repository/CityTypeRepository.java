package de.clashofdynasties.repository;

import de.clashofdynasties.models.City;
import de.clashofdynasties.models.CityType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class CityTypeRepository extends Repository<CityType> {
    private static CityTypeRepository instance;

    @PostConstruct
    public void initialize() {
        load(CityType.class);
        instance = this;
    }

    public static CityTypeRepository get() {
        return instance;
    }

    public synchronized CityType findById(int id) {
        return items.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
    }
}
