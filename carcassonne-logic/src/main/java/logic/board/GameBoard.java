package logic.board;

import logic.math.Vector2;
import logic.tile.Tile;
import logic.tile.TileEdge;
import logic.tile.TileFlags;

import java.util.*;

/**
 * Gameboard
 */
public class GameBoard {

    public static final Vector2 STARTING_TILE_POSITION = new Vector2(0, 0);

    private final HashMap<Vector2, Tile> tiles;

    public GameBoard() {
        this.tiles = new HashMap<>();
    }

    public void clear() {
        this.tiles.clear();
    }

    /**
     * Place a tile
     *
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
    }

    /**
     * Get tile to position
     *
     * @param position
     * @return
     */
    public Tile getTileAt(Vector2 position) {
        return tiles.getOrDefault(position, null);
    }

    public Tile getStartingTile() {
        return tiles.getOrDefault(STARTING_TILE_POSITION, null);
    }

    public int getTileCount() {
        return tiles.size();
    }

    public boolean hasTileAt(Vector2 position) {
        return tiles.containsKey(position);
    }

    public boolean isEmpty() {
        return getTileCount() == 0;
    }

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

    public List<Tile> getTiles() {
        return new ArrayList<>(tiles.values());
    }
}
