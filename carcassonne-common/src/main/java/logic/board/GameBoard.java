package logic.board;

import logic.Game;
import logic.math.Vector2;
import logic.tile.Tile;
import logic.tile.TileEdge;
import logic.tile.TileFlags;
import stream.ByteInputStream;
import stream.ByteOutputStream;
import stream.ByteStreamHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

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

    public GameBoard() {
        this.tiles = new HashMap<>();
    }

    /**
     * Clears the board.
     */
    public void clear() {
        this.tiles.clear();
    }

    /**
     * Place a tile on the board.
     * @throws IllegalArgumentException if the tile position is null.
     * @throws IllegalArgumentException if the tile is already placed on the board.
     * @param tile the tile to place
     */
    public void place(Tile tile) {
        if (tile.getPosition() == null) {
            throw new IllegalArgumentException("Try to place a tile without position.");
        }

        if (hasTileAt(tile.getPosition())) {
            throw new IllegalArgumentException("Try to place a tile on another.");
        }

        tiles.put(tile.getPosition(), tile);
        tile.onTilePlaced(this);
    }

    /**
     * Gets the tile at the specified position.
     * @param position the position of the tile
     * @return the tile at the specified position
     */
    public Tile getTileAt(Vector2 position) {
        return tiles.getOrDefault(position, null);
    }

    /**
     * Gets the starting tile.
     * @return the starting tile
     */
    public Tile getStartingTile() {
        return tiles.getOrDefault(STARTING_TILE_POSITION, null);
    }

    /**
     * Gets the number of tiles on the board.
     * @return the number of tiles on the board
     */
    public int getTileCount() {
        return tiles.size();
    }

    /**
     * Checks if the board has a tile at the specified position.
     * @param position the position to check
     * @return true if the board has a tile at the specified position, false otherwise
     */
    public boolean hasTileAt(Vector2 position) {
        return tiles.containsKey(position);
    }

    /**
     * Determines if the board is empty.
     * @return true if the board is empty, false otherwise
     */
    public boolean isEmpty() {
        return getTileCount() == 0;
    }

    /**
     * Determines if the board has a free place for the specified tile.
     * @param tileToPlace the tile to place
     * @return true if the board has a free place for the specified tile, false otherwise
     */
    public boolean hasFreePlaceForTile(Tile tileToPlace) {
        if (tileToPlace == null) {
            throw new IllegalArgumentException("Tile must be not null.");
        }

        Tile startingTile = getStartingTile();

        if (startingTile == null) {
            return tileToPlace.hasFlags(TileFlags.STARTING);
        } else if (tileToPlace.hasFlags(TileFlags.STARTING)) {
            return false;
        } else {
            return hasFreePlaceForTileFromNode(startingTile, tileToPlace, new HashSet<>());
        }
    }

    /**
     * Determines if the board has a free place for the specified tile from the specified node.
     * @param node the node to start from
     * @param tileToPlace the tile to place
     * @param parentNodes the nodes that have already been visited
     * @return true if the board has a free place for the specified tile from the specified node, false otherwise
     */
    private boolean hasFreePlaceForTileFromNode(Tile node, Tile tileToPlace, HashSet<Tile> parentNodes) {
        Vector2 tilePosition = node.getPosition();

        for (TileEdge edge : TileEdge.values()) {
            Vector2 edgePos = tilePosition.add(edge.getValue());

            if (hasTileAt(edgePos)) {
                Tile subNode = getTileAt(edgePos);

                if (!parentNodes.contains(subNode)) {
                    parentNodes.add(subNode);

                    if (hasFreePlaceForTileFromNode(subNode, tileToPlace, parentNodes)) {
                        return true;
                    }
                }
            } else if (tileToPlace.canBePlacedAt(edgePos, this)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Finds all free places for the specified tile.
     * @param tileToPlace the tile to place
     * @return a list of free places for the specified tile
     */
    public ArrayList<Vector2> findFreePlaceForTile(Tile tileToPlace) {
        if (tileToPlace == null) {
            throw new IllegalArgumentException("Tile must be not null.");
        }

        ArrayList<Vector2> freePoints = new ArrayList<>();
        Tile startingTile = getStartingTile();

        if (startingTile == null) {
            if (tileToPlace.hasFlags(TileFlags.STARTING)) {
                freePoints.add(STARTING_TILE_POSITION);
            }
        } else if (!tileToPlace.hasFlags(TileFlags.STARTING)) {
            findFreePlaceForTileFromNode(startingTile, tileToPlace, new HashSet<>(), freePoints);
        }

        return freePoints;
    }

    /**
     * Finds all free places for the specified tile from the specified node.
     * @param node the node to start from
     * @param tileToPlace the tile to place
     * @param parentNodes the nodes that have already been visited
     * @param freePoints the list of free points to add to
     */
    private void findFreePlaceForTileFromNode(Tile node, Tile tileToPlace, HashSet<Tile> parentNodes, ArrayList<Vector2> freePoints) {
        Vector2 tilePosition = node.getPosition();

        for (TileEdge edge : TileEdge.values()) {
            Vector2 edgePos = tilePosition.add(edge.getValue());

            if (hasTileAt(edgePos)) {
                Tile subNode = getTileAt(edgePos);

                if (!parentNodes.contains(subNode)) {
                    parentNodes.add(subNode);
                    findFreePlaceForTileFromNode(subNode, tileToPlace, parentNodes, freePoints);
                }
            } else if (tileToPlace.canBePlacedAt(edgePos, this)) {
                freePoints.add(edgePos);
            }
        }
    }

    /**
     * Gets all tiles on the board.
     * @return
     */
    public List<Tile> getTiles() {
        return tiles.values().stream().toList();
    }

    /**
     * Encodes the board into the specified output stream.
     * @param stream the output stream to encode to
     */
    public void encode(ByteOutputStream stream, Game game) {
        stream.writeInt(tiles.size());

        for (Tile tile : tiles.values()) {
            ByteStreamHelper.encodeTile(stream, tile, game);
        }
    }

    /**
     * Decodes the board from the specified input stream.
     * @param stream the input stream to decode from
     */
    public void decode(ByteInputStream stream, Game game) {
        tiles.clear();

        int tileCount = stream.readInt();

        for (int i = 0; i < tileCount; i++) {
            Tile tile = ByteStreamHelper.decodeTile(stream, game);
            tiles.put(tile.getPosition(), tile);
            tile.onTilePlaced(this);
        }
    }
}
