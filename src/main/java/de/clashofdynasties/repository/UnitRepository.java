package de.clashofdynasties.repository;

import de.clashofdynasties.models.City;
import de.clashofdynasties.models.Unit;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class UnitRepository extends Repository<Unit> {
    private static UnitRepository instance;

    @PostConstruct
    public void initialize() {
        load(Unit.class);
        instance = this;
    }

    public static UnitRepository get() {
        return instance;
    }

    public Unit findById(ObjectId id) {
        return items.stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);
    }
}
