package logic.tile.chunk;

import logic.tile.Tile;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/**
 * Represents a chunk area.
 * It contains the list of chunks in the area.
 */
public class ChunkArea {
    private static int uniqueId;

    private final HashSet<Chunk> chunks;
    private final HashSet<Tile> tiles;
    private final ChunkType type;
    private final int id;

    private boolean closed;

    /**
     * Constructor for the area.
     * @param type The type of the area.
     */
    public ChunkArea(ChunkType type) {
        this.type = type;
        this.id = uniqueId++;

        chunks = new HashSet<>();
        tiles = new HashSet<>();
        closed = false;
    }

    /**
     * Gets the list of chunks in the area.
     * @return The list of chunks.
     */
    public HashSet<Chunk> getChunks() {
        return chunks;
    }

    /**
     * Gets the area unique id.
     * @return The unique id.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns whether the area is closed.
     * @return True if the area is closed, false otherwise.
     */
    public boolean isClosed() {
        return closed;
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
        if (chunk.getType() != type) {
            throw new IllegalArgumentException("Chunk type does not match area type.");
        }
        if (chunks.contains(chunk)) {
            throw new IllegalStateException("Chunk already in area.");
        }

        chunks.add(chunk);
        tiles.add(chunk.getParent());
        chunk.setArea(this);

        if (chunk.getParent().getPosition() != null) {
            checkIfClosed();
        }
    }

    /**
     * Merges two areas together.
     * @param other The other area to merge with.
     */
    public void merge(ChunkArea other) {
        if (other.type != type) {
            throw new IllegalArgumentException("Cannot merge areas of different types.");
        }

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

        Chunk first = chunks.stream().filter(c -> c.isBorder()).findFirst().orElse(null);

        if (first != null) {
            HashSet<Chunk> chunksVisited = new HashSet<>();
            // closed = browseAreaBorder(first, first, null, chunksVisited);
        } else {
            closed = false;
        }
    }

    @Override
    public String toString() {
        return Integer.toString(id);
    }
}
