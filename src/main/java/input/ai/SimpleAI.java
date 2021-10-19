package input.ai;

import logger.Logger;
import logic.board.GameBoard;
import logic.math.Vector2;
import logic.tile.Tile;
import logic.tile.TileData;
import logic.tile.TileFactory;

import java.util.ArrayList;
import java.util.Random;

public class SimpleAI extends AI {
    private final Random random;

    public SimpleAI() {
        random = new Random();
    }

    @Override
    public boolean placeTile(TileData data) {
        GameBoard board = player.getGame().getGameBoard();
        Vector2 position;

        if (board.isEmpty()) {
            position = GameBoard.STARTING_TILE_POSITION;
        }
        else {
            position = findRandomPluggablePoint();

            if (position == null) {
                Logger.info(String.format("[AI] No point available to place tile %d", data.getType()));
                return false;
            }
        }

        Logger.info(String.format("[AI] Player %d places the tile %s to %s", player.getInfo().getId(), data.getType(), position));

        board.place(TileFactory.create(data, position));
        return true;
    }

    private Vector2 findRandomPluggablePoint() {
        GameBoard board = player.getGame().getGameBoard();

        if (board.isEmpty()) {
            return null;
        }

        ArrayList<Vector2> pluggablePoints = retrievePluggablePoints(board);
        Vector2 pluggablePointSelected = pluggablePoints.get(random.nextInt(pluggablePoints.size()));
        return pluggablePointSelected;
    }

    private static ArrayList<Vector2> retrievePluggablePoints(GameBoard board) {
        ArrayList<Vector2> pluggablePoints = new ArrayList<>();
        ArrayList<Tile> pluggedTiles = new ArrayList<>();
        retrievePluggablePoints(board, board.getTileAt(GameBoard.STARTING_TILE_POSITION), pluggedTiles, pluggablePoints);
        return pluggablePoints;
    }

    private static void retrievePluggablePoints(GameBoard board, Tile tile, ArrayList<Tile> parentTiles, ArrayList<Vector2> pluggablePoints) {
        Vector2[] edges = {
                new Vector2(1, 0), // Droite
                new Vector2(-1, 0), // Gauche

                new Vector2(0, 1), // Haut
                new Vector2(0, -1), // Bas
        };

        for (Vector2 edge : edges) {
            Vector2 pos = tile.getPosition().add(edge);

            if (board.hasTileAt(pos)) {
                Tile nextTile = board.getTileAt(pos);

                if (!parentTiles.contains(nextTile)) {
                    parentTiles.add(nextTile);
                    retrievePluggablePoints(board, nextTile, parentTiles, pluggablePoints);
                }
            } else {
                pluggablePoints.add(pos);
            }
        }
    }
}
