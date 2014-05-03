package de.clashofdynasties.models;

public interface IBlueprint {
    String getName();
    void setName(String name);
    int getPrice();
    void setPrice(int price);
    Nation getNation();
    void setNation(Nation nation);
    int getRequiredProduction();
    void setRequiredProduction(int requiredProduction);
}
