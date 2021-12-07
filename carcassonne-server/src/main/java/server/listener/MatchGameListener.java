package server.listener;

import logic.IGameListener;
import logic.player.Player;
import logic.state.GameState;
import logic.state.turn.GameTurnPlaceTileState;
import logic.tile.Tile;
import logic.tile.chunk.Chunk;
import logic.tile.chunk.ChunkId;
import server.logger.Logger;
import server.matchmaking.Match;

public class MatchGameListener implements IGameListener {
    private final Match match;

    public MatchGameListener(Match match) {
        this.match = match;
    }

    @Override
    public void onGameStarted() {
        Logger.info("Match %d: Game started", match.getId());
    }

    @Override
    public void onGameOver() {
        Logger.info("Match %d: Game over", match.getId());
        match.onGameOver();
    }

    /**
     * Called when the turn is started.
     */
    @Override
    public void onTurnStarted(int id, Tile tileDrawn) {
        Logger.info("Match %d: Turn %d started", match.getId(), id);
        match.onGameTurnStarted(tileDrawn);
    }

    /**
     * Called when the turn is ended.
     */
    @Override
    public void onTurnEnded(int id) {
        Logger.info("Match %d: Turn %d ended", match.getId(), id);
    }

    @Override
    public void onStateChanged(GameState state) {
        Logger.info("Match %d: State changed to %s", match.getId(), state.getType());
    }

    @Override
    public void onTilePlaced(Tile tile) {
        Logger.info("Match %d: Tile model %s placed at (%d,%d)", match.getId(), tile.getConfig().model, tile.getPosition().getX(), tile.getPosition().getY());
    }

    @Override
    public void onMeeplePlaced(Chunk chunk) {
        Logger.info("Match %d: Meeple placed at tile (%d,%d), chunk %s", match.getId(), chunk.getParent().getPosition().getX(), chunk.getParent().getPosition().getY(), chunk.getCurrentId());
    }

    @Override
    public void onFairyPlaced(Chunk chunk) {
        Logger.info("Match %d: Fairy placed at tile (%d,%d), chunk %s", match.getId(), chunk.getParent().getPosition().getX(), chunk.getParent().getPosition().getY(), chunk.getCurrentId());
    }

    @Override
    public void onMeepleRemoved(Chunk chunk) {
        Logger.info("Match %d: Meeple removed from tile (%d,%d), chunk %s", match.getId(), chunk.getParent().getPosition().getX(), chunk.getParent().getPosition().getY(), chunk.getCurrentId());
    }
}
