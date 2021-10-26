package logic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import input.ai.SimpleAI;
import logic.config.GameConfig;
import logic.exception.TooManyPlayerException;
import logic.player.PlayerInfo;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private static final String config0 = "{\"MIN_PLAYERS\":2,\"MAX_PLAYERS\":3,\"PLAYER_DECK_CAPACITY\":25,\"TILES\":{\"START\": { \"DECK_COUNT\": 5 }, \"ROAD\": { \"DECK_COUNT\": 5 }, \"TOWN_CHUNK\": { \"DECK_COUNT\": 5 }, \"RIVER\": { \"DECK_COUNT\": 5 } }}";

    @Test
    void testGame(){
        GameConfig gameConfig = new GameConfig();
        Game game = new Game(gameConfig);

        assertNotNull(game.getConfig());
        assertEquals(gameConfig, game.getConfig());
        assertEquals(0, game.getPlayerCount());
        assertNotNull(game.getBoard());
        assertNotNull(game.getStack());
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

        PlayerInfo playerInfo0 = new PlayerInfo(500);
        PlayerInfo playerInfo1 = new PlayerInfo(501);
        PlayerInfo playerInfo2 = new PlayerInfo(502);
        PlayerInfo playerInfo3 = new PlayerInfo(503);
        SimpleAI simpleAI = new SimpleAI();
        game.createPlayer(playerInfo0, simpleAI);

        assertEquals(1, game.getPlayerCount());
        //assertEquals(player, game.getPlayer(0)); TODO Test player info

        game.createPlayer(playerInfo1, simpleAI);
        game.createPlayer(playerInfo2, simpleAI);
        assertEquals(3, game.getPlayerCount());
        assertNotNull(game.getPlayer(0));
        assertNotNull(game.getPlayer(1));
        assertNotNull(game.getPlayer(2));
        assertThrows(TooManyPlayerException.class, () -> {
            game.createPlayer(playerInfo3, simpleAI);
        });
    }

    @Test
    void testUpdate(){
        GameConfig gameConfig = new GameConfig();
        Game game = new Game(gameConfig);
        PlayerInfo playerInfo = new PlayerInfo(0);
        SimpleAI simpleAI = new SimpleAI();
        game.createPlayer(playerInfo, simpleAI);
        assertThrows(IllegalStateException.class, game::update);

        GameConfig gameConfig1 = new GameConfig();
        Game game1 = new Game(gameConfig1);
        PlayerInfo playerInfo1 = new PlayerInfo(0);
        SimpleAI simpleAI1 = new SimpleAI();
        game1.createPlayer(playerInfo1, simpleAI1);
        game1.start();
        assertDoesNotThrow(game1::update);
    }

    @Test @Disabled
    void testIsGameFinished() {
        GameConfig gameConfig = new GameConfig();
        Game game = new Game(gameConfig);

        assertTrue(game.isFinished());
        game.start();
        assertFalse(game.isFinished());
    }
    @Test
    void testIfThrowExceptionIfStartCalledSeveralTimes(){
        GameConfig gameConfig = new GameConfig();
        Game game = new Game(gameConfig);

        assertTrue(game.isFinished());
        game.start();
        assertThrows(IllegalStateException.class, game::start);
    }
    @Test
    void testIfThrowExceptionIfWinnerCalledWhenGameNotFinished(){
        GameConfig gameConfig = new GameConfig();
        Game game = new Game(gameConfig);

        assertTrue(game.isFinished());
        game.start();
        assertThrows(IllegalStateException.class, game::getWinner);
    }
}
