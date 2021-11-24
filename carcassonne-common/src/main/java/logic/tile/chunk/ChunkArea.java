package logic.tile.chunk;

import logic.tile.Tile;

import java.util.HashSet;
import java.util.List;

/**
 * Represents a chunk area.
 * It contains the list of chunks in the area.
 */
public class ChunkArea {
    private final HashSet<Chunk> chunks;
    private final HashSet<Tile> tiles;
    private final ChunkType type;

    private boolean closed;

    public ChunkArea(ChunkType type) {
        this.type = type;

        chunks = new HashSet<>();
        tiles = new HashSet<>();
        closed = false;
    }

    /**
     *
     * @return
     */
    public HashSet<Chunk> getChunks() {
        return chunks;
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
        checkIfClosed();
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

        List<Chunk> borderChunks = chunks.stream().filter(Chunk::isBorder).toList();

    }

    private boolean findClosedFromBorder(Chunk border, HashSet<Chunk> visited) {
        visited.add(border);
        Tile tile = border.getParent();

        for (ChunkId neighborId : border.getCurrentId().getNeighbors()) {
            Chunk neighbor = tile.getChunk(neighborId);

            if (visited.contains(neighbor)) {
                continue;
            }

            if (neighbor.isBorder()) {
                if (findClosedFromBorder(neighbor, visited)) {
                    return true;
                }
            }
        }

        return false;
    }
}
