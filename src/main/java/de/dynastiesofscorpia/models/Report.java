package de.dynastiesofscorpia.models;

import java.util.ArrayList;
import java.util.List;

public class Report {
    private List<Party> parties;

    public Report() {
        parties = new ArrayList<>();
    }

    public List<Party> getParties() {
        return parties;
    }

    public void setParties(List<Party> parties) {
        this.parties = parties;
    }
}
