package logic.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import logic.tile.TileType;

import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GameConfig {
    public class TileConfig {
        public int DECK_COUNT;
    }

    public int MIN_PLAYERS = 2;
    public int MAX_PLAYERS = 5;
    public int PLAYER_DECK_CAPACITY;
    public HashMap<TileType, TileConfig> TILES = new HashMap<>() {{
        // TODO
    }};

    public boolean validate() {
        if (MIN_PLAYERS < 0 || MIN_PLAYERS > MAX_PLAYERS)
            return false;
        if (PLAYER_DECK_CAPACITY < 1)
            return false;

        return true;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
