package client.ai.heuristic.evaluator;

import logic.Game;
import logic.board.GameBoard;
import logic.math.Vector2;
import logic.tile.Tile;
import logic.tile.TileEdge;
import logic.tile.chunk.Chunk;
import logic.tile.chunk.ChunkArea;
import logic.tile.chunk.ChunkId;
import logic.tile.chunk.ChunkType;

import java.util.HashMap;
import java.util.Map;

/**
 * Evaluator that evaluates the heuristic score of a tile.
 * Used to determine the best position to place a tile.
 *
 * The evaluator favours the following:
 * - positions that close an area OR close an area soon (preferably an abbey, town or road area).
 * - positions that are an area in contact with multiple existing areas.
 *
 * The evaluator does not favour the following:
 * - positions that area in contact with an area that are too many open edges.
 */
public class HeuristicTileEvaluator extends HeuristicEvaluator {
    /**
     * Score penalty for each free edges in an area.
     */
    private static int AREA_CLOSING_EDGE_SCORE = 50;

    /**
     * Score penalty for each free edges in an area.
     */
    private static int AREA_OPEN_EDGE_PENALTY = 25;

    /**
     * Minimum of free edges in an area before the evaluator
     * gives a score penalty.
     */
    private static int AREA_OPEN_EDGE_PENALTY_THRESHOLD = 3;

    /**
     * When a tile can be connected to multiple existing areas,
     * The current score is divided by this value to favour it.
     */
    private static int MULTIPLE_AREA_CONNECTION_MULTIPLIER = 2;

    /**
     * Multiplier for the heuristic score gained by the area type.
     */
    public static Map<ChunkType, Integer> AREA_MULTIPLIER = new HashMap<>() {{
        put(ChunkType.ROAD_END, 0);

        put(ChunkType.FIELD, 1);
        put(ChunkType.ROAD, 2);
        put(ChunkType.TOWN, 3);
        put(ChunkType.ABBEY, 4);
    }};

    private final GameBoard board;

    public HeuristicTileEvaluator(Game game) {
        this.board = game.getBoard();
    }

    /**
     * Evaluates the heuristic score for the given tile and position.
     * @param tile The tile to evaluate.
     * @param position The position to evaluate.
     * @return The heuristic score.
     */
    public int evaluate(Tile tile, Vector2 position) {
        try {
            tile.setPosition(position);
            return evaluate(tile);
        } finally {
            tile.setPosition(null);
        }
    }

    /**
     * Evaluates the heuristic score for the given tile.
     *
     * @param tile The tile to calculate the score for.
     * @return The heuristic score.
     */
    public int evaluate(Tile tile) {
        Vector2 position = tile.getPosition();

        for (TileEdge edge : TileEdge.values()) {
            Tile neighbor = board.getTileAt(position.add(edge.getValue()));

            if (neighbor != null) {
                evaluateNeighbor(tile, neighbor, edge);
            }
        }

        return finalizeScore();
    }

    /**
     * Evaluates the heuristic score for the given neighbor.
     * @param tile The tile to evaluate.
     * @param neighborTile The neighbor to evaluate.
     * @param edge The edge connecting the two tiles.
     * @return
     */
    private void evaluateNeighbor(Tile tile, Tile neighborTile, TileEdge edge) {
        ChunkId[] ownChunkIds = edge.getChunkIds();
        ChunkId[] neighborChunkIds = edge.negate().getChunkIds();
        
        int numConnectedAreas = 0;

        for (int i = 0; i < ownChunkIds.length; i++) {
            ChunkId ownChunkId = ownChunkIds[i];
            ChunkId neighborChunkId = neighborChunkIds[i];

            Chunk ownChunk = tile.getChunk(ownChunkId);
            Chunk neighborChunk = neighborTile.getChunk(neighborChunkId);

            ChunkArea ownArea = ownChunk.getArea();
            ChunkArea neighborArea = neighborChunk.getArea();

            if (neighborArea.canBeMerged(ownArea)) {
                evaluateArea(neighborArea, ownArea);
                
                if (++numConnectedAreas >= 2) {
                    multiplyScore(MULTIPLE_AREA_CONNECTION_MULTIPLIER);
                }
            }
        }
    }

    /**
     * Evaluates the heuristic score for the given area.
     * @param area The area to evaluate.
     * @return The heuristic score.
     */
    private void evaluateArea(ChunkArea area, ChunkArea mergeWith) {
        setMultiplier(AREA_MULTIPLIER.get(area.getType()));

        int openEdges = area.getFreeEdges(mergeWith);

        if (openEdges >= AREA_OPEN_EDGE_PENALTY_THRESHOLD) {
            addPenalty(AREA_OPEN_EDGE_PENALTY * (openEdges - AREA_OPEN_EDGE_PENALTY_THRESHOLD));
        } else {
            addScore(AREA_CLOSING_EDGE_SCORE * (AREA_OPEN_EDGE_PENALTY_THRESHOLD - openEdges));
        }

        resetMultiplier();
    }
}
