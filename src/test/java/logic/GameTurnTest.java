package logic;

import logic.config.GameConfig;
import logic.player.SimpleAIPlayer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTurnTest {
    @Test
    void testGameTurn() {
        GameConfig gameConfig = new GameConfig();
        Game game = new Game(gameConfig);
        GameTurn gameTurn = new GameTurn(game);

        assertEquals(0, gameTurn.getCount());
    }

    @Test
    void testStart() {
        GameConfig gameConfig = new GameConfig();
        Game game = new Game(gameConfig);
        GameTurn gameTurn = new GameTurn(game);
        assertEquals(0, gameTurn.getCount());
        gameTurn.startNext();
        assertEquals(1, gameTurn.getCount());
    }

    @Test
    void testGetPlayerIndex() {
        GameConfig gameConfig = new GameConfig();
        Game game = new Game(gameConfig);

        game.createPlayer(new SimpleAIPlayer(1));
        game.createPlayer(new SimpleAIPlayer(1));
        game.createPlayer(new SimpleAIPlayer(1));

        GameTurn gameTurn = new GameTurn(game);

        assertEquals(-1, gameTurn.getPlayerIndex());
        gameTurn.startNext();

        assertEquals(0, gameTurn.getPlayerIndex());
        gameTurn.startNext();

        assertEquals(1, gameTurn.getPlayerIndex());

        for (int i = 0; i < 15; i++)
            gameTurn.startNext();

        assertEquals(17, gameTurn.getCount());
        assertEquals(1, gameTurn.getPlayerIndex());
    }
}
