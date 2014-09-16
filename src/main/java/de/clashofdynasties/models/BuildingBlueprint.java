package de.clashofdynasties.models;

import de.clashofdynasties.repository.BiomeRepository;
import de.clashofdynasties.repository.ItemRepository;
import de.clashofdynasties.repository.NationRepository;
import de.clashofdynasties.repository.ResourceRepository;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Document
public class BuildingBlueprint implements IBlueprint {
    @Id
    private int id;

    private int price;

    private int requiredProduction;

    private int nation;

    private int requiredResource;

    private String name;

    private String description;

    private int defencePoints;

    private int maxCount;

    private int produceItem;

    private double producePerStep;

    private List<Integer> requiredBiomes;

    public BuildingBlueprint() {
        requiredBiomes = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getRequiredProduction() {
        return requiredProduction;
    }

    public void setRequiredProduction(int requiredProduction) {
        this.requiredProduction = requiredProduction;
    }

    public Nation getNation() {
        return NationRepository.get().findById(nation);
    }

    public void setNation(Nation nation) {
        this.nation = nation.getId();
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public Resource getRequiredResource() {
        return ResourceRepository.get().findById(requiredResource);
    }

    public void setRequiredResource(Resource requiredResource) {
        this.requiredResource = requiredResource.getId();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Item getProduceItem() {
        return ItemRepository.get().findById(produceItem);
    }

    public void setProduceItem(Item produceItem) {
        this.produceItem = produceItem.getId();
    }

    public double getProducePerStep() {
        return producePerStep;
    }

    public void setProducePerStep(double producePerStep) {
        this.producePerStep = producePerStep;
    }

    public int getDefencePoints() {
        return defencePoints;
    }

    public void setDefencePoints(int defencePoints) {
        this.defencePoints = defencePoints;
    }

    public List<Biome> getRequiredBiomes() {
        return requiredBiomes.stream().map(b -> BiomeRepository.get().findById(b)).collect(Collectors.toList());
    }

    public void addRequiredBiome(Biome biome) {
        requiredBiomes.add(biome.getId());
    }

    public boolean equals(Object other) {
        if (other instanceof BuildingBlueprint && ((BuildingBlueprint) other).getId() == this.id)
            return true;
        else
            return false;
    }
}
