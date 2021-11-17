import logic.Game;
import logic.config.GameConfig;
import logic.exception.NotEnoughPlayerException;
import logic.exception.TooManyPlayerException;
import logic.player.SimpleAIPlayer;
import logic.tile.ChunkType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private static final GameConfig config = GameConfig.loadFromDirectory("config");

    @Test
    void testInitialState(){
        Game game = new Game(config);

        assertEquals(config, game.getConfig());
        assertEquals(0, game.getPlayerCount());
        assertNotNull(game.getBoard());
        assertNotNull(game.getStack());
    }

    @Test
    void testAddPlayer() {
        GameConfig gameConfig = new GameConfig(config.tiles, 1, 3, 7);

        assertNotNull(gameConfig);
        Game game = new Game(gameConfig);

        assertEquals(0, game.getPlayerCount());

        game.addPlayer(new SimpleAIPlayer(1));
        assertEquals(1, game.getPlayerCount());

        game.addPlayer(new SimpleAIPlayer(2));
        game.addPlayer(new SimpleAIPlayer(3));
        assertEquals(3, game.getPlayerCount());

        assertNotNull(game.getPlayer(0));
        assertNotNull(game.getPlayer(1));
        assertNotNull(game.getPlayer(2));

        assertThrows(TooManyPlayerException.class, () -> {
            game.addPlayer(new SimpleAIPlayer(4));
        });
    }

    @Test
    void testIsGameFinished() {
        GameConfig gameConfig = new GameConfig(config.tiles, 1, 3, config.startingMeepleCount);
        Game game = new Game(gameConfig);

        assertTrue(game.isFinished());

        game.addPlayer(new SimpleAIPlayer(1));
        game.start();

        assertFalse(game.isFinished());

        game.getPlayer(0).addScore(99999, ChunkType.ROAD);

        assertTrue(game.isFinished());
    }

    @Test
    void testIfThrowExceptionIfUpdateCalledBeforeStart() {
        Game game = new Game(config);

        assertTrue(game.isFinished());
        assertThrows(IllegalStateException.class, game::update);
    }

    @Test
    void testIfThrowExceptionIfWinnerCalledWhenGameNotFinished(){
        GameConfig gameConfig = new GameConfig(config.tiles, 1, 3, config.startingMeepleCount);
        Game game = new Game(gameConfig);

        game.addPlayer(new SimpleAIPlayer(1));
        game.start();

        assertEquals(false, game.isFinished());
        assertThrows(IllegalStateException.class, game::getWinner);
    }

    @Test
    void testWinner() {
        Game game = new Game(config);
        game.addPlayer(new SimpleAIPlayer(501));
        game.addPlayer(new SimpleAIPlayer(502));

        assertTrue(game.isFinished());

        game.start();
        game.getPlayer(0).addScore(279, ChunkType.TOWN);

        while (!game.isFinished()) {
            game.update();
        }

        assertNotNull(game.getWinner());
        assertEquals(game.getWinner(), game.getPlayer(0));
    }

    @Test
    void testIfThrowExceptionWhenNotEnoughPlayers() {
        Game game = new Game(new GameConfig(config.tiles, 1, 1, config.startingMeepleCount));

        assertThrows(NotEnoughPlayerException.class, () -> {
            game.start();
        });
        assertDoesNotThrow(() -> {
            game.addPlayer(new SimpleAIPlayer(1));
            game.start();
        });
    }
}
