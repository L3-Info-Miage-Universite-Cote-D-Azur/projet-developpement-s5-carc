package client.ai.heuristic.evaluator;

import logic.Game;
import logic.tile.chunk.Chunk;

/**
 * Evaluator that evaluates the placement of a meeple on a chunk.
 * Used to determine the best placement of a meeple.
 *
 * The evaluator favours the placement of a meeple on an abbey, town
 * or road.
 */
public class HeuristicMeeplePlacementEvaluator extends HeuristicEvaluator {
    private final Game game;

    public HeuristicMeeplePlacementEvaluator(Game game) {
        this.game = game;
    }

    /**
     * Evaluates the placement of a meeple on the given chunk.
     * @param chunk The chunk to evaluate.
     * @return the heuristic score.
     */
    public int evaluate(Chunk chunk) {
        // TODO
        return 0;
    }
}
