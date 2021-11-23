package logic.tile.chunk;

import logic.tile.Tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ChunkArea {
    private final HashSet<Chunk> chunks;
    private final HashSet<Tile> tiles;
    private boolean completed;

    public ChunkArea() {
        chunks = new HashSet<>();
        tiles = new HashSet<>();
        completed = false;
    }

    /**
     * Adds a chunk to the area.
     * It will also add all the tiles in the chunk to the area.
     * @param chunk The chunk to add.
     */
    public void addChunk(Chunk chunk) {
        if (chunk.getArea() != null) {
            throw new IllegalStateException("Chunk already has an area. Are you trying to merge two area?");
        }
        if (chunks.contains(chunk)) {
            throw new IllegalStateException("Chunk already in area.");
        }

        chunks.add(chunk);
        tiles.add(chunk.getParent());
    }

    /**
     * Merges two areas together.
     * @param other The other area to merge with.
     */
    public void merge(ChunkArea other) {
        chunks.addAll(other.chunks);
        tiles.addAll(other.tiles);
    }
}
