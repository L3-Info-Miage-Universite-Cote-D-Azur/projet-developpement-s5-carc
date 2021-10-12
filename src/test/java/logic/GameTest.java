package logic;

import input.ai.SimpleAI;
import logic.config.GameConfig;
import logic.player.Player;
import logic.player.PlayerInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void testGame(){
        GameConfig gameConfig = new GameConfig();
        Game game = new Game(gameConfig);

        assertNotNull(game.getGameConfig());
        assertEquals(gameConfig, game.getGameConfig());
    }

    @Test
    void testPlayer(){
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
    }

    @Test
    void testGameBoard(){
        GameConfig gameConfig = new GameConfig();
        Game game = new Game(gameConfig);

        assertNotNull(game.getGameBoard());
    }

    @Test
    void testIsGameFinished(){
        GameConfig gameConfig = new GameConfig();
        Game game = new Game(gameConfig);

        assertTrue(game.isGameFinished());
        game.start();
        assertFalse(game.isGameFinished());
    }
}
