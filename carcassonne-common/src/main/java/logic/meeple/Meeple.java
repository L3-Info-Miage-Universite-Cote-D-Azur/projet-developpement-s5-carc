package logic.meeple;

import logic.player.Player;

/**
 * The meeple class that represents a meeple on the tile.
 */
public class Meeple {
    private final Player owner;

    public Meeple(Player owner) {
        this.owner = owner;
    }

    /**
     * Gets the owner of the meeple.
     * @return the owner of the meeple.
     */
    public Player getOwner() {
        return owner;
    }
}
