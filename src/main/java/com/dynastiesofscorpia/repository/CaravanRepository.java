package com.dynastiesofscorpia.repository;

import com.dynastiesofscorpia.models.Caravan;
import com.dynastiesofscorpia.models.Player;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CaravanRepository extends Repository<Caravan> {
    private static CaravanRepository instance;

    @PostConstruct
    public void initialize() {
        load(Caravan.class);
        instance = this;
    }

    public List<Caravan> findByPlayer(Player player) {
        return items.stream().filter(c -> c.getPlayer().equals(player)).collect(Collectors.toList());
    }

    public Caravan findById(ObjectId id) {
        return items.stream().filter(c -> c.getId().equals(id)).findFirst().orElse(null);
    }

    public static CaravanRepository get() {
        return instance;
    }
}
