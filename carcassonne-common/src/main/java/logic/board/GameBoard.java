package logic.board;

import logic.Game;
import logic.dragon.Dragon;
import logic.math.Vector2;
import logic.tile.Tile;
import logic.tile.Direction;
import logic.tile.TileFlags;
import logic.tile.TileRotation;
import logic.tile.area.Area;
import logic.tile.chunk.ChunkId;
import stream.ByteInputStream;
import stream.ByteOutputStream;
import stream.ByteStreamHelper;

import java.util.*;

/**
 * Class that represents the game board.
 */
public class GameBoard {
    /**
     * The position where the board starts.
     */
    public static final Vector2 STARTING_TILE_POSITION = new Vector2(0, 0);

    /**
     * Collection of all the tiles on the board.
     */
    private final HashMap<Vector2, Tile> tiles;

    /**
     * To decode the areas properly, we need to encode and decode the tiles
     * in the same order as the tile are placed on the board.
     * As the order is not guaranteed using the HashMap, we need to store the
     * order in a list.
     */
    private final ArrayList<Tile> tilesList;

    /**
     * Current dragon that is on the board.
     * Null if there is no dragon on the board.
     */
    private Dragon dragon;

    public GameBoard() {
        this.tiles = new HashMap<>();
        this.tilesList = new ArrayList<>();
    }

    /**
     * Clears the board.
     */
    public void clear() {
        this.tiles.clear();
        this.tilesList.clear();
    }

    /**
     * Place a tile on the board.
     *
     * @param tile the tile to place
     * @throws IllegalArgumentException if the tile position is null.
     * @throws IllegalArgumentException if the tile is already placed on the board.
     */
    public void place(Tile tile) {
        if (tile.getPosition() == null) {
            throw new IllegalArgumentException("Try to place a tile without position.");
        }

        if (hasTileAt(tile.getPosition())) {
            throw new IllegalArgumentException("Try to place a tile on another.");
        }

        tiles.put(tile.getPosition(), tile);
        tilesList.add(tile);

        tile.onBoard();

        updateAreaClosures();
    }

    /**
     * Gets the tile at the specified position.
     *
     * @param position the position of the tile
     * @return the tile at the specified position
     */
    public Tile getTileAt(Vector2 position) {
        return tiles.getOrDefault(position, null);
    }

    /**
     * Gets the starting tile.
     *
     * @return the starting tile
     */
    public Tile getStartingTile() {
        return tiles.getOrDefault(STARTING_TILE_POSITION, null);
    }

    /**
     * Gets the number of tiles on the board.
     *
     * @return the number of tiles on the board
     */
    public int getTileCount() {
        return tiles.size();
    }

    /**
     * Checks if the board has a tile at the specified position.
     *
     * @param position the position to check
     * @return true if the board has a tile at the specified position, false otherwise
     */
    public boolean hasTileAt(Vector2 position) {
        return tiles.containsKey(position);
    }

    /**
     * Determines if the board is empty.
     *
     * @return true if the board is empty, false otherwise
     */
    public boolean isEmpty() {
        return getTileCount() == 0;
    }

    /**
     * Determines if the board has a free place for the specified tile.
     *
     * @param tileToPlace the tile to place
     * @return true if the board has a free place for the specified tile, false otherwise
     */
    public boolean hasFreePlaceForTile(Tile tileToPlace) {
        if (tileToPlace == null) {
            throw new IllegalArgumentException("Tile must be not null.");
        }

        Tile startingTile = getStartingTile();

        if (startingTile == null) {
            return tileToPlace.hasFlag(TileFlags.STARTING);
        } else if (tileToPlace.hasFlag(TileFlags.STARTING)) {
            return false;
        } else {
            TileRotation originalRotation = tileToPlace.getRotation();

            for (int i = 0; i < TileRotation.NUM_ROTATIONS; i++) {
                tileToPlace.rotate();

                if (hasFreePlaceForTileFromNode(startingTile, tileToPlace, new HashSet<>())) {
                    // Restores the original rotation of the tile.
                    tileToPlace.setRotation(originalRotation);
                    return true;
                }
            }

            return false;
        }
    }

