package logic.board;

import logic.math.Vector2;
import logic.tile.Tile;
import logic.tile.TileData;
import logic.tile.TileType;

import java.util.*;

public class GameBoard {
    public static final Vector2 STARTING_TILE_POSITION = new Vector2(0, 0);
    public static final Vector2[] TILE_EDGE_OFFSETS = {
        new Vector2(1, 0), // Droite
        new Vector2(-1, 0), // Gauche

        new Vector2(0, 1), // Haut
        new Vector2(0, -1), // Bas
    };

    private final HashMap<Vector2, Tile> tiles;

    public GameBoard(){
        this.tiles = new HashMap<>();
    }

    public void place(Tile tile) {
        if (hasTileAt(tile.getPosition())) {
            throw new IllegalArgumentException("Try to place a tile on another.");
        }

        tiles.put(tile.getPosition(), tile);
    }

    public Tile getTileAt(Vector2 position) {
        return tiles.getOrDefault(position, null);
    }
    public Tile getStartingTile() {
        return tiles.getOrDefault(STARTING_TILE_POSITION, null);
    }

    public int getTileCount() {
        return tiles.size();
    }

    public boolean hasTileAt(Vector2 position){
        return tiles.containsKey(position);
    }
    public boolean isEmpty() {
        return getTileCount() == 0;
    }

    public ArrayList<Vector2> findFreePoints(TileData tileData) {
        if (tileData == null) {
            throw new IllegalArgumentException("Tile data must be not null.");
        }

        Tile startingTile = getStartingTile();
        ArrayList<Vector2> freePoints = new ArrayList<>();

        if (startingTile != null) {
            findFreePointsFromNode(startingTile, tileData, new HashSet<>(), freePoints);
        } else if (tileData.getType() == TileType.START) {
            return new ArrayList<>() {{ add(STARTING_TILE_POSITION); }};
        }

        return freePoints;
    }

    private void findFreePointsFromNode(Tile node, TileData tileData, HashSet<Tile> parentNodes, ArrayList<Vector2> freePoints) {
        Vector2 tilePosition = node.getPosition();

        for (Vector2 edgeOffset : TILE_EDGE_OFFSETS) {
            Vector2 edgePos = tilePosition.add(edgeOffset);

            if (hasTileAt(edgePos)) {
                Tile subNode = getTileAt(edgePos);

                if (!parentNodes.contains(subNode)) {
                    parentNodes.add(subNode);
                    findFreePointsFromNode(subNode, tileData, parentNodes, freePoints);
                }
            } else {
                freePoints.add(edgePos);
            }
        }
    }
}
