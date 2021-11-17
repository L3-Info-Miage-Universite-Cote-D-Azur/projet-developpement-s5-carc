package logic;

import logic.config.GameConfig;
import logic.player.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTurnTest {
    private static GameConfig config = GameConfig.loadFromDirectory("config");

    @Test
    void testInitialState() {
        Game game = new Game(config);
        GameTurn gameTurn = new GameTurn(game);

        assertEquals(0, gameTurn.getCount());
    }

    @Test
    void testStart() {
        Game game = new Game(config);
        game.addPlayer(createFakePlayer(0));
        GameTurn gameTurn = new GameTurn(game);
        assertEquals(0, gameTurn.getCount());
        gameTurn.playTurn();
        assertEquals(1, gameTurn.getCount());
    }

    @Test
    void testGetPlayerIndex() {
        Game game = new Game(config);

        game.addPlayer(createFakePlayer(1));
        game.addPlayer(createFakePlayer(2));
        game.addPlayer(createFakePlayer(3));

        GameTurn gameTurn = new GameTurn(game);

        assertEquals(-1, gameTurn.getPlayerIndex());
        gameTurn.playTurn();

        assertEquals(0, gameTurn.getPlayerIndex());
        gameTurn.playTurn();

        assertEquals(1, gameTurn.getPlayerIndex());

        for (int i = 0; i < 15; i++)
            gameTurn.playTurn();

        assertEquals(17, gameTurn.getCount());
        assertEquals(1, gameTurn.getPlayerIndex());
    }

    private static Player createFakePlayer(int id) {
        return new Player(id);
    }
}
