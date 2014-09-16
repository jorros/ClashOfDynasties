package de.clashofdynasties.repository;

import de.clashofdynasties.models.City;
import de.clashofdynasties.models.Formation;
import de.clashofdynasties.models.Player;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FormationRepository extends Repository<Formation> {
    private static FormationRepository instance;

    @PostConstruct
    public void initialize() {
        load(Formation.class);
        instance = this;
    }

    public static FormationRepository get() {
        return instance;
    }

    public List<Formation> findByCity(City city) {
        return items.parallelStream().filter(f -> f.getLastCity().equals(city) && f.getRoute() == null).collect(Collectors.toList());
    }

    public List<Formation> findByPlayer(Player player) {
        return items.stream().filter(f -> f.getPlayer().equals(player)).collect(Collectors.toList());
    }

    public Formation findById(ObjectId id) {
        return items.parallelStream().filter(f -> f.getId().equals(id)).findFirst().orElse(null);
    }
}
