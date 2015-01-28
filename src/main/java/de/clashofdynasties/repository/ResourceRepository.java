package de.clashofdynasties.repository;

import de.clashofdynasties.models.City;
import de.clashofdynasties.models.Resource;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ResourceRepository extends Repository<Resource> {
    private static ResourceRepository instance;

    @PostConstruct
    public void initialize() {
        load(Resource.class);
        instance = this;
    }

    public static ResourceRepository get() {
        return instance;
    }

    public synchronized Resource findById(int id) {
        return items.stream().filter(r -> r.getId() == id).findFirst().orElse(null);
    }
}
