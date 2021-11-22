package server.listener;

import logic.IGameListener;
import logic.player.Player;
import logic.state.GameState;
import logic.state.turn.GameTurnPlaceTileState;
import logic.tile.ChunkId;
import logic.tile.Tile;
import server.logger.Logger;
import server.matchmaking.Match;

public class MatchGameListener implements IGameListener {
    private final Match match;

    public MatchGameListener(Match match) {
        this.match = match;
    }

    @Override
    public void onGameStarted() {
        Logger.debug("Match %d: Game started", match.getId());
    }

    @Override
    public void onGameOver() {
        Logger.debug("Match %d: Game over", match.getId());
    }

    /**
     * Called when the turn is started.
     */
    @Override
    public void onTurnStarted(int id) {
        Logger.debug("Match %d: Turn %d started", match.getId(), id);
    }

    /**
     * Called when the turn is ended.
     */
    @Override
    public void onTurnEnded(int id) {
        Logger.debug("Match %d: Turn %d ended", match.getId(), id);
    }


    @Override
    public void onStateChanged(GameState state) {
        Logger.debug("Match %d: State changed to %s", match.getId(), state.getType());

        switch (state.getType()) {
            case TURN_PLACE_TILE -> match.onGameTurnStarted(((GameTurnPlaceTileState) state).getTileDrawn());
            case OVER -> match.onGameOver();
        }
    }

    @Override
    public void onTilePlaced(Tile tile) {
        Logger.debug("Match %d: Tile model %s placed at (%d,%d)", match.getId(), tile.getConfig().model, tile.getPosition().getX(), tile.getPosition().getY());
    }

    @Override
    public void onMeeplePlaced(Player player, Tile tile, ChunkId chunkId) {
        Logger.debug("Match %d: Meeple placed at tile (%d,%d), chunk %s", match.getId(), tile.getPosition().getX(), tile.getPosition().getY(), chunkId);
    }

    @Override
    public void onMeepleRemoved(Player player, Tile tile, ChunkId chunkId) {
        Logger.debug("Match %d: Meeple removed from tile (%d,%d), chunk %s", match.getId(), tile.getPosition().getX(), tile.getPosition().getY(), chunkId);
    }
}
