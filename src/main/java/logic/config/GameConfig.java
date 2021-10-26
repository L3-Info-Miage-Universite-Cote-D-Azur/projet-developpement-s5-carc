package logic.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import logic.tile.TileType;

import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GameConfig {
    public int MIN_PLAYERS = 2;
    public int MAX_PLAYERS = 5;
    public int PLAYER_DECK_CAPACITY;
    public HashMap<TileType, TileConfig> TILES = new HashMap<>() {{
        put(TileType.START, new TileConfig() {{ DECK_COUNT = 1; }});
        put(TileType.ROAD, new TileConfig() {{ DECK_COUNT = 10; }});
    }};

    public boolean validate() {
        if (MIN_PLAYERS < 0 || MIN_PLAYERS > MAX_PLAYERS)
            return false;
        if (PLAYER_DECK_CAPACITY < 1)
            return false;
        List<TileType> tileTypes = new ArrayList<>(List.of(TileType.values()));
        for (Map.Entry<TileType, TileConfig> tile : TILES.entrySet()) {
            if (tileTypes.contains(tile.getKey()))
                tileTypes.remove(tile.getKey());
            else
                return false;

            if (tile.getValue().DECK_COUNT < 0)
                return false;
        }

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
