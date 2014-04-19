package de.clashofdynasties.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

public class Report
{
    private List<Party> parties;

    public List<Party> getParties()
    {
        return parties;
    }

    public void setParties(List<Party> parties)
    {
        this.parties = parties;
    }
}
