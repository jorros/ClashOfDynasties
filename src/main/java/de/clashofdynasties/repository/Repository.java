package de.clashofdynasties.repository;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class Repository<K>{
    protected List<K> items;

    @Autowired
    private MongoTemplate mongoTemplate;

    public void load(Class<K> clazz) {
        items = Collections.synchronizedList(mongoTemplate.findAll(clazz));
    }

    public synchronized void remove(K item) {
        if(items.contains(item)) {
            mongoTemplate.remove(item);
            items.remove(item);
        }
    }

    public synchronized void remove(List<K> item) {
        if(items.containsAll(item)) {
            for(K removable : item) {
                mongoTemplate.remove(removable);
            }
            items.removeAll(item);
        }
    }

    public synchronized void add(K item) {
        mongoTemplate.insert(item);
        items.add(item);
    }

    public synchronized List<K> getList() {
        return items;
    }

    public void save() {
        items.forEach(mongoTemplate::save);
    }
}
