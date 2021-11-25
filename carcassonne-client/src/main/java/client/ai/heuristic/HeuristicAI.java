package client.ai.heuristic;

import client.ai.AI;
import logic.math.Vector2;
import logic.player.Player;
import logic.tile.Tile;
import logic.tile.chunk.Chunk;

/**
 * AI that uses a heuristic to determine the best move to make.
 */
public class HeuristicAI extends AI {
    /**
     * Threshold of the max area free edges before
     * the area is not considered in closing status.
     */
    private static int AREA_CLOSING_THRESHOLD = 2;

    /**
     * Earned heuristic score when a tile can be placed
     * on a free edge of a closing area status.
     */
    private static int AREA_CLOSING_SCORE = 150;

    public HeuristicAI(Player player) {
        super(player);
    }

    /**
     * Finds the best position for the given tile to be placed.
     *
     * @param tile The tile to find a position for.
     * @return
     */
    @Override
    protected Vector2 findPositionForTile(Tile tile) {
        return null;
    }

    @Override
    protected Chunk pickChunkToPlaceMeeple() {
        return null;
    }

    /**
     * Calculates the heuristic score for the given tile.
     *
     * @param tile The tile to calculate the score for.
     * @return The heuristic score.
     */
    private int calculateHeuristicScore(Tile tile) {
        int score = 0;
        // TODO
        return score;
    }
}
