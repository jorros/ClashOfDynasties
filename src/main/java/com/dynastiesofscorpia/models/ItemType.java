package com.dynastiesofscorpia.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ItemType {
    @Id
    private int id;

    private String name;

    private int type;

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean equals(Object other) {
        if (other instanceof ItemType && ((ItemType) other).getId() == this.id)
            return true;
        else
            return false;
    }
}
