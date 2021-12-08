package client.ai.heuristic.evaluator;

import logic.Game;
import logic.meeple.Meeple;
import logic.player.Player;
import logic.tile.area.Area;
import logic.tile.chunk.Chunk;

public class HeuristicMeepleRemovalEvaluator extends HeuristicEvaluator {
    /**
     * Score earned if the area contains an enemy meeple and
     * if the number of meeples in the area is greater than mine.
     */
    public static final int ENEMY_MEEPLE_WINNER_SCORE = 5;

    /**
     * Maximum difference before the winning enemy's score is no longer applied.
     */
    public static final int ENEMY_MEEPLE_WINNER_MAX_DIFF = 2;

    /**
     * Score earned if the area contains an enemy meeple and
     * if the number of meeples in the area is less than mine.
     */
    public static final int ENEMY_MEEPLE_LOSER_SCORE = 5;

    /**
     * Maximum difference before the loser enemy's score is no longer applied.
     */
    public static final int ENEMY_MEEPLE_LOSER_MAX_DIFF = 2;

    /**
     * Score multiplier for the area score.
     */
    public static final int AREA_SCORE_MULTIPLIER = 2;

    private final Game game;
    private final Player player;

    public HeuristicMeepleRemovalEvaluator(Game game, Player player) {
        this.game = game;
        this.player = player;
    }

    /**
     * Evaluates the removal of a meeple from a chunk.
     *
     * @param chunk The chunk to evaluate.
     * @return The score of the removal.
     */
    public int evaluate(Chunk chunk) {
        evaluateArea(chunk.getArea());
        return finalizeScore();
    }

    /**
     * Evaluates the removal of a meeple from an area.
     *
     * @param area The area to evaluate.
     */
    private void evaluateArea(Area area) {
        int ownMeeples = 0;
        int enemyMeeples = 0;

        for (Meeple meeple : area.getMeeples()) {
            if (meeple.getOwner() == player) {
                ownMeeples++;
            } else {
                enemyMeeples++;
            }
        }

        if (ownMeeples > enemyMeeples) {
            int diff = ownMeeples - enemyMeeples;

            if (diff <= ENEMY_MEEPLE_LOSER_MAX_DIFF) {
                addScore(ENEMY_MEEPLE_LOSER_SCORE * (ENEMY_MEEPLE_LOSER_MAX_DIFF - diff + 1));
            }
        } else {
            int diff = enemyMeeples - ownMeeples;

            if (diff <= ENEMY_MEEPLE_WINNER_MAX_DIFF) {
                addScore(ENEMY_MEEPLE_WINNER_SCORE * (ENEMY_MEEPLE_WINNER_MAX_DIFF - diff + 1));
            }
        }

        addScore(AREA_SCORE_MULTIPLIER * area.getClosingPoints());
    }
}
