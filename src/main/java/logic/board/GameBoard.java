package logic.board;

import logic.math.Vector2;
import logic.tile.Tile;

import java.util.HashMap;

public class GameBoard {
    private final HashMap<Vector2, Tile> tiles;

    public GameBoard(){
        this.tiles = new HashMap<>();
    }

    public void place(Tile tile) {
        if (hasTileAt(tile.getPosition())) {
            throw new IllegalArgumentException("Try to place a tile on another.");
        }

        tiles.put(tile.getPosition(), tile);
    }

    public Tile getTileAt(Vector2 position) {
        return tiles.getOrDefault(position, null);
    }

    public boolean hasTileAt(Vector2 position){
        return tiles.containsKey(position);
    }
}
