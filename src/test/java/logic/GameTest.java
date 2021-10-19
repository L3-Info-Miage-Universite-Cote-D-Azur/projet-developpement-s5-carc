package logic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import input.ai.SimpleAI;
import logic.config.GameConfig;
import logic.exception.TooManyPlayerException;
import logic.player.Player;
import logic.player.PlayerInfo;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private static final String config0 = "{\"MIN_PLAYERS\":2,\"MAX_PLAYERS\":3,\"PLAYER_DECK_CAPACITY\":25,\"TILES\":{\"START\": { \"DECK_COUNT\": 5 }, \"ROAD\": { \"DECK_COUNT\": 5 }, \"FIELD\": { \"DECK_COUNT\": 5 }, \"TOWN_CHUNK\": { \"DECK_COUNT\": 5 }, \"RIVER\": { \"DECK_COUNT\": 5 } }}";

    @Test
    void testGame(){
        GameConfig gameConfig = new GameConfig();
        Game game = new Game(gameConfig);

        assertNotNull(game.getGameConfig());
        assertEquals(gameConfig, game.getGameConfig());
        assertEquals(0, game.getPlayerCount());
        assertNotNull(game.getGameBoard());
    }

    @Test
    void testPlayer() {
        GameConfig gameConfig0 = null;

        try {
            gameConfig0 = new ObjectMapper().readValue(config0, GameConfig.class);
        } catch (JsonProcessingException ignored) {
        }

        assertNotNull(gameConfig0);
        Game game = new Game(gameConfig0);

        assertEquals(0, game.getPlayerCount());

        PlayerInfo playerInfo = new PlayerInfo(0);
        SimpleAI simpleAI = new SimpleAI();
        Player player = new Player(playerInfo, simpleAI);
        game.addPlayer(player);

        assertEquals(1, game.getPlayerCount());
        assertEquals(player, game.getPlayer(0));

        game.addPlayer(player);
        game.addPlayer(player);
        assertThrows(TooManyPlayerException.class, () -> {
            game.addPlayer(player);
        });
    }

    @Test
    void testUpdate(){
        GameConfig gameConfig = new GameConfig();
        Game game = new Game(gameConfig);
        PlayerInfo playerInfo = new PlayerInfo(0);
        SimpleAI simpleAI = new SimpleAI();
        Player player = new Player(playerInfo, simpleAI);
        game.addPlayer(player);
        assertThrows(IllegalStateException.class, game::update);
        game.start();
        assertDoesNotThrow(game::update);
    }

    @Test @Disabled
    void testIsGameFinished(){
        GameConfig gameConfig = new GameConfig();
        Game game = new Game(gameConfig);

        assertTrue(game.isFinished());
        game.start();
        assertFalse(game.isFinished());
    }
}
