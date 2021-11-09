package logic.board;

import logic.math.Vector2;
import logic.tile.Tile;
import logic.tile.TileEdge;

import java.util.*;

/** Gameboard
 *
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

        if (getStartingTile() == null) {
            if (!tile.isStartingTile()) {
                throw new IllegalArgumentException("Starting tile must be placed before another tile can be placed.");
            }

            if (!tile.getPosition().equals(STARTING_TILE_POSITION)) {
                throw new IllegalArgumentException("Starting tile must be at 0,0.");
            }
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

    public ArrayList<Vector2> findFreePlaceForTile(Tile tileToPlace) {
        if (tileToPlace == null) {
            throw new IllegalArgumentException("Tile data must be not null.");
        }

        ArrayList<Vector2> freePoints = new ArrayList<>();
        Tile startingTile = getStartingTile();

        if (startingTile == null) {
            if (tileToPlace.isStartingTile()) {
                freePoints.add(STARTING_TILE_POSITION);
            }
        } else if (!tileToPlace.isStartingTile()) {
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
            } else {
                freePoints.add(edgePos);
            }
        }
    }

    /*@Override
    public String toString() {
        StringBuilder state = new StringBuilder();
        int sizeMax = 0;
        for (Map.Entry<Vector2, Tile> tile : tiles.entrySet()) {
            Vector2 tilePos = tile.getValue().getPosition();
            sizeMax = Math.max(sizeMax, Math.max(Math.abs(tilePos.getX()), Math.abs(tilePos.getY())));
        }
        for (int i = -sizeMax; i <= sizeMax; i++) {
            for (int j = -sizeMax; j <= sizeMax; j++) {
                Vector2 actualPos = new Vector2(i, j);
                if (hasTileAt(actualPos)) {
                    TileType type = getTileAt(actualPos).getType();
                    state.append("\033[0;3").append(type.ordinal() + 1).append("m").append(type.name().charAt(0)).append("  ");
                } else
                    state.append("\033[0;37m•  ");
            }
            state.append("\033[0;0m•\n");
        }
        state.append("\033[0;0m"); // Color Reset

        return state.toString();
    }*/
}
