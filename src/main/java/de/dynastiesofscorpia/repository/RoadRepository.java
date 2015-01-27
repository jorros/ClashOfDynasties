package de.dynastiesofscorpia.repository;

import de.dynastiesofscorpia.models.City;
import de.dynastiesofscorpia.models.Road;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoadRepository extends Repository<Road> {
    private static RoadRepository instance;

    @PostConstruct
    public void initialize() {
        load(Road.class);
        instance = this;
    }

    public static RoadRepository get() {
        return instance;
    }

    public Road findById(ObjectId id) {
        return items.stream().filter(r -> r.getId().equals(id)).findFirst().orElse(null);
    }

    public Road findByCities(City city1, City city2) {
        return items.stream().filter(r -> r.getPoint1().equals(city1) && r.getPoint2().equals(city2) || r.getPoint2().equals(city1) && r.getPoint1().equals(city2)).findFirst().orElse(null);
    }

    public List<Road> findByCity(City city) {
        return items.stream().filter(r -> r.getPoint1().equals(city) || r.getPoint2().equals(city)).collect(Collectors.toList());
    }
}
