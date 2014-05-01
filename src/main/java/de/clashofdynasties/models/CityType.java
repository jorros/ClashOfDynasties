package de.clashofdynasties.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class CityType {
    @Id
    private int id;

    private String name;
    private float capacity;

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

    public float getCapacity() {
        return capacity;
    }

    public void setCapacity(float capacity) {
        this.capacity = capacity;
    }

    public boolean equals(Object other) {
        if (other instanceof CityType && ((CityType) other).getId() == this.id)
            return true;
        else
            return false;
    }
}
