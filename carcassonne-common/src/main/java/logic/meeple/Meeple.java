package logic.meeple;

import logic.player.Player;

/**
 * The meeple class that represents a meeple on the tile.
 */
public record Meeple(Player owner) {

    /**
     * Gets the owner of the meeple.
     *
     * @return the owner of the meeple.
     */
    public Player getOwner() {
        return owner;
    }
}
