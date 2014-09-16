package de.clashofdynasties.repository;

import de.clashofdynasties.models.Biome;
import de.clashofdynasties.models.City;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class BiomeRepository extends Repository<Biome> {
    private static BiomeRepository instance;

    @PostConstruct
    public void initialize() {
        load(Biome.class);
        instance = this;
    }

    public static BiomeRepository get() {
        return instance;
    }

    public Biome findById(int id) {
        return items.stream().filter(b -> b.getId() == id).findFirst().orElse(null);
    }
}
