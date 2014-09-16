package de.clashofdynasties.models;

import de.clashofdynasties.repository.ItemTypeRepository;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Item {
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

    public ItemType getType() {
        return ItemTypeRepository.get().findById(type);
    }

    public void setType(ItemType type) {
        this.type = type.getId();
    }

    public boolean equals(Object other) {
        if (other instanceof Item && ((Item) other).getId() == this.id)
            return true;
        else
            return false;
    }
}
