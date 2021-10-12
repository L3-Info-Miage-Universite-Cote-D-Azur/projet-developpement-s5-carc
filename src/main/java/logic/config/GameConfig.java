package logic.config;

import com.fasterxml.jackson.databind.JsonNode;
import logic.tile.TileType;

import java.io.File;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GameConfig {
    public class TileConfig {
        private final int DECK_COUNT;

        public TileConfig(int DECK_COUNT) {
            this.DECK_COUNT = DECK_COUNT;
        }

        public int getDECK_COUNT() {
            return DECK_COUNT;
        }
    }

    private int MAX_PLAYERS;
    private int MIN_PLAYERS;
    private int PLAYER_DECK_CAPACITY;
    private HashMap<TileType, TileConfig> TILES;

    public GameConfig(){
        loadConfig();
    }

    private boolean loadConfig() {
        ObjectMapper config = new ObjectMapper();

        // All try will be moved later into signature
        try {
            JsonNode jsonNode = config.readTree(new File("config.json"));
            MAX_PLAYERS = Integer.parseInt(jsonNode.get("MAX_PLAYERS").asText());
            MIN_PLAYERS = Integer.parseInt(jsonNode.get("MIN_PLAYERS").asText());
            PLAYER_DECK_CAPACITY = Integer.parseInt(jsonNode.get("PLAYER_DECK_CAPACITY").asText());
            for (TileType tileType : TileType.values()) {
                JsonNode countNode = jsonNode.get(tileType.name());
                int tile = Integer.parseInt(countNode.get("DECK_COUNT").asText());
                TileConfig tileConfig = new TileConfig(tile);
                //TILES.put(ADD TILE TYPE, tileConfig);
            }
        } catch (Exception e){
            // TODO REMOVE ERROR MESSAGE ?
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean validate() {
        if (MIN_PLAYERS < 0 || MAX_PLAYERS < MIN_PLAYERS)
            return false;
        if (PLAYER_DECK_CAPACITY < 1) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GameConfig{" +
                "MAX_PLAYERS=" + MAX_PLAYERS +
                ", MIN_PLAYERS=" + MIN_PLAYERS +
                ", PLAYER_DECK_CAPACITY=" + PLAYER_DECK_CAPACITY +
                ", TILES=" + TILES +
                '}';
    }
}
