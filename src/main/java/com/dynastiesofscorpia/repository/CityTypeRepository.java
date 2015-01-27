package com.dynastiesofscorpia.repository;

import com.dynastiesofscorpia.models.CityType;
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

    public CityType findById(int id) {
        return items.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
    }
}
