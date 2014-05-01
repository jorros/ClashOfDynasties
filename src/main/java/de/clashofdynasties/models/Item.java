package de.clashofdynasties.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Item {
    @Id
    private int id;

    private String name;

    @DBRef
    private ItemType type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public boolean equals(Object other) {
        if (other instanceof Item && ((Item) other).getId() == this.id)
            return true;
        else
            return false;
    }
}
