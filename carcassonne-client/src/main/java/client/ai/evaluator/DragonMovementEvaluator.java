package client.ai.evaluator;

import logic.Game;
import logic.math.Vector2;
import logic.player.Player;
import logic.tile.Tile;
import logic.tile.chunk.Chunk;
import logic.tile.chunk.ChunkId;

/**
 * Evaluates the heuristic value of a dragon position.
 * Used to determine the best position of a dragon.
 * <p>
 * The evaluator favours :
 * - positions that are far away from the own tiles.
 * - positions that are close to the enemy tiles.
 */
public class DragonMovementEvaluator extends HeuristicEvaluator {
    /**
     * Maximum distance fo the dragon to be considered in proximity with a tile.
     */
    public static final int OWN_PROXIMITY_THRESHOLD = 10;

    /**
     * Maximum distance fo the dragon to be considered in very proximity with a tile.
     */
    public static final int OWN_PROXIMITY_PANIC_THRESHOLD = 4;

    /**
     * The penalty for a dragon being in proximity with a tile.
     */
    public static final int OWN_PROXIMITY_PENALTY = 5;

    /**
     * The penalty for a dragon being in very proximity with a tile.
     */
    public static final int OWN_PROXIMITY_PANIC_PENALTY = 100;

    /**
     * Maximum distance fo the dragon to be considered in proximity with a tile.
     */
    public static final int ENEMY_PROXIMITY_THRESHOLD = 10;

    /**
     * Maximum distance fo the dragon to be considered in very proximity with a tile.
     */
    public static final int ENEMY_PROXIMITY_PANIC_THRESHOLD = 4;

    /**
     * The penalty for a dragon being in proximity with a tile.
     */
    public static final int ENEMY_PROXIMITY_SCORE = 2;

    /**
     * The penalty for a dragon being in very proximity with a tile.
     */
    public static final int ENEMY_PROXIMITY_PANIC_SCORE = 25;

    private final Game game;
    private final Player player;

    public DragonMovementEvaluator(Game game, Player player) {
        this.game = game;
        this.player = player;
    }

    /**
     * Evaluates the dragon's position to move.
     *
     * @param position the position to evaluate.
     * @return the evaluation score.
     */
    public int evaluate(Vector2 position) {
        evaluateTiles(position);
        return finalizeScore();
    }

    /**
     * Evaluates the tiles' proximity to the given position.
     *
     * @param position the position to evaluate.
     */
    private void evaluateTiles(Vector2 position) {
        for (Tile tile : game.getBoard().getTiles()) {
            if (isOwnTile(tile)) {
                evaluateOwnTiles(position, tile);
            } else if (isEnemyTile(tile)) {
                evaluateEnemyTiles(position, tile);
            }
        }
    }

    /**
     * Evaluates the proximity of the position to our tiles (tiles with our meeples).
     * Severely penalizes positions too close to our tiles.
     *
     * @param position the position to evaluate.
     * @param tile     the tile to evaluate.
     */
    private void evaluateOwnTiles(Vector2 position, Tile tile) {
        int distance = tile.getPosition().subtract(position).magnitude();

        if (distance < OWN_PROXIMITY_THRESHOLD) {
            addPenalty(distance < OWN_PROXIMITY_PANIC_THRESHOLD
                    ? OWN_PROXIMITY_PANIC_PENALTY * (OWN_PROXIMITY_PANIC_THRESHOLD - distance)
                    : OWN_PROXIMITY_PENALTY * (OWN_PROXIMITY_THRESHOLD - distance));
        } else {
            addPenalty(distance / 10);
        }
    }

    /**
     * Evaluates the proximity of the position to the enemy tiles (tiles with enemy meeples).
     * Severely favours positions close to the enemy tiles.
     *
     * @param position the position to evaluate.
     */
    private void evaluateEnemyTiles(Vector2 position, Tile tile) {
        int distance = tile.getPosition().subtract(position).magnitude();

        if (distance < ENEMY_PROXIMITY_THRESHOLD) {
            addScore(distance < ENEMY_PROXIMITY_PANIC_THRESHOLD
                    ? ENEMY_PROXIMITY_PANIC_SCORE * (ENEMY_PROXIMITY_PANIC_THRESHOLD - distance)
                    : ENEMY_PROXIMITY_SCORE * (ENEMY_PROXIMITY_THRESHOLD - distance));
        }
    }

    /**
     * Returns whether the given tile is owned by the player.
     *
     * @param tile the tile to check.
     * @return whether the given tile is owned by the player.
     */
    private boolean isOwnTile(Tile tile) {
        for (ChunkId chunkId : ChunkId.values()) {
            Chunk chunk = tile.getChunk(chunkId);

            if (chunk.hasMeeple() && chunk.getMeeple().getOwner() == player) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns whether the given tile is owned by an enemy.
     *
     * @param tile the tile to check.
     * @return whether the given tile is owned by an enemy.
     */
    private boolean isEnemyTile(Tile tile) {
        boolean hasMeeple = false;

        for (ChunkId chunkId : ChunkId.values()) {
            Chunk chunk = tile.getChunk(chunkId);

            if (chunk.hasMeeple()) {
                hasMeeple = true;

                if (chunk.getMeeple().getOwner() == player) {
                    return false;
                }
            }
        }

        return hasMeeple;
    }
}
