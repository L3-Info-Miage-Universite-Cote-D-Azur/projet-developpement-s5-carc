package client.ai;

import logic.Game;
import logic.board.GameBoard;
import logic.command.PlaceMeepleCommand;
import logic.math.Vector2;
import logic.player.Player;
import logic.tile.Chunk;
import logic.tile.ChunkId;
import logic.tile.Tile;

import java.util.ArrayList;
import java.util.Random;

/**
 * Simple AI that places the tile drawn randomly and randomly places meeple on the board.
 */
public class SimpleAI extends AI {
    private static final int MEEPLE_PLACEMENT_PROBABILITY = 80;

    protected final Random random;

    public SimpleAI(Player player) {
        super(player);
        random = new Random();
    }

    /**
     * This method is called when it's the turn of the AI player.
     */
    @Override
    public void placeMeepleIfNeeded() {
        if (player.hasRemainingMeeples()) {
            if (random.nextInt(100) >= MEEPLE_PLACEMENT_PROBABILITY) {
                Game game = player.getGame();
                GameBoard board = player.getGame().getBoard();
                Tile tilePicked = board.getTiles().get(random.nextInt(board.getTileCount()));

                ChunkId chunkId = ChunkId.values()[random.nextInt(ChunkId.values().length)];
                Chunk chunkToPlaceMeeple = tilePicked.getChunk(chunkId);

                if (!chunkToPlaceMeeple.hasMeeple()) {
                    game.getCommandExecutor().execute(new PlaceMeepleCommand(tilePicked, chunkId));
                }
            }
        }
    }

    /**
     * Finds the best position to place the tile.
     * @param tile The tile to place.
     * @return The position to place the tile.
     */
    @Override
    public Vector2 findPositionForTile(Tile tile) {
        GameBoard board = player.getGame().getBoard();
        ArrayList<Vector2> freePoints = board.findFreePlaceForTile(tile);

        if (freePoints.isEmpty()) {
            return null;
        }

        return freePoints.get(random.nextInt(freePoints.size()));
    }
}
