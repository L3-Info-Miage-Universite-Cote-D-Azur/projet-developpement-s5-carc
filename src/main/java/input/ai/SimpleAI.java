package input.ai;

import logger.Logger;
import logic.board.GameBoard;
import logic.math.Vector2;
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
        GameBoard board = player.getGame().getBoard();
        Vector2 position;

        if (board.isEmpty()) {
            position = GameBoard.STARTING_TILE_POSITION;
        }
        else {
            position = findRandomFreePoint();

            if (position == null) {
                Logger.info(String.format("[AI] No point available to place tile %d", data.getType()));
                return false;
            }
        }

        Logger.info(String.format("[AI] Player %d places the tile %s to %s", player.getInfo().getId(), data.getType(), position));

        board.place(TileFactory.create(data, position));
        return true;
    }

    private Vector2 findRandomFreePoint() {
        GameBoard board = player.getGame().getBoard();
        ArrayList<Vector2> freePoints = board.findFreePoints();

        if (freePoints.isEmpty()) {
            return null;
        }
        
        return freePoints.get(random.nextInt(freePoints.size()));
    }
}
