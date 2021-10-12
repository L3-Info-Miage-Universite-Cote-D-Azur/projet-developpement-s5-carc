package logic.config;

import logic.tile.TileType;

import java.util.HashMap;
import java.util.Set;

public class GameConfig {
    public class TileConfig {
        private int DECK_COUNT;
    }

    private int MAX_PLAYERS;
    private int MIN_PLAYERS;
    private int PLAYER_DECK_CAPACITY;
    private HashMap<TileType, TileConfig> TILES;

    public boolean validate() {
        if(MIN_PLAYERS < 0 || MAX_PLAYERS < MIN_PLAYERS){
            return false;
        }
        return true;
    }
}
