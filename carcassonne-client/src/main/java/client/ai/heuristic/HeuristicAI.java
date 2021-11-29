package client.ai.heuristic;

import client.ai.AI;
import client.ai.TilePosition;
import client.ai.heuristic.evaluator.HeuristicMeeplePlacementEvaluator;
import client.ai.heuristic.evaluator.HeuristicTileEvaluator;
import client.ai.target.TargetList;
import logic.Game;
import logic.math.Vector2;
import logic.player.Player;
import logic.tile.Tile;
import logic.tile.TileRotation;
import logic.tile.chunk.Chunk;
import logic.tile.chunk.ChunkId;

/**
 * AI that uses a heuristic to determine the best move to make.
 */
public class HeuristicAI extends AI {
    /**
     * Maximum number of entries in the target list picker.
     */
    private static int TARGET_LIST_MAX_SIZE = 100;

    /**
     * Minimum of score to consider a meeple placement.
     */
    private static int MEEPLE_PLACEMENT_MIN_SCORE = 10;

    private final HeuristicTileEvaluator tileEvaluator;
    private final HeuristicMeeplePlacementEvaluator meeplePlacementEvaluator;

    public HeuristicAI(Player player) {
        super(player);

        Game game = player.getGame();

        if (player.getGame() == null) {
            throw new IllegalArgumentException("Player must be in a game.");
        }

        this.tileEvaluator = new HeuristicTileEvaluator(game);
        this.meeplePlacementEvaluator = new HeuristicMeeplePlacementEvaluator(game);
    }

    /**
     * Finds the best position for the given tile to be placed.
     *
     * @param tile The tile to find a position for.
     * @return
     */
    @Override
    protected TilePosition findPositionForTile(Tile tile) {
        TargetList<TilePosition> targetList = new TargetList<>(TARGET_LIST_MAX_SIZE);

        for (int i = 0; i < TileRotation.NUM_ROTATIONS; i++) {
            tile.rotate();
            TileRotation rotation = tile.getRotation();

            for (Vector2 freePlace : getGame().getBoard().findFreePlacesForTile(tile)) {
                targetList.add(new TilePosition(freePlace, rotation), tileEvaluator.evaluate(tile, freePlace));
            }
        }

        return targetList.pick();
    }

    /**
     * Picks a tile's chunk where the meeple can be placed.
     * Returns null if no chunk should be placed.
     *
     * @return The chunk where the meeple can be placed.
     */
    @Override
    protected Chunk pickChunkToPlaceMeeple() {
        TargetList<Chunk> targetList = new TargetList<>(TARGET_LIST_MAX_SIZE);

        for (Tile tile : getGame().getBoard().getTiles()) {
            for (ChunkId chunkId : ChunkId.values()) {
                Chunk chunk = tile.getChunk(chunkId);

                if (!chunk.getArea().hasMeeple()) {
                    int score = meeplePlacementEvaluator.evaluate(chunk);

                    if (score >= MEEPLE_PLACEMENT_MIN_SCORE) {
                        targetList.add(chunk, score);
                    }
                }
            }
        }

        return targetList.pick();
    }
}
