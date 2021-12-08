package client.ai.heuristic.evaluator;

public class HeuristicEvaluator {
    /**
     * Current heuristic score of the evaluated tile.
     */
    private int score;

    /**
     * Current multiplier when score is earned.
     */
    private int multiplier;

    /**
     * Adds the given score to the current score.
     *
     * @param score The score to add.
     */
    protected void addScore(int score) {
        this.score += score * multiplier;
    }

    /**
     * Removes the given score from the current score.
     *
     * @param score The score to remove.
     */
    protected void addPenalty(int score) {
        this.score -= score * multiplier;
    }

    /**
     * Multiplies the current score by the given multiplier.
     *
     * @param multiplier
     */
    protected void multiplyScore(int multiplier) {
        this.score *= multiplier;
    }

    /**
     * Finalizes the heuristic score.
     *
     * @return the final heuristic score.
     */
    protected int finalizeScore() {
        int scoreTemp = this.score;
        this.score = 0;
        this.resetMultiplier();
        return scoreTemp;
    }

    /**
     * Resets the heuristic score multiplier.
     */
    protected void resetMultiplier() {
        multiplier = 1;
    }

    /**
     * Sets the multiplier for the heuristic score.
     *
     * @param multiplier The multiplier to set.
     */
    protected void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }
}
