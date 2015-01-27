package com.dynastiesofscorpia.repository;

import com.dynastiesofscorpia.models.Resource;
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

    public Resource findById(int id) {
        return items.stream().filter(r -> r.getId() == id).findFirst().orElse(null);
    }
}
