package logic;

import input.ai.SimpleAI;
import logic.config.GameConfig;
import logic.exception.TooManyPlayerException;
import logic.player.Player;
import logic.player.PlayerInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void testGame(){
        // TODO LOAD A SPECIFIC CONFIG
        GameConfig gameConfig = new GameConfig();
        Game game = new Game(gameConfig);

        assertNotNull(game.getGameConfig());
        assertEquals(gameConfig, game.getGameConfig());
        assertEquals(0, game.getPlayerCount());
        assertNotNull(game.getGameBoard());
    }

    @Test
    void testPlayer() {
        // TODO LOAD A SPECIFIC CONFIG 3 PLAYERS
        GameConfig gameConfig = new GameConfig();
        Game game = new Game(gameConfig);

        assertEquals(0, game.getPlayerCount());

        PlayerInfo playerInfo = new PlayerInfo(0);
        SimpleAI simpleAI = new SimpleAI();
        Player player = new Player(playerInfo, simpleAI);
        game.addPlayer(player);

        assertEquals(1, game.getPlayerCount());
        assertEquals(player, game.getPlayer(0));
        assertDoesNotThrow(() -> {
            game.getPlayer(Integer.MAX_VALUE);
        });
        assertDoesNotThrow(() -> {
            game.getPlayer(Integer.MIN_VALUE);
        });
        assertNull(game.getPlayer(Integer.MAX_VALUE));
        assertNull(game.getPlayer(Integer.MIN_VALUE));
        game.addPlayer(player);
        game.addPlayer(player);
        assertThrows(TooManyPlayerException.class, () -> {
            game.addPlayer(player);
        });
    }

    @Test
    void testUpdate(){
        // TODO LOAD A SPECIFIC CONFIG
        GameConfig gameConfig = new GameConfig();
        Game game = new Game(gameConfig);

        assertThrows(IllegalStateException.class, game::update);
        game.start();
        assertDoesNotThrow(game::update);
    }

    @Test
    void testIsGameFinished(){
        // TODO LOAD A SPECIFIC CONFIG
        GameConfig gameConfig = new GameConfig();
        Game game = new Game(gameConfig);

        assertTrue(game.isFinished());
        game.start();
        assertFalse(game.isFinished());
    }
}
