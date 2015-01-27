package com.dynastiesofscorpia.models;

import com.dynastiesofscorpia.repository.BuildingBlueprintRepository;
import com.dynastiesofscorpia.repository.CityRepository;
import com.dynastiesofscorpia.repository.UnitBlueprintRepository;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.bson.types.ObjectId;

public class Objective {
    private ObjectId city;
    private Integer building;
    private Integer unit;
    private Integer population;
    private int count;
    private int countReady;
    private ObjectId conquerCity;
    private boolean completed;

    public City getCity() {
        return CityRepository.get().findById(city);
    }

    public void setCity(City city) {
        if(city != null)
            this.city = city.getId();
        else
            this.city = null;
    }

    public City getConquerCity() {
        if(conquerCity != null)
            return CityRepository.get().findById(conquerCity);
        else
            return null;
    }

    public void setConquerCity(City conquerCity) {
        if(conquerCity != null)
            this.conquerCity = conquerCity.getId();
        else
            this.conquerCity = null;
    }

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public BuildingBlueprint getBuilding() {
        if(building != null)
            return BuildingBlueprintRepository.get().findById(building);
        else
            return null;
    }

    public void setBuilding(BuildingBlueprint building) {
        if(building != null)
            this.building = building.getId();
        else
            this.building = null;
    }

    public int getCountReady() {
        return countReady;
    }

    public void setCountReady(int countReady) {
        this.countReady = countReady;
    }

    public UnitBlueprint getUnit() {
        if(unit != null)
            return UnitBlueprintRepository.get().findById(unit);
        else
            return null;
    }

    public void setUnit(UnitBlueprint unit) {
        if(unit != null)
            this.unit = unit.getId();
        else
            this.unit = null;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public ObjectNode toJSON() {
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode node = factory.objectNode();

        node.put("completed", isCompleted());

        String text = "";

        if(building != null && city != null)
            text = "Baue " + getCount() + "x " + getBuilding().getName() + " in " + getCity().getName();
        else if(unit != null && city != null)
            text = "Bilde " + getCount() + "x " + getUnit().getName() + " in " + getCity().getName() + " aus";
        else if(city != null && population != null)
            text = "Erreiche eine Population von " + population + " Bürger in " + getCity().getName();
        else if(conquerCity != null)
            text = "Erobere die Stadt " + getConquerCity().getName();

        node.put("text", text);

        return node;
    }
}
