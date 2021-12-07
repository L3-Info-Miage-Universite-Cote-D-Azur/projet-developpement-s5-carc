package logic.tile.area;

import logic.board.GameBoard;
import logic.command.RemoveMeepleCommand;
import logic.math.Vector2;
import logic.meeple.Meeple;
import logic.player.Player;
import logic.tile.Direction;
import logic.tile.Tile;
import logic.tile.TileFlags;
import logic.tile.chunk.Chunk;
import logic.tile.chunk.ChunkType;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a chunk area.
 * It contains the list of chunks in the area.
 */
public abstract class Area {
    private static int uniqueId;

    private final HashSet<Chunk> chunks;
    private final HashSet<Tile> tiles;
    private final Tile baseTile;
    private final int id;

    private boolean closed;

    /**
     * Constructor for the area.
     */
    public Area(List<Chunk> chunks) {
        Chunk firstChunk = chunks.get(0);

        this.chunks = new HashSet<>(chunks);

        id = uniqueId++;
        tiles = new HashSet<>();
        closed = false;

        baseTile = firstChunk.getParent();
        tiles.add(baseTile);

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
     *
     * @return The area type.
     */
    public abstract ChunkType getType();

    /**
     * Checks if the given area can be merged.
     *
     * @param other The other area to merge with.
     * @return True if the areas can be merged, false otherwise.
     */
    public abstract boolean canBeMerged(Area other);

    /**
     * Gets the points earned by the area closing.
     *
     * @return The points earned by the area closing.
     */
    public abstract int getClosingPoints();

    /**
     * Returns whether the area is closed.
     *
     * @return True if the area is closed, false otherwise.
     */
    public boolean isClosed() {
        return closed;
    }

    /**
     * Gets the board instance.
     *
     * @return
     */
    public GameBoard getBoard() {
        return baseTile.getGame().getBoard();
    }

    /**
     * Gets the base position of the area.
     *
     * @return The base position of the area.
     */
    public Tile getBaseTile() {
        return baseTile;
    }

    /**
     * Gets the number of tiles in the area.
     *
     * @return The number of tiles in the area.
     */
    public int getNumTiles() {
        return tiles.size();
    }

    /**
     * Gets the number of tiles with the given flags in the area.
     *
     * @param flag the flag to check.
     * @return The number of tiles in the area.
     */
    public int getNumTiles(TileFlags flag) {
        return (int) tiles.stream().filter(t -> t.hasFlag(flag)).count();
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
    }

    /**
     * Updates the closure of the area.
     */
    public void updateClosure() {
        if (closed) {
            return;
        }

        if (checkClosed()) {
            onClosed();
        }
    }

    /**
     * Checks if the area is closed.
     * By default, it is closed if there are no free tile edges.
     *
     * @return True if the area is closed, false otherwise.
     */
    protected boolean checkClosed() {
        /* By default, the area is closed if there is no free tile edge. */
        return !hasFreeEdge();
    }

    /**
     * Gets the remaining tile edges that can be used to continue the area.
     *
     * @return The remaining tile edges.
     */
    public int getFreeEdges() {
        return getFreeEdges(tiles, chunks);
    }

    /**
     * Gets the remaining tile edges that can be used to continue the area including the tile to place if we merge the given area.
     *
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
     *
     * @return
     */
    private boolean hasFreeEdge() {
        return getFreeEdges() >= 1;
    }

    /**
     * Called when the area is closed.
     */
    protected void onClosed() {
        closed = true;
        evaluatePoints();
    }

    /**
     * Evaluates the area points.
     */
    protected void evaluatePoints() {
        for (Player winner : getEvaluationWinners()) {
            winner.addScore(getClosingPoints(), getType());
        }

        /* As the area evaluation is done, we can remove the meeples in the area. */
        for (Chunk chunk : chunks) {
            if (chunk.hasMeeple()) {
                RemoveMeepleCommand.removeMeeple(chunk);
            }
        }
    }

    /**
     * Returns the players who will receive the area closure evaluation points.
     *
     * @return The player list.
     */
    private List<Player> getEvaluationWinners() {
        /* Get all meeples from chunks */
        List<Meeple> meeples = getMeeples();

        /* If there is no meeples in the area, there is no winner. */
        if (meeples.isEmpty()) {
            return new ArrayList<>();
        }

        /* Map the number of meeples by the player instance */
        Map<Player, Integer> numMeeplesPerPlayer = meeples.stream().collect(Collectors.groupingBy(m -> m.getOwner(), Collectors.summingInt(c -> 1)));

        /* Get the highest number of meeples */
        int highestNumMeeples = numMeeplesPerPlayer.values().stream().max(Integer::compareTo).orElse(0);

        /* Get the players who have the highest number of meeples */
        return numMeeplesPerPlayer.entrySet().stream().filter(e -> e.getValue() == highestNumMeeples).map(Map.Entry::getKey).collect(Collectors.toList());
    }

    /**
     * Gets the list of meeples that are in the area.
     *
     * @return The list of meeples.
     */
    public List<Meeple> getMeeples() {
        return chunks.stream().filter(c -> c.hasMeeple()).map(c -> c.getMeeple()).toList();
    }

    /**
     * Gets the list of meeples of the given player in the area.
     *
     * @param player The player.
     * @return The list of meeples.
     */
    public List<Meeple> getMeeples(Player player) {
        return chunks.stream().filter(c -> c.hasMeeple() && c.getMeeple().getOwner() == player).map(c -> c.getMeeple()).toList();
    }

    /**
     * Gets whether the area has one or more meeples.
     *
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
     * Gets whether the area has the given tile.
     *
     * @param tile The tile.
     * @return
     */
    public boolean hasTile(Tile tile) {
        return tiles.contains(tile);
    }

    /**
     * Gets the remaining tile edges that can be used to continue the area including the tile to place.
     *
     * @return The remaining tile edges.
     */
    protected int getFreeEdges(Set<Tile> tiles, Set<Chunk> chunks) {
        int count = 0;

        for (Tile tile : tiles) {
            for (Direction edge : Direction.values()) {
                // Check if we have a chunk from this tile that is on this edge.
                if (Arrays.stream(edge.getChunkIds()).map(tile::getChunk).anyMatch(chunks::contains)) {
                    Vector2 edgePos = tile.getPosition().add(edge.value());

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
