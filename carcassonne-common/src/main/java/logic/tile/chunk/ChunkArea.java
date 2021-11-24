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
    private final HashSet<Chunk> chunks;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChunkArea chunkArea = (ChunkArea) o;
        return closed == chunkArea.closed && Objects.equals(chunks, chunkArea.chunks) && Objects.equals(tiles, chunkArea.tiles) && type == chunkArea.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chunks, tiles, type, closed);
    }

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
            closed = browseAreaBorder(first, null, chunksVisited);
        } else {
            closed = false;
        }
    }

    private boolean browseAreaBorder(Chunk current, Chunk previous, HashSet<Chunk> chunksVisited) {
        Chunk next = findNextBorder(current, previous, chunksVisited);
        chunksVisited.add(current);

        if (next == current) {
            return true;
        } else if (next != null) {
            return browseAreaBorder(next, current, chunksVisited);
        } else {
            return false;
        }
    }

    /**
     * Finds the next border from the current chunk.
     * @param current The current chunk.
     * @param previous The previous chunk.
     * @param visited The list of visited chunks.
     * @return The next border chunk.
     */
    private Chunk findNextBorder(Chunk current, Chunk previous, HashSet<Chunk> visited) {
        List<Chunk> neighbors = current.getNeighbors();

        if (previous != null && previous.equals(current)) {
            throw new IllegalStateException("Previous chunk is not the neighbors.");
        }

        List<Chunk> borderNeighbors = neighbors.stream().filter(c -> c.isBorder()).filter(c -> !visited.contains(c)).toList();

        if (borderNeighbors.size() == 0) {
            return null;
        }

        if (borderNeighbors.size() == 1) {
            return borderNeighbors.get(0);
        }

        return borderNeighbors.stream().filter(c -> c != previous).findFirst().get();
    }
}
