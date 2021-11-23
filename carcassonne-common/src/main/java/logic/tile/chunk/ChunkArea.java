package logic.tile.chunk;

import logic.tile.Tile;

import java.util.HashSet;

/**
 * Represents a chunk area.
 * It contains the list of chunks in the area.
 */
public class ChunkArea {
    private final HashSet<Chunk> chunks;
    private final HashSet<Tile> tiles;
    private boolean closed;

    public ChunkArea() {
        chunks = new HashSet<>();
        tiles = new HashSet<>();
        closed = false;
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
        chunk.setArea(this);
        checkIfClosed();
    }

    /**
     * Merges two areas together.
     * @param other The other area to merge with.
     */
    public void merge(ChunkArea other) {
        chunks.addAll(other.chunks);
        tiles.addAll(other.tiles);

        for (Chunk chunk : other.chunks) {
            chunk.setArea(this);
        }

        checkIfClosed();
    }

    /**
     * Checks if the area is closed.
     */
    private void checkIfClosed() {
        if (closed) {
            return;
        }


    }

    /**
     * Gets if the given chunk is a border chunk.
     * @param chunk The chunk to check.
     * @return True if the chunk is a border chunk.
     */
    private boolean isBorderChunk(Chunk chunk) {
        return chunk.getCurrentId() != ChunkId.CENTER_MIDDLE || chunks.size() == 1;
    }
}
