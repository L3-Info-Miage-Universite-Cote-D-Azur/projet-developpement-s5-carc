package logic;

import logic.player.Player;
import logic.tile.ChunkId;
import logic.tile.Tile;

public interface IGameListener {
    void onTurnStarted(int id);
    void onTurnEnded(int id);

    void onTilePlaced(Tile tile);
    void onMeeplePlaced(Player player, Tile tile, ChunkId chunkId);
    void onStart();
    void onEnd();

    void onCommandFailed(String reason);
    void onCommandFailed(String reason, Object... args);
}
