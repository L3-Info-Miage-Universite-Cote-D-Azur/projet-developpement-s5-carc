package logic.config;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import logic.tile.Chunk;
import logic.tile.ChunkType;
import logic.tile.Tile;

public class GameConfig {
    public int MIN_PLAYERS = 2;
    public int MAX_PLAYERS = 5;
    public ArrayList<TileConfig> TILES = new ArrayList<>();

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
