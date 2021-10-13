package logic;

import input.ai.SimpleAI;
import logic.config.GameConfig;
import logic.player.Player;
import logic.player.PlayerInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTurnTest {
    @Test
    void testGameTurn() {
        // TODO LOAD A SPECIFIC CONFIG
        GameConfig gameConfig = new GameConfig();
        Game game = new Game(gameConfig);
        GameTurn gameTurn = new GameTurn(game);

        assertEquals(-1, gameTurn.getCount());
    }

    @Test
    void testStart() {
        // TODO LOAD A SPECIFIC CONFIG
        GameConfig gameConfig = new GameConfig();
        Game game = new Game(gameConfig);
        GameTurn gameTurn = new GameTurn(game);
        assertEquals(0, gameTurn.getCount());
        gameTurn.startNext();
        assertEquals(1, gameTurn.getCount());
    }

    @Test
    void testGetPlayerIndex() {
        // TODO LOAD A SPECIFIC CONFIG 3 PLAYER MIN
        GameConfig gameConfig = new GameConfig();
        Game game = new Game(gameConfig);
        Player player0 = new Player(new PlayerInfo(0), new SimpleAI());
        Player player1 = new Player(new PlayerInfo(1), new SimpleAI());
        Player player2 = new Player(new PlayerInfo(2), new SimpleAI());
        game.addPlayer(player0);
        game.addPlayer(player1);
        game.addPlayer(player2);
        GameTurn gameTurn = new GameTurn(game);

        assertEquals(-1, gameTurn.getPlayerIndex());
        gameTurn.startNext();

        assertEquals(0, gameTurn.getPlayerIndex());
        gameTurn.startNext();

        assertEquals(1, gameTurn.getPlayerIndex());

        for (int i = 0; i < 15; i++)
            gameTurn.startNext();

        assertEquals(16, gameTurn.getCount());
        assertEquals(1, gameTurn.getPlayerIndex());
    }
}
