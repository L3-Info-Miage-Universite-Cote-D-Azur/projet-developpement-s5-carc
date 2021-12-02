package client.ai.heuristic;

import client.ai.AI;
import client.ai.TilePosition;
import client.ai.heuristic.evaluator.HeuristicDragonEvaluator;
import client.ai.heuristic.evaluator.HeuristicMeeplePlacementEvaluator;
import client.ai.heuristic.evaluator.HeuristicTileEvaluator;
import client.ai.target.TargetList;
import logic.Game;
import logic.dragon.Dragon;
import logic.math.Vector2;
import logic.player.Player;
import logic.tile.Direction;
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
    private final HeuristicDragonEvaluator dragonEvaluator;

    public HeuristicAI(Player player) {
        super(player);

        Game game = player.getGame();

        if (player.getGame() == null) {
            throw new IllegalArgumentException("Player must be in a game.");
        }

        this.tileEvaluator = new HeuristicTileEvaluator(game);
        this.meeplePlacementEvaluator = new HeuristicMeeplePlacementEvaluator(game, player);
        this.dragonEvaluator = new HeuristicDragonEvaluator(game, player);
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
     * Finds a tile's chunk where the meeple can be placed.
     * Returns null if no chunk should be placed.
     *
     * @return The chunk where the meeple can be placed.
     */
    @Override
    protected Chunk findChunkToPlaceMeeple(Tile tileDrawn) {
        TargetList<Chunk> targetList = new TargetList<>(TARGET_LIST_MAX_SIZE);

        if (tileDrawn.isPortal()) {
            for (Tile tile : getGame().getBoard().getTiles()) {
                findChunkToPlaceMeeple(tile, targetList);
            }
        } else {
            findChunkToPlaceMeeple(tileDrawn, targetList);
        }

        return targetList.pick();
    }

    /**
     * Finds a tile's chunk where the meeple can be placed.
     *
     * @param tile The tile to find a chunk for.
     * @param targetList The target list to add the chunks to.
     */
    private void findChunkToPlaceMeeple(Tile tile, TargetList<Chunk> targetList) {
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

    /**
     * Finds a position for the given dragon.
     *
     * @param dragon The dragon to find a position for.
     * @return The position where the dragon can be placed.
     */
    @Override
    protected Direction findDirectionForDragon(Dragon dragon) {
        TargetList<Direction> targetList = new TargetList<>(TARGET_LIST_MAX_SIZE);

        for (Direction direction : Direction.values()) {
            Vector2 position = dragon.getPosition().add(direction.value());

            if (dragon.canMoveTo(position)) {
                targetList.add(direction, dragonEvaluator.evaluate(position));
            }
        }

        return targetList.pick();
    }
}
