package de.clashofdynasties.models;

import de.clashofdynasties.repository.CaravanRepository;
import de.clashofdynasties.repository.PlayerRepository;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Document
public class Relation {
    @Id
    private ObjectId id;

    private ObjectId player1;

    private ObjectId player2;

    private int relation;

    private ObjectId pendingRelationPlayer;

    private List<ObjectId> caravans;

    private List<ObjectId> pendingCaravans;

    private Integer pendingRelation;

    private Integer ticksLeft;

    public Relation() {
        this.id = new ObjectId();
        caravans = new ArrayList<>();
        pendingCaravans = new ArrayList<>();
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Player getPlayer1() {
        return PlayerRepository.get().findById(player1);
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1.getId();
    }

    public Player getPlayer2() {
        return PlayerRepository.get().findById(player2);
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2.getId();
    }

    public int getRelation() {
        return relation;
    }

    public void setRelation(int relation) {
        this.relation = relation;
    }

    public List<Caravan> getCaravans() {
        return caravans.stream().map(c -> CaravanRepository.get().findById(c)).collect(Collectors.toList());
    }

    public void addCaravan(Caravan caravan) {
        caravans.add(caravan.getId());
    }

    public void removeCaravan(Caravan caravan) {
        caravans.remove(caravan.getId());
    }

    public List<Caravan> getPendingCaravans() {
        return pendingCaravans.stream().map(c -> CaravanRepository.get().findById(c)).collect(Collectors.toList());
    }

    public void addPendingCaravan(Caravan caravan) {
        pendingCaravans.add(caravan.getId());
    }

    public void removePendingCaravan(Caravan caravan) {
        pendingCaravans.remove(caravan.getId());
    }

    public Integer getPendingRelation() {
        return pendingRelation;
    }

    public void setPendingRelation(Integer pendingRelation) {
        this.pendingRelation = pendingRelation;
    }

    public Player getPendingRelationPlayer() {
        return PlayerRepository.get().findById(pendingRelationPlayer);
    }

    public void setPendingRelationPlayer(Player pendingRelationPlayer) {
        if(pendingRelationPlayer != null)
            this.pendingRelationPlayer = pendingRelationPlayer.getId();
        else
            this.pendingRelationPlayer = null;
    }

    public Integer getTicksLeft() {
        return ticksLeft;
    }

    public void setTicksLeft(Integer ticksLeft) {
        this.ticksLeft = ticksLeft;
    }
}
