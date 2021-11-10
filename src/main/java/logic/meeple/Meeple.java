package logic.meeple;

import logic.player.PlayerBase;

public class Meeple {
    private final PlayerBase owner;

    public Meeple(PlayerBase owner) {
        this.owner = owner;
    }

    public PlayerBase getOwner() {
        return owner;
    }
}
