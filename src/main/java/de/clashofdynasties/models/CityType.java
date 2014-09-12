package de.clashofdynasties.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class CityType {
    @Id
    private int id;

    private String name;
    private double capacity;
    private double consumeBasic;
    private double consumeLuxury1;
    private double consumeLuxury2;
    private double consumeLuxury3;
    private double taxes;
    private double productionRate;

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

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public double getConsumeBasic() {
        return consumeBasic;
    }

    public void setConsumeBasic(double consumeBasic) {
        this.consumeBasic = consumeBasic;
    }

    public double getTaxes() {
        return taxes;
    }

    public void setTaxes(double taxes) {
        this.taxes = taxes;
    }

    public double getConsumeLuxury1() {
        return consumeLuxury1;
    }

    public void setConsumeLuxury1(double consumeLuxury1) {
        this.consumeLuxury1 = consumeLuxury1;
    }

    public double getConsumeLuxury2() {
        return consumeLuxury2;
    }

    public void setConsumeLuxury2(double consumeLuxury2) {
        this.consumeLuxury2 = consumeLuxury2;
    }

    public double getConsumeLuxury3() {
        return consumeLuxury3;
    }

    public void setConsumeLuxury3(double consumeLuxury3) {
        this.consumeLuxury3 = consumeLuxury3;
    }

    public double getProductionRate() {
        return productionRate;
    }

    public void setProductionRate(double productionRate) {
        this.productionRate = productionRate;
    }

    public boolean equals(Object other) {
        if (other instanceof CityType && ((CityType) other).getId() == this.id)
            return true;
        else
            return false;
    }
}
