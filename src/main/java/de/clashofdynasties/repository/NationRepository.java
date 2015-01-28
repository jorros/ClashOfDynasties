package de.clashofdynasties.repository;

import de.clashofdynasties.models.City;
import de.clashofdynasties.models.Nation;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class NationRepository extends Repository<Nation> {
    private static NationRepository instance;

    @PostConstruct
    public void initialize() {
        load(Nation.class);
        instance = this;
    }

    public static NationRepository get() {
        return instance;
    }

    public synchronized Nation findById(int id) {
        return items.stream().filter(n -> n.getId() == id).findFirst().orElse(null);
    }
}
