package client.ai.heuristic.evaluator;

import logic.Game;
import logic.player.Player;
import logic.tile.area.AbbeyArea;
import logic.tile.area.Area;
import logic.tile.area.RoadArea;
import logic.tile.area.TownArea;
import logic.tile.chunk.Chunk;

/**
 * Evaluator that evaluates the placement of a meeple on a chunk.
 * Used to determine the best placement of a meeple.
 * <p>
 * The evaluator favours the placement of a meeple on an abbey, town
 * or road.
 */
public class HeuristicMeeplePlacementEvaluator extends HeuristicEvaluator {
    /**
     * Score earned for each abbey edge completed.
     */
    public static final int ABBEY_EDGE_COMPLETED_SCORE = 5;

    /**
     * Score earned for each tiles in the town area.
     */
    public static final int TOWN_TILE_COUNT_SCORE = 15;

    /**
     * Score earned for each tiles in the town area.
     */
    public static final int ROAD_TILE_COUNT_SCORE = 3;

    /**
     * Penalty for each meeples placed in the area.
     */
    public static final int PRESENT_MEEPLE_PENALTY = 20;

    /**
     * Number of open edges need to trigger the penalty for too open town area.
     */
    public static final int TOWN_TOO_OPEN_THRESHOLD = 3;

    /**
     * Number of tiles needed to trigger the penalty for too open town area.
     */
    public static final int TOWN_TOO_OPEN_TILES_THRESHOLD = 2;

    /**
     * Penalty received when the town area is too open.
     */
    public static final int TOWN_TOO_OPEN_PENALTY = 20;

    /**
     * Threshold to penalize the placement of a meeple when the remaining meeples
     * is less than this threshold.
     */
    public static final int LOW_MEEPLE_REMAINING_THRESHOLD = 2;

    /**
     * Penalty when the number of remaining meeples is less than the penalty threshold.
     */
    public static final int LOW_MEEPLE_REMAINING_PENALTY = 15;

    private final Game game;
    private final Player player;

    public HeuristicMeeplePlacementEvaluator(Game game, Player player) {
        this.game = game;
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
            default:
                throw new IllegalStateException("Unexpected value: " + area.getType());
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
    }

    /**
     * Evaluates the placement of a meeple on the given road area.
     *
     * @param area The area to evaluate.
     */
    private void evaluateRoad(RoadArea area) {
        addScore(ROAD_TILE_COUNT_SCORE * area.getNumTiles());
    }
}
