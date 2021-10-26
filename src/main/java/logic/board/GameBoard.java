package logic.board;

import logic.math.Vector2;
import logic.tile.Tile;

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
    public Tile getStartingTile() { return tiles.getOrDefault(STARTING_TILE_POSITION, null); }

    public boolean hasTileAt(Vector2 position){
        return tiles.containsKey(position);
    }
    public boolean isEmpty() {
        return tiles.size() == 0;
    }

    public ArrayList<Vector2> findFreePoints() {
        Tile startingTile = getStartingTile();
        ArrayList<Vector2> freePoints = new ArrayList<>();

        if (startingTile != null) {
            findFreePointsFromNode(startingTile, new HashSet<>(), freePoints);
        }

        return freePoints;
    }

    private void findFreePointsFromNode(Tile node, HashSet<Tile> parentNodes, ArrayList<Vector2> freePoints) {
        Vector2 tilePosition = node.getPosition();

        for (Vector2 edgeOffset : TILE_EDGE_OFFSETS) {
            Vector2 edgePos = tilePosition.add(edgeOffset);

            if (hasTileAt(edgePos)) {
                Tile subNode = getTileAt(edgePos);

                if (!parentNodes.contains(subNode)) {
                    parentNodes.add(subNode);
                    findFreePointsFromNode(subNode, parentNodes, freePoints);
                }
            } else {
                freePoints.add(edgePos);
            }
        }
    }
}
