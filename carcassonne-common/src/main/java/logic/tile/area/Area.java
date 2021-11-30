package logic.tile.area;

import logic.math.Vector2;
import logic.meeple.Meeple;
import logic.tile.Tile;
import logic.tile.TileEdge;
import logic.tile.chunk.Chunk;
import logic.tile.chunk.ChunkType;

import java.util.*;

/**
 * Represents a chunk area.
 * It contains the list of chunks in the area.
 */
public class Area {
    private static int uniqueId;

    private final HashSet<Chunk> chunks;
    private final HashSet<Tile> tiles;
    private final ChunkType type;
    private final int id;

    private boolean closed;

    /**
     * Constructor for the area.
     */
    public Area(List<Chunk> chunks) {
        Chunk firstChunk = chunks.get(0);

        this.type = firstChunk.getType();
        this.chunks = new HashSet<>(chunks);

        id = uniqueId++;
        tiles = new HashSet<>();
        closed = false;

        tiles.add(firstChunk.getParent());

        for (Chunk chunk : chunks) {
            chunk.setArea(this);
        }
    }

    /**
     * Gets the list of chunks in the area.
     *
     * @return The list of chunks.
     */
    public HashSet<Chunk> getChunks() {
        return chunks;
    }

    /**
     * Gets the area unique id.
     *
     * @return The unique id.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the area type.
     * @return The area type.
     */
    public ChunkType getType() {
        return type;
    }

    /**
     * Returns whether the area is closed.
     *
     * @return True if the area is closed, false otherwise.
     */
    public boolean isClosed() {
        return closed;
    }

    /**
     * Checks if the given area can be merged.
     * @param other The other area to merge with.
     * @return True if the areas can be merged, false otherwise.
     */
    public boolean canBeMerged(Area other) {
        return type == other.type;
    }

    /**
     * Merges two areas together.
     *
     * @param other The other area to merge with.
     */
    public void merge(Area other) {
        if (!canBeMerged(other)) {
            throw new IllegalArgumentException("Cannot merge areas of different types.");
        }

        chunks.addAll(other.chunks);
        tiles.addAll(other.tiles);

        for (Chunk chunk : other.chunks) {
            chunk.setArea(this);
        }

        updateClosure();
    }

    /**
     * Updates the closure of the area.
     */
    private void updateClosure() {
        closed = checkClosed();

        if (closed) {
            onClosed();
        }
    }

    /**
     * Checks if the area is closed.
     * By default, it is closed if there are no free tile edges.
     * @return True if the area is closed, false otherwise.
     */
    protected boolean checkClosed() {
        /* By default, the area is closed if there is no free tile edge. */
        return !hasFreeEdge();
    }

    /**
     * Gets the remaining tile edges that can be used to continue the area.
     * @return The remaining tile edges.
     */
    public int getFreeEdges() {
        return getFreeEdges(tiles, chunks);
    }

    /**
     * Gets the remaining tile edges that can be used to continue the area including the tile to place if we merge the given area.
     * @return The remaining tile edges.
     */
    public int getFreeEdges(Area simulatedMergingArea) {
        Set<Tile> tiles = new HashSet<>(this.tiles);
        Set<Chunk> chunks = new HashSet<>(this.chunks);

        tiles.addAll(simulatedMergingArea.tiles);
        chunks.addAll(simulatedMergingArea.chunks);

        return getFreeEdges(tiles, chunks);
    }

    /**
     * Gets whether remaining tile edges can be used to continue the area.
     * @return
     */
    private boolean hasFreeEdge() {
        return getFreeEdges() >= 1;
    }

    /**
     * Called when the area is closed.
     */
    private void onClosed() {
        List<Chunk> chunksWithMeeple = chunks.stream().filter(c -> c.hasMeeple()).toList();

        if (chunksWithMeeple.size() >= 1) {
            // TODO: Change score earn by chunk type.
            Meeple meeple = chunksWithMeeple.get(0).getMeeple();
            meeple.getOwner().addScore(tiles.size(), type);

            /* As the area points are counted, we can remove the meeples from the area. */
            for (Chunk chunk : chunksWithMeeple) {
                meeple.getOwner().decreasePlayedMeeples();
                chunk.setMeeple(null);
            }
        }
    }

    /**
     * Gets whether the area has one or more meeples.
     * @return
     */
    public boolean hasMeeple() {
        for (Chunk chunk : chunks) {
            if (chunk.hasMeeple()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Called when the parent tile of the area is placed on the board.
     */
    public void onBoard() {
        updateClosure();
    }

    /**
     * Gets the remaining tile edges that can be used to continue the area including the tile to place.
     * @return The remaining tile edges.
     */
    private static int getFreeEdges(Set<Tile> tiles, Set<Chunk> chunks) {
        int count = 0;

        for (Tile tile : tiles) {
            for (TileEdge edge : TileEdge.values()) {
                // Check if we have a chunk from this tile that is on this edge.
                if (Arrays.stream(edge.getChunkIds()).map(tile::getChunk).anyMatch(chunks::contains)) {
                    Vector2 edgePos = tile.getPosition().add(edge.getValue());

                    if (tiles.stream().noneMatch(t -> t.getPosition().equals(edgePos))) {
                        // We can continue on this edge -> not closed.
                        count++;
                    }
                }
            }
        }

        return count;
    }

    @Override
    public String toString() {
        return Integer.toString(id);
    }
}
