package client.ai.evaluator;

import logic.Game;
import logic.board.GameBoard;
import logic.player.Player;
import logic.tile.chunk.Chunk;

public class FairyPlacementEvaluator extends HeuristicEvaluator {
    /**
     * Score earned when the fairy is on the current chunk.
     */
    private static final int FAIRY_ON_CHUNK_SCORE = 50;

    /**
     * Score earned when the dragon is close to the fairy.
     */
    private static final int CHUNK_CLOSE_TO_DRAGON_SCORE = 4;

    /**
     * Threshold for the distance between the dragon and the chunk.
     */
    private static final int CHUNK_CLOSE_TO_DRAGON_DISTANCE_THRESHOLD = 2;

    private final Game game;
    private final Player player;

    public FairyPlacementEvaluator(Game game, Player player) {
        this.game = game;
        this.player = player;
    }

    /**
     * Evaluates the placement of a fairy on the given chunk.
     *
     * @param chunk The chunk to evaluate.
     * @return the heuristic score.
     */
    public int evaluate(Chunk chunk) {
        evaluateChunk(chunk);
        return finalizeScore();
    }

    /**
     * Evaluates the placement of a fairy on the given chunk.
     *
     * @param chunk The chunk to evaluate.
     */
    private void evaluateChunk(Chunk chunk) {
        GameBoard board = game.getBoard();

        setMultiplier(chunk.getArea().getClosingPoints());

        if (board.hasFairy() && board.getFairy().getChunk() == chunk) {
            addScore(FAIRY_ON_CHUNK_SCORE);
        }

        if (board.hasDragon() && chunk.hasMeeple()) {
            int distance = chunk.getParent().getPosition().subtract(board.getDragon().getPosition()).magnitude();

            if (distance <= CHUNK_CLOSE_TO_DRAGON_DISTANCE_THRESHOLD) {
                addScore(CHUNK_CLOSE_TO_DRAGON_SCORE * (CHUNK_CLOSE_TO_DRAGON_DISTANCE_THRESHOLD - distance + 1));
            }
        }

        resetMultiplier();
    }
}
