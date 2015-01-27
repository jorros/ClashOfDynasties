package com.dynastiesofscorpia.repository;

import com.dynastiesofscorpia.models.ItemType;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ItemTypeRepository extends Repository<ItemType> {
    private static ItemTypeRepository instance;

    @PostConstruct
    public void initialize() {
        load(ItemType.class);
        instance = this;
    }

    public static ItemTypeRepository get() {
        return instance;
    }

    public ItemType findById(int id) {
        return items.stream().filter(i -> i.getId() == id).findFirst().orElse(null);
    }
}
