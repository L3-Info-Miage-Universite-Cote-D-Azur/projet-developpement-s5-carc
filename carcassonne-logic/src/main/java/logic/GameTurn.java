package logic;

import logic.player.Player;
import logic.tile.Tile;
import logic.tile.TileStack;

public class GameTurn {
    private final Game game;
    private int id;

    private Tile tileDraw;

    private boolean hasPlacedTile;
    private boolean hasPlacedMeeple;
    private boolean isOver;

    public GameTurn(Game game) {
        this.game = game;
        this.id = 0;
        this.isOver = true;
    }

    public void reset() {
        id = 0;
    }

    public boolean isOver() {
        return isOver;
    }

    public void endTurn() {
        isOver = true;
        game.getListener().onTurnEnded(id);
    }

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
        }

        return false;
    }

    public Tile getTileToDraw() {
        return tileDraw;
    }

    public boolean hasPlacedTile() {
        return hasPlacedTile;
    }

    public void setTilePlaced() {
        hasPlacedTile = true;
    }

    public boolean hasPlacedMeeple() {
        return hasPlacedMeeple;
    }

    public void setMeeplePlaced() {
        hasPlacedMeeple = true;
    }

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

    public int getPlayerIndex() {
        return (id - 1) % game.getPlayerCount();
    }

    public Player getPlayer() {
        return game.getPlayer(getPlayerIndex());
    }

    public int getCount() {
        return id;
    }
}
