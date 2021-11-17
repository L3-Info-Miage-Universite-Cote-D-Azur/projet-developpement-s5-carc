package logic.tile;

import logic.config.GameConfig;
import logic.config.excel.TileExcelConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Represents a stack of tiles.
 */
public class TileStack {
    private final LinkedList<Tile> tiles;

    public TileStack() {
        this.tiles = new LinkedList<>();
    }

    /**
     * Fill the stack with the given number of tiles.
     * @param tiles Tiles to fill the stack with.
     */
    public void fill(ArrayList<Tile> tiles) {
        this.tiles.addAll(tiles);
    }

    /**
     * Fill the stack with the given configuration.
     * @param config Configuration to fill the stack with.
     */
    public void fill(GameConfig config) {
        for (TileExcelConfig tileConfig : config.tiles) {
            for (int i = 0; i < tileConfig.count; i++) {
                tiles.add(tileConfig.createTile());
            }
        }
    }

    /**
     * Get the number of tiles in the stack.
     * @return Number of tiles in the stack.
     */
    public int getNumTiles() {
        return tiles.size();
    }

    /**
     * Determine if the stack is empty.
     * @return True if the stack is empty, false otherwise.
     */
    public boolean isEmpty() {
        return getNumTiles() == 0;
    }

    /**
     * Draw a tile from the stack.
     * @return Tile drawn from the stack.
     */
    public Tile remove() {
        return tiles.remove();
    }

    /**
     * Peek at the top tile in the stack. Allows the top tile to be seen without removing it.
     * @return Top tile in the stack.
     */
    public Tile peek() {
        return tiles.peek();
    }

    /**
     * Shuffle the stack. Starting tile is not shuffled.
     */
    public void shuffle() {
        Collections.shuffle(tiles);
        Tile startingTile = tiles.stream().filter(f -> f.hasFlags(TileFlags.STARTING)).findAny().orElse(null);

        if (startingTile != null) {
            tiles.remove(startingTile);
            tiles.addFirst(startingTile);
        }
    }
}