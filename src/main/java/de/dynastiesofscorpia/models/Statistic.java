package de.dynastiesofscorpia.models;

public class Statistic {
    private int rank;
    private int economy;
    private int demography;
    private int military;

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getEconomy() {
        return economy;
    }

    public void setEconomy(int economy) {
        this.economy = economy;
    }

    public void addEconomy(int economy) {
        this.economy += economy;
    }

    public int getDemography() {
        return demography;
    }

    public void setDemography(int demography) {
        this.demography = demography;
    }

    public void addDemography(int demography) {
        this.demography += demography;
    }

    public int getMilitary() {
        return military;
    }

    public void setMilitary(int military) {
        this.military = military;
    }

    public void addMilitary(int military) {
        this.military += military;
    }

    public int getTotal() {
        return this.economy + this.demography + this.military;
    }
}
