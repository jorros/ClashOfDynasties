package de.clashofdynasties.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class Relation {
    @Id
    private String id;

    @DBRef(lazy = true)
    private Player player1;

    @DBRef(lazy = true)
    private Player player2;

    private int relation;

    private Player pendingRelationPlayer;

    @DBRef
    private List<Caravan> caravans;

    @DBRef
    private List<Caravan> pendingCaravans;

    private Integer pendingRelation;

    private Integer ticksLeft;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public int getRelation() {
        return relation;
    }

    public void setRelation(int relation) {
        this.relation = relation;
    }

    public List<Caravan> getCaravans() {
        return caravans;
    }

    public void setCaravans(List<Caravan> caravans) {
        this.caravans = caravans;
    }

    public List<Caravan> getPendingCaravans() {
        return pendingCaravans;
    }

    public void setPendingCaravans(List<Caravan> pendingCaravans) {
        this.pendingCaravans = pendingCaravans;
    }

    public Integer getPendingRelation() {
        return pendingRelation;
    }

    public void setPendingRelation(Integer pendingRelation) {
        this.pendingRelation = pendingRelation;
    }

    public ObjectId getOId() {
        return new ObjectId(this.id);
    }

    public Player getPendingRelationPlayer() {
        return pendingRelationPlayer;
    }

    public void setPendingRelationPlayer(Player pendingRelationPlayer) {
        this.pendingRelationPlayer = pendingRelationPlayer;
    }

    public Integer getTicksLeft() {
        return ticksLeft;
    }

    public void setTicksLeft(Integer ticksLeft) {
        this.ticksLeft = ticksLeft;
    }
}
