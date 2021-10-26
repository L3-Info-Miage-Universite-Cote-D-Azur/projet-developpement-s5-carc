package logic.player;

import logic.board.GameBoard;
import logic.math.Vector2;
import logic.tile.TileData;
import logic.tile.TileType;

import java.util.ArrayList;
import java.util.Random;

public class SimpleAIPlayer extends AIPlayerBase {
    private Random random;

    public SimpleAIPlayer(int id) {
        super(id);
        this.random = new Random();
    }

    @Override
    public Vector2 findFreePositionForTile(TileData tile) {
        GameBoard board = game.getBoard();
        ArrayList<Vector2> freePoints = board.findFreePoints(tile);

        if (freePoints.isEmpty()) {
            return null;
        }

        return freePoints.get(random.nextInt(freePoints.size()));
    }
}
