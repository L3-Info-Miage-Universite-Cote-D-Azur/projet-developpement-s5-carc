package logic.player;

import input.ai.SimpleAI;
import logic.Game;
import logic.config.GameConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    @Test
    void testPlayer() {
        PlayerInfo playerInfo = new PlayerInfo(0);
        SimpleAI simpleAI = new SimpleAI();
        Game game = new Game(new GameConfig());
        Player player0 = new Player(playerInfo, simpleAI, game);
        assertEquals(0, player0.getScore());
        assertNotNull(player0.getInfo());
        assertEquals(game, player0.getGame());
    }

    @Test
    void testAddScore() {
        // Substract
        PlayerInfo playerInfo = new PlayerInfo(0);
        SimpleAI simpleAI = new SimpleAI();
        Game game = new Game(new GameConfig());
        Player player0 = new Player(playerInfo, simpleAI, game);
        int score0 = 10;
        player0.addScore(score0);
        assertEquals(score0, player0.getScore());
        int score1 = 1000;
        player0.addScore(score1);
        assertEquals(score1 + score0, player0.getScore());
    }
}
