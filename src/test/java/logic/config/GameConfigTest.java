package logic.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import logic.tile.TileType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameConfigTest {
    private static final String config0 = "{\"MIN_PLAYERS\":2,\"MAX_PLAYERS\":5,\"PLAYER_DECK_CAPACITY\":25,\"TILES\":{\"START\": { \"DECK_COUNT\": 5 }, \"ROAD\": { \"DECK_COUNT\": 5 }, \"RIVER\": { \"DECK_COUNT\": 5 }, \"TOWN_CHUNK\": { \"DECK_COUNT\": 5 }}}";
    private static final String config1 = "{\"MIN_PLAYERS\":-5,\"MAX_PLAYERS\":5,\"PLAYER_DECK_CAPACITY\":25,\"TILES\":{\"START\": { \"DECK_COUNT\": 5 }, \"ROAD\": { \"DECK_COUNT\": 5 }, \"RIVER\": { \"DECK_COUNT\": 5 }, \"TOWN_CHUNK\": { \"DECK_COUNT\": 5 }}}";
    private static final String config2 = "{\"MIN_PLAYERS\":5,\"MAX_PLAYERS\":3,\"PLAYER_DECK_CAPACITY\":25,\"TILES\":{\"START\": { \"DECK_COUNT\": 5 }, \"ROAD\": { \"DECK_COUNT\": 5 }, \"RIVER\": { \"DECK_COUNT\": 5 }, \"TOWN_CHUNK\": { \"DECK_COUNT\": 5 }}}";
    private static final String config3 = "{\"MIN_PLAYERS\":2,\"MAX_PLAYERS\":5,\"PLAYER_DECK_CAPACITY\":-1,\"TILES\":{\"START\": { \"DECK_COUNT\": 5 }, \"ROAD\": { \"DECK_COUNT\": 5 }, \"RIVER\": { \"DECK_COUNT\": 5 }, \"TOWN_CHUNK\": { \"DECK_COUNT\": 5 }}}";
    private static final String config4 = "{\"MIN_PLAYERS\":2,\"MAX_PLAYERS\":5,\"PLAYER_DECK_CAPACITY\":25,\"TILES\":{\"WHAT\": { \"DECK_COUNT\": 5 }, \"ROAD\": { \"DECK_COUNT\": 5 }, \"RIVER\": { \"DECK_COUNT\": 5 }, \"TOWN_CHUNK\": { \"DECK_COUNT\": 5 }}}";
    private static final String config5 = "{\"MIN_PLAYERS\":2,\"MAX_PLAYERS\":5,\"PLAYER_DECK_CAPACITY\":25,\"TILES\":{\"ROAD\": { \"DECK_COUNT\": 5 }, \"ROAD\": { \"DECK_COUNT\": 5 }, \"RIVER\": { \"DECK_COUNT\": 5 }, \"TOWN_CHUNK\": { \"DECK_COUNT\": 5 }}}";
    private static final String config6 = "{\"MIN_PLAYERS\":2,\"MAX_PLAYERS\":5,\"PLAYER_DECK_CAPACITY\":25,\"TILES\":{\"START\": { \"DECK_COUNT\": 5 }, \"ROAD\": { \"DECK_COUNT\": -1 }, \"RIVER\": { \"DECK_COUNT\": 5 }, \"TOWN_CHUNK\": { \"DECK_COUNT\": 5 }}}";

    @Test
    void testGameConfig() {
        GameConfig config = null;
        try {
            config = new ObjectMapper().readValue(config0, GameConfig.class);
        } catch (JsonProcessingException e) {
        }

        assertNotNull(config);
        assertEquals(2, config.MIN_PLAYERS);
        assertEquals(5, config.MAX_PLAYERS);
        assertEquals(25, config.PLAYER_DECK_CAPACITY);
        assertEquals(5, config.TILES.get(TileType.START).DECK_COUNT);
        assertEquals(5, config.TILES.get(TileType.RIVER).DECK_COUNT);
        assertEquals(5, config.TILES.get(TileType.ROAD).DECK_COUNT);
        assertEquals(5, config.TILES.get(TileType.TOWN_CHUNK).DECK_COUNT);
    }

    @Test
    void testValidate() {
        GameConfig gameConfig0 = null;
        GameConfig gameConfig1 = null;
        GameConfig gameConfig2 = null;
        GameConfig gameConfig3 = null;
        GameConfig gameConfig4 = null;
        GameConfig gameConfig5 = null;
        GameConfig gameConfig6 = null;

        try {
            gameConfig0 = new ObjectMapper().readValue(config0, GameConfig.class);
            gameConfig1 = new ObjectMapper().readValue(config1, GameConfig.class);
            gameConfig2 = new ObjectMapper().readValue(config2, GameConfig.class);
            gameConfig3 = new ObjectMapper().readValue(config3, GameConfig.class);
            gameConfig4 = new ObjectMapper().readValue(config4, GameConfig.class);
            gameConfig5 = new ObjectMapper().readValue(config5, GameConfig.class);
            gameConfig6 = new ObjectMapper().readValue(config6, GameConfig.class);
        } catch (JsonProcessingException ignored) {
        }

        assertNotNull(gameConfig0);
        assertTrue(gameConfig0.validate());
        assertNotNull(gameConfig1);
        assertFalse(gameConfig1.validate());
        assertNotNull(gameConfig2);
        assertFalse(gameConfig2.validate());
        assertNotNull(gameConfig3);
        assertFalse(gameConfig3.validate());
        assertNull(gameConfig4);
        assertNull(gameConfig5);
        assertNull(gameConfig6);
    }
}
