package com.dynastiesofscorpia.repository;

import com.dynastiesofscorpia.models.Nation;
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

    public Nation findById(int id) {
        return items.stream().filter(n -> n.getId() == id).findFirst().orElse(null);
    }
}
