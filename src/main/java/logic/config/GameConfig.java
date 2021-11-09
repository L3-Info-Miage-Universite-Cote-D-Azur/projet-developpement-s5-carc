package logic.config;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import logic.tile.ChunkType;

public class GameConfig {
    public int MIN_PLAYERS = 2;
    public int MAX_PLAYERS = 5;
    public ArrayList<TileConfig> TILES = new ArrayList<>() {{
        add(new TileConfig() {{
            center = ChunkType.ROAD;
            left = ChunkType.ROAD;
            right = ChunkType.ROAD;
            up = ChunkType.TOWN;
            down = ChunkType.FIELD;
            isStartingTile = true;
        }});
        add(new TileConfig() {{
            center = ChunkType.ROAD;
            left = ChunkType.FIELD;
            right = ChunkType.FIELD;
            up = ChunkType.ROAD;
            down = ChunkType.ROAD;
        }});
    }};

    public boolean validate() {
        if (MIN_PLAYERS < 0 || MIN_PLAYERS > MAX_PLAYERS)
            return false;

        for (TileConfig tile : TILES) {
            if (!tile.validate()) {
                return false;
            }
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
