package logic.meeple;

import logic.player.Player;

public class Meeple {
    private final Player owner;

    public Meeple(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }
}
