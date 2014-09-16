package de.clashofdynasties.repository;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.List;

public class Repository<K>{
    protected List<K> items;

    @Autowired
    private MongoTemplate mongoTemplate;

    public void load(Class<K> clazz) {
        items = mongoTemplate.findAll(clazz);
    }

    public void remove(K item) {
        if(items.contains(item)) {
            mongoTemplate.remove(item);
            items.remove(item);
        }
    }

    public void remove(List<K> item) {
        if(items.containsAll(item)) {
            mongoTemplate.remove(item);
            items.remove(item);
        }
    }

    public void add(K item) {
        mongoTemplate.insert(item);
        items.add(item);
    }

    public List<K> getList() {
        return items;
    }

    public void save() {
        items.forEach(mongoTemplate::save);
    }
}
