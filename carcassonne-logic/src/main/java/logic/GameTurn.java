package logic;

import logic.player.Player;
import logic.tile.Tile;
import logic.tile.TileStack;
import stream.ByteInputStream;
import stream.ByteOutputStream;
import stream.ByteStreamHelper;

/**
 * This class represents a turn in the game. It determines the player who must play and the tiles that must be placed.
 */
public class GameTurn {
    private final Game game;
    private int id;

    private Tile tileDraw;

    private boolean hasPlacedTile;
    private boolean hasPlacedMeeple;
    private boolean hasTookMeeple;
    private boolean isOver;

    public GameTurn(Game game) {
        this.game = game;
        this.id = 0;
        this.isOver = true;
    }

    /**
     * Resets the turn instance to its initial state.
     */
    public void reset() {
        id = 0;
        isOver = true;
    }

    /**
     * Determines whether the turn is over.
     * @return true if the turn is over, false otherwise
     */
    public boolean isOver() {
        return isOver;
    }

    /**
     * Ends the current turn.
     */
    public void endTurn() {
        isOver = true;
        game.getListener().onTurnEnded(id);
    }

    /**
     * Plays the next turn.
     * A Tile will be drawn and the player who must play will be determined.
     * @return true if the turn was successfully started, false otherwise.
     */
    public boolean playTurn() {
        id++;
        tileDraw = drawTileToPlace();
        hasPlacedTile = false;
        hasPlacedMeeple = false;
        isOver = false;

        if (tileDraw != null) {
            game.getListener().onTurnStarted(id);
            getPlayer().getListener().play();
            return true;
        } else {
            game.onEnd();
            return false;
        }
    }

    /**
     * Gets the tile that must be placed.
     * @return the tile that must be placed
     */
    public Tile getTileToDraw() {
        return tileDraw;
    }

    /**
     * Determines whether the tile drawn during the turn was placed.
     * @return true if the tile was placed, false otherwise
     */
    public boolean hasPlacedTile() {
        return hasPlacedTile;
    }

    /**
     * Sets the tile that the tile drawn during the turn was placed.
     */
    public void setTilePlaced() {
        hasPlacedTile = true;
    }

    /**
     * Determines whether the player has placed a meeple during this turn.
     * @return
     */
    public boolean hasPlacedMeeple() {
        return hasPlacedMeeple;
    }

    public boolean hasTookMeeple(){ return hasTookMeeple();}
    /**
     * Sets the player who has placed a meeple during this turn.
     */
    public void setMeeplePlaced() {
        hasPlacedMeeple = true;
        hasTookMeeple = false;
    }

    /**
     * Draws a tile to be placed during the turn.
     * @return the tile that must be placed
     */
    private Tile drawTileToPlace() {
        TileStack stack = game.getStack();

        while (!stack.isEmpty()) {
            Tile tile = stack.remove();

            if (game.getBoard().hasFreePlaceForTile(tile)) {
                return tile;
            }
        }

        return null;
    }

    /**
     * Gets the index of the player who must play.
     * @return The index of the player who must play
     */
    public int getPlayerIndex() {
        return (id - 1) % game.getPlayerCount();
    }

    /**
     * Gets the player who must play.
     * @return The player who must play
     */
    public Player getPlayer() {
        return game.getPlayer(getPlayerIndex());
    }

    /**
     * Gets the turn id.
     * @return The turn id
     */
    public int getCount() {
        return id;
    }

    /**
     * Encodes the turn into the given output stream.
     * @param stream
     * @param game
     */
    public void encode(ByteOutputStream stream, Game game) {
        stream.writeInt(id);
        stream.writeBoolean(isOver);
        stream.writeBoolean(hasPlacedTile);
        stream.writeBoolean(hasPlacedMeeple);
        stream.writeBoolean(hasTookMeeple);

        if (tileDraw != null) {
            stream.writeBoolean(true);
            ByteStreamHelper.encodeTile(stream, tileDraw, game);
        } else {
            stream.writeBoolean(false);
        }
    }

    /**
     * Decodes the turn from the given input stream.
     * @param stream
     * @param game
     */
    public void decode(ByteInputStream stream, Game game) {
        id = stream.readInt();
        isOver = stream.readBoolean();
        hasPlacedTile = stream.readBoolean();
        hasPlacedMeeple = stream.readBoolean();
        hasTookMeeple = stream.readBoolean();

        if (stream.readBoolean()) {
            tileDraw = ByteStreamHelper.decodeTile(stream, game);
        } else {
            tileDraw = null;
        }
    }
}
