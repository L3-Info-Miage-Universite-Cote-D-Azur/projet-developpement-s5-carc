package logic.board;

import logic.math.Vector2;
import logic.tile.Tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GameBoard {
    public static final Vector2 STARTING_TILE_POSITION = new Vector2(0, 0);

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
    public Set<Map.Entry<Vector2, Tile>> getTiles() {
        return tiles.entrySet();
    }

    public boolean hasTileAt(Vector2 position){
        return tiles.containsKey(position);
    }
    public boolean isEmpty() {
        return tiles.size() == 0;
    }
}
