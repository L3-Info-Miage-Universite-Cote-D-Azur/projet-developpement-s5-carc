package logic.tile;

import logic.Game;
import logic.config.GameConfig;
import logic.config.excel.TileConfig;
import stream.ByteInputStream;
import stream.ByteOutputStream;
import stream.ByteStreamHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Represents a stack of tiles.
 */
public class TileStack {
    private final LinkedList<Tile> tiles;
    private final Game game;

    public TileStack(Game game) {
        this.tiles = new LinkedList<>();
        this.game = game;
    }

    /**
     * Clears the stack.
     */
    public void clear() {
        tiles.clear();
    }

    /**
     * Fill the stack with the given number of tiles.
     *
     * @param tiles Tiles to fill the stack with.
     */
    public void fill(ArrayList<Tile> tiles) {
        this.tiles.addAll(tiles);
    }

    /**
     * Fill the stack with the given configuration.
     *
     * @param config Configuration to fill the stack with.
     */
    public void fill(GameConfig config) {
        for (TileConfig tileConfig : config.tiles) {
            for (int i = 0; i < tileConfig.count; i++) {
                tiles.add(tileConfig.createTile(game));
            }
        }
    }

    /**
     * Get the number of tiles in the stack.
     *
     * @return Number of tiles in the stack.
     */
    public int getNumTiles() {
        return tiles.size();
    }

    /**
     * Determine if the stack is empty.
     *
     * @return True if the stack is empty, false otherwise.
     */
    public boolean isEmpty() {
        return getNumTiles() == 0;
    }

    /**
     * Draw a tile from the stack.
     *
     * @return Tile drawn from the stack.
     */
    public Tile remove() {
        return tiles.remove();
    }

    /**
     * Peek at the top tile in the stack. Allows the top tile to be seen without removing it.
     *
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
        Tile startingTile = tiles.stream().filter(f -> f.hasFlag(TileFlags.STARTING)).findAny().orElse(null);

        if (startingTile != null) {
            tiles.remove(startingTile);
            tiles.addFirst(startingTile);
        }
    }

    /**
     * Encodes the stack into a byte stream.
     *
     * @param stream Stream to encode the stack into.
     * @param game   Game to encode the stack for.
     */
    public void encode(ByteOutputStream stream, Game game) {
        stream.writeInt(tiles.size());

        for (Tile tile : tiles) {
            ByteStreamHelper.encodeTile(stream, tile, game);
        }
    }

    /**
     * Decodes the stack from a byte stream.
     *
     * @param stream Stream to decode the stack from.
     * @param game   Game to decode the stack for.
     */
    public void decode(ByteInputStream stream, Game game) {
        tiles.clear();

        int numTiles = stream.readInt();

        for (int i = 0; i < numTiles; i++) {
            tiles.add(ByteStreamHelper.decodeTile(stream, game));
        }
    }
}