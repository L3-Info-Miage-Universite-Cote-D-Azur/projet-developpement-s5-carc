package client.ai.evaluator;

import logic.player.Player;
import logic.tile.area.*;
import logic.tile.chunk.Chunk;

/**
 * Evaluator that evaluates the placement of a meeple on a chunk.
 * Used to determine the best placement of a meeple.
 * <p>
 * The evaluator favours the placement of a meeple on an abbey, town
 * or road.
 */
public class MeeplePlacementEvaluator extends HeuristicEvaluator {
    /**
     * Score earned for each abbey edge completed.
     */
    public static final int ABBEY_EDGE_COMPLETED_SCORE = 5;

    /**
     * Score earned for each tiles in the town area.
     */
    public static final int TOWN_TILE_COUNT_SCORE = 15;

    /**
     * Score earned when a meeple is placed on a closing town.
     */
    public static final int TOWN_CLOSED_SCORE = 50;

    /**
     * Score earned for each tiles in the town area.
     */
    public static final int ROAD_TILE_COUNT_SCORE = 3;

    /**
     * Score earned for each tiles in the town area.
     */
    public static final int FIELD_TILE_EARN_POINT_SCORE = 5;

    /**
     * Penalty for each meeples placed in the area.
     */
    public static final int PRESENT_MEEPLE_PENALTY = 20;

    /**
     * Number of open edges need to trigger the penalty for too open town area.
     */
    public static final int TOWN_TOO_OPEN_THRESHOLD = 6;

    /**
     * Number of tiles needed to trigger the penalty for too open town area.
     */
    public static final int TOWN_TOO_OPEN_TILES_THRESHOLD = 3;

    /**
     * Penalty received when the town area is too open.
     */
    public static final int TOWN_TOO_OPEN_PENALTY = 10;

    /**
     * Threshold to penalize the placement of a meeple when the remaining meeples
     * is less than this threshold.
     */
    public static final int LOW_MEEPLE_REMAINING_THRESHOLD = 2;

    /**
     * Penalty when the number of remaining meeples is less than the penalty threshold.
     */
    public static final int LOW_MEEPLE_REMAINING_PENALTY = 15;

    private final Player player;

    public MeeplePlacementEvaluator(Player player) {
        this.player = player;
    }

    /**
     * Evaluates the placement of a meeple on the given chunk.
     *
     * @param chunk The chunk to evaluate.
     * @return the heuristic score.
     */
    public int evaluate(Chunk chunk) {
        evaluateArea(chunk.getArea());
        evaluateRemainingMeeples();

        return finalizeScore();
    }

    /**
     * Evaluates the remaining meeples of the player.
     */
    private void evaluateRemainingMeeples() {
        int meeplesRemained = player.getMeeplesRemained();

        if (meeplesRemained <= LOW_MEEPLE_REMAINING_THRESHOLD) {
            addPenalty(LOW_MEEPLE_REMAINING_PENALTY * (LOW_MEEPLE_REMAINING_THRESHOLD - meeplesRemained + 1));
        }
    }

    /**
     * Evaluates the placement of a meeple on the given area.
     *
     * @param area The area to evaluate.
     */
    private void evaluateArea(Area area) {
        evaluateAreaCurrentMeeples(area);

        switch (area.getType()) {
            case ABBEY:
                evaluateAbbey((AbbeyArea) area);
                break;
            case TOWN:
                evaluateTown((TownArea) area);
                break;
            case ROAD:
                evaluateRoad(((RoadArea) area));
                break;
            case FIELD:
                evaluateField(((FieldArea) area));
                break;
        }
    }

    /**
     * Evaluates the current meeples on the area.
     *
     * @param area The area to evaluate.
     */
    private void evaluateAreaCurrentMeeples(Area area) {
        addPenalty(PRESENT_MEEPLE_PENALTY * area.getMeeples().size());
    }

    /**
     * Evaluates the placement of a meeple on the given abbey area.
     *
     * @param area The area to evaluate.
     */
    private void evaluateAbbey(AbbeyArea area) {
        addScore(ABBEY_EDGE_COMPLETED_SCORE * (AbbeyArea.NUM_NEIGHBORS_REQUIRED - area.getFreeEdges()));
    }

    /**
     * Evaluates the placement of a meeple on the given town area.
     *
     * @param area The area to evaluate.
     */
    private void evaluateTown(TownArea area) {
        addScore(TOWN_TILE_COUNT_SCORE * area.getNumTiles());

        if (area.getNumTiles() >= TOWN_TOO_OPEN_TILES_THRESHOLD) {
            int freeEdges = area.getFreeEdges();

            if (freeEdges >= TOWN_TOO_OPEN_THRESHOLD) {
                addPenalty(TOWN_TOO_OPEN_PENALTY * (TOWN_TOO_OPEN_THRESHOLD - freeEdges + 1));
            }
        }

        if (area.isClosed() && area.isWaitingClosingEvaluation() && area.getEvaluationWinners().isEmpty()) {
            addScore(TOWN_CLOSED_SCORE);
        }
    }

    /**
     * Evaluates the placement of a meeple on the given road area.
     *
     * @param area The area to evaluate.
     */
    private void evaluateRoad(RoadArea area) {
        addScore(ROAD_TILE_COUNT_SCORE * area.getNumTiles());
    }

    /**
     * Evaluates the placement of a meeple on the given field area.
     *
     * @param area The area to evaluate.
     */
    private void evaluateField(FieldArea area) {
        addScore(FIELD_TILE_EARN_POINT_SCORE * area.getNumTiles());
    }
}
