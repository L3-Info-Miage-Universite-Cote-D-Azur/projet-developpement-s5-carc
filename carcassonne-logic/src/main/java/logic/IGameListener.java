package logic;

import logic.tile.Chunk;
import logic.tile.Tile;

public interface IGameListener {
    void onTurnStarted(int id);
    void onTilePlaced(Tile tile);
    void onMeeplePlaced(Chunk chunk);
    void onStart();
    void onEnd();

    void logWarning(String message);
    void logWarning(String message, Object... args);
}
