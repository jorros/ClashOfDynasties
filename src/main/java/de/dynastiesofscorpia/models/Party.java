package de.dynastiesofscorpia.models;

import de.dynastiesofscorpia.repository.PlayerRepository;
import org.bson.types.ObjectId;

public class Party {
    private ObjectId player;
    private int losses;
    private boolean lost;

    public Player getPlayer() {
        return PlayerRepository.get().findById(player);
    }

    public void setPlayer(Player player) {
        this.player = player.getId();
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