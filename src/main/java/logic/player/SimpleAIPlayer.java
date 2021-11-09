package logic.player;

import logic.board.GameBoard;
import logic.math.Vector2;
import logic.tile.Tile;

import java.util.ArrayList;
import java.util.Random;

public class SimpleAIPlayer extends AIPlayerBase {
    private final Random random;

    public SimpleAIPlayer(int id) {
        super(id);
        this.random = new Random();
    }

    @Override
    public Vector2 findPositionForTile(Tile tile) {
        GameBoard board = game.getBoard();
        ArrayList<Vector2> freePoints = board.findFreePlaceForTile(tile);

        if (freePoints.isEmpty()) {
            return null;
        }

        return freePoints.get(random.nextInt(freePoints.size()));
    }
}
