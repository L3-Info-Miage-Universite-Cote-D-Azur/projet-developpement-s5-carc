package logic.tile.chunk;

import logic.board.GameBoard;
import logic.tile.Tile;
import logic.tile.TileEdge;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

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
     */
    public ChunkArea(List<Chunk> chunks) {
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

        checkClosure();
    }

    /**
     * Called when the parent tile of the area is placed on the board.
     */
    public void onBoard() {
        checkClosure();
    }

    /**
     * Checks if the area is closed.
     */
    private void checkClosure() {
        if (closed) {
            return;
        }

        GameBoard board = tiles.iterator().next().getGame().getBoard();

        // Checks if we have a continuation of this area on any edge of the tiles.
        // If yes, then the area is not closed as we can continue on the edge.
        closed = tiles.stream().allMatch(t -> {
            for (TileEdge edge : TileEdge.values()) {
                if (Arrays.stream(edge.getChunkIds()).map(c -> t.getChunk(c)).anyMatch(c -> c.getArea() == this)) {
                    if (board.getTileAt(t.getPosition().add(edge.getValue())) == null) {
                        // We can continue on this edge -> not closed.
                        return false;
                    }
                }
            }

            // No edge can be continued -> Need to check the next tile.
            return true;
        });
    }

    @Override
    public String toString() {
        return Integer.toString(id);
    }
}