    /**
     * Determines if the board has a free place for the specified tile from the specified node.
     *
     * @param node        the node to start from
     * @param tileToPlace the tile to place
     * @param parentNodes the nodes that have already been visited
     * @return true if the board has a free place for the specified tile from the specified node, false otherwise
     */
    private boolean hasFreePlaceForTileFromNode(Tile node, Tile tileToPlace, HashSet<Tile> parentNodes) {
        Vector2 tilePosition = node.getPosition();

        for (Direction edge : Direction.values()) {
            Vector2 edgePos = tilePosition.add(edge.value());

            if (hasTileAt(edgePos)) {
                Tile subNode = getTileAt(edgePos);

                if (!parentNodes.contains(subNode)) {
                    parentNodes.add(subNode);

                    if (hasFreePlaceForTileFromNode(subNode, tileToPlace, parentNodes)) {
                        return true;
                    }
                }
            } else if (tileToPlace.canBePlacedAt(edgePos)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Finds all free positions for the specified tile and the current rotation.
     *
     * @param tileToPlace the tile to place
     * @return a list of free places for the specified tile
     */
    public LinkedList<Vector2> findFreePlacesForTile(Tile tileToPlace) {
        if (tileToPlace == null) {
            throw new IllegalArgumentException("Tile must be not null.");
        }

        LinkedList<Vector2> freePoints = new LinkedList<>();
        Tile startingTile = getStartingTile();

        if (startingTile == null) {
            if (tileToPlace.hasFlag(TileFlags.STARTING)) {
                freePoints.add(STARTING_TILE_POSITION);
            }
        } else if (!tileToPlace.hasFlag(TileFlags.STARTING)) {
            findFreePlaceForTileFromNode(startingTile, tileToPlace, new HashSet<>(), freePoints);
        }

        return freePoints;
    }

    /**
     * Finds all free places for the specified tile from the specified node.
     *
     * @param node        the node to start from
     * @param tileToPlace the tile to place
     * @param parentNodes the nodes that have already been visited
     * @param freePoints  the list of free points to add to
     */
    private void findFreePlaceForTileFromNode(Tile node, Tile tileToPlace, HashSet<Tile> parentNodes, List<Vector2> freePoints) {
        Vector2 tilePosition = node.getPosition();

        for (Direction edge : Direction.values()) {
            Vector2 edgePos = tilePosition.add(edge.value());

            if (hasTileAt(edgePos)) {
                Tile subNode = getTileAt(edgePos);

                if (!parentNodes.contains(subNode)) {
                    parentNodes.add(subNode);
                    findFreePlaceForTileFromNode(subNode, tileToPlace, parentNodes, freePoints);
                }
            } else if (tileToPlace.canBePlacedAt(edgePos)) {
                freePoints.add(edgePos);
            }
        }
    }

    /**
     * Updates the area closure states.
     */
    public void updateAreaClosures() {
        for (Area area : getAreas()) {
            area.updateClosure();
        }
    }

    /**
     * Gets all tiles on the board.
     *
     * @return the list of tiles on the board
     */
    public List<Tile> getTiles() {
        return tilesList;
    }

    /**
     * Gets all areas on the board.
     * @return the list of areas
     */
    public List<Area> getAreas() {
        return tilesList.stream().flatMap(t -> Arrays.stream(ChunkId.values()).map(id -> t.getChunk(id)).map(c -> c.getArea())).distinct().toList();
    }

    /**
     * Returns whether the board has a dragon.
     *
     * @return true if the board has a dragon, false otherwise
     */
    public boolean hasDragon() {
        return dragon != null;
    }

    /**
     * Gets the current dragon on the board.
     *
     * @return the current dragon on the board
     */
    public Dragon getDragon() {
        return dragon;
    }

    /**
     * Destructs the dragon on the board.
     */
    public void destructDragon() {
        dragon = null;
    }

    /**
     * Spawns the dragon on the board to the specified position.
     * @param position the position to spawn the dragon
     */
    public void spawnDragon(Vector2 position) {
        dragon = new Dragon(this, position);
    }

    /**
     * Encodes the board into the specified output stream.
     *
     * @param stream the output stream to encode to
     */
    public void encode(ByteOutputStream stream, Game game) {
        stream.writeInt(tilesList.size());

        for (Tile tile : tilesList) {
            ByteStreamHelper.encodeTile(stream, tile, game);
        }

        List<Area> areas = getAreas();
        stream.writeInt(areas.size());

        for (Area area : areas) {
            stream.writeBoolean(area.isClosed());
        }

        if (dragon != null) {
            stream.writeBoolean(true);
            dragon.encode(stream);
        } else {
            stream.writeBoolean(false);
        }
    }

    /**
     * Decodes the board from the specified input stream.
     *
     * @param stream the input stream to decode from
     */
    public void decode(ByteInputStream stream, Game game) {
        clear();

        int tileCount = stream.readInt();

        for (int i = 0; i < tileCount; i++) {
            place(ByteStreamHelper.decodeTile(stream, game));
        }

        List<Area> areas = getAreas();
        int masterAreaCount = stream.readInt();

        if (masterAreaCount != areas.size()) {
            throw new IllegalStateException("Master area count does not match area count");
        }

        for (Area area : areas) {
            boolean closed = stream.readBoolean();

            if (closed) {
                if (!area.isClosed()) {
                    throw new IllegalStateException("Area is not closed");
                }
            } else {
                if (area.isClosed()) {
                    throw new IllegalStateException("Area is closed");
                }
            }
        }

        if (stream.readBoolean()) {
            dragon = new Dragon(this);
            dragon.decode(stream);
        } else {
            dragon = null;
        }
    }
}
