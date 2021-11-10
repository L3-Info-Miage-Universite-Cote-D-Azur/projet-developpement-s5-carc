package logic.config;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import logic.tile.ChunkType;

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

    public static GameConfig loadFromJSON(String json) {
        try {
            return new ObjectMapper().readValue(json, GameConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static GameConfig loadFromFile(String path) {
        try {
            return new ObjectMapper().readValue(Paths.get(path).toFile(), GameConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
