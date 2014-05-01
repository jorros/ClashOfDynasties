package de.clashofdynasties.models;

import org.springframework.data.mongodb.core.mapping.DBRef;

public class Party {
    @DBRef
    private Player player;
    private int losses;
    private boolean lost;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public boolean isLost() {
        return lost;
    }

    public void setLost(boolean lost) {
        this.lost = lost;
    }
}