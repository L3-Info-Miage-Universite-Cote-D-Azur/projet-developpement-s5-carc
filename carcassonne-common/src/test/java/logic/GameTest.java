package logic;

import logic.board.GameBoard;
import logic.config.GameConfig;
import logic.exception.NotEnoughPlayerException;
import logic.exception.TooManyPlayerException;
import logic.player.Player;
import logic.state.GameOverState;
import logic.tile.Tile;
import logic.tile.chunk.Chunk;
import logic.tile.chunk.ChunkId;
import logic.tile.chunk.ChunkType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private static final GameConfig config = GameConfig.loadFromResources();

    @Test
    void testInitialState() {
        assertNotNull(config);
        Game game = new Game(config);

        assertEquals(config, game.getConfig());
        assertEquals(0, game.getPlayerCount());
        assertNotNull(game.getBoard());
        assertNotNull(game.getStack());
    }

    @Test
    void testAddPlayer() {
        assertNotNull(config);
        GameConfig gameConfig = new GameConfig(config.getTiles(), 1, 3, 7);

        assertNotNull(gameConfig);
        Game game = new Game(gameConfig);

        assertEquals(0, game.getPlayerCount());

        game.addPlayer(new Player(1));
        assertEquals(1, game.getPlayerCount());

        game.addPlayer(new Player(2));
        game.addPlayer(new Player(3));
        assertEquals(3, game.getPlayerCount());

        assertNotNull(game.getPlayer(0));
        assertNotNull(game.getPlayer(1));
        assertNotNull(game.getPlayer(2));

        try {
            game.addPlayer(new Player(4));
            fail("Expected TooManyPlayerException to be thrown");
        }
        catch (TooManyPlayerException e){
            // Test exception message...
        }
    }

    @Test
    void testPlayerList() {
        assertNotNull(config);
        Game game = new Game(config);

        Player player1 = new Player(501);
        Player player2 = new Player(502);

        game.addPlayer(player1);
        game.addPlayer(player2);

        assertEquals(2, game.getPlayerCount());
        assertEquals(player1, game.getPlayer(0));
        assertEquals(player2, game.getPlayer(1));

        assertEquals(0, game.getPlayerIndex(player1));
        assertEquals(1, game.getPlayerIndex(player2));
    }

    @Test
    void testIsGameFinished() {
        assertNotNull(config);
        GameConfig gameConfig = new GameConfig(config.getTiles(), 1, 3, config.getStartingMeepleCount());
        Game game = new Game(gameConfig);

        assertFalse(game.isOver());

        game.addPlayer(new Player(1));
        game.start();

        assertFalse(game.isOver());

        game.getPlayer(0).addScore(99999, ChunkType.ROAD);

        assertFalse(game.isOver());
    }

    @Test
    void testIfThrowExceptionWhenNotEnoughPlayers() {
        assertNotNull(config);
        Game game = new Game(new GameConfig(config.getTiles(), 1, 1, config.getStartingMeepleCount()));

        assertThrows(NotEnoughPlayerException.class, game::start);
        assertDoesNotThrow(() -> {
            game.addPlayer(new Player(1));
            game.start();
        });
    }

    @Test
    void testPreventStartingForSlave() {
        assertNotNull(config);
        Game game = new Game(config);

        game.addPlayer(new Player(501));
        game.addPlayer(new Player(502));
        game.setMaster(false);

        assertThrows(IllegalStateException.class, game::start);
    }

    @Test
    void testCloning() {
        Game game = TestUtils.initGameEnv(5, true, true);
        Game clonedGame = game.clone();

        assertEquals(game.getPlayerCount(), clonedGame.getPlayerCount());

        for (int i = 0; i < game.getPlayerCount(); i++) {
            Player originalPlayer = game.getPlayer(i);
            Player clonedPlayer = clonedGame.getPlayer(i);

            assertEquals(originalPlayer.getId(), clonedPlayer.getId());
            assertEquals(originalPlayer.getScore(), clonedPlayer.getScore());
        }

        GameBoard originalBoard = game.getBoard();
        GameBoard clonedBoard = clonedGame.getBoard();

        assertEquals(originalBoard.getTileCount(), clonedBoard.getTileCount());

        for (Tile originalTile : originalBoard.getTiles()) {
            Tile clonedTile = clonedBoard.getTileAt(originalTile.getPosition());

            assertEquals(originalTile.getPosition(), clonedTile.getPosition());
            assertEquals(originalTile.getConfig(), clonedTile.getConfig());

            for (ChunkId chunkId : ChunkId.values()) {
                Chunk originalChunk = originalTile.getChunk(chunkId);
                Chunk clonedChunk = clonedTile.getChunk(chunkId);

                assertEquals(originalChunk.getType(), clonedChunk.getType());

                if (originalChunk.getMeeple() != null) {
                    assertNotNull(clonedChunk.getMeeple());
                    assertEquals(originalChunk.getMeeple().getOwner().getId(), clonedChunk.getMeeple().getOwner().getId());
                } else {
                    assertNull(clonedChunk.getMeeple());
                }
            }
        }
    }

    @Test
    void testSlaveStackNonEncoding() {
        assertNotNull(config);
        Game game = new Game(config);

        game.addPlayer(new Player(501));
        game.addPlayer(new Player(502));
        game.start();

        assertTrue(game.isMaster());
        assertFalse(game.getStack().isEmpty());

        game.setMaster(false);

        Game clonedGame = game.clone();

        game.setMaster(true);

        assertTrue(game.isMaster());
        assertFalse(clonedGame.isMaster());

        assertTrue(clonedGame.getStack().isEmpty());
    }

    @Test
    void testGetTurnExecutor() {
        assertNotNull(config);
        Game game = new Game(config);

        game.addPlayer(new Player(501));
        game.addPlayer(new Player(502));

        assertThrows(IllegalStateException.class, game::getTurnExecutor);

        game.start();

        assertNotNull(game.getTurnExecutor());

        game.setState(new GameOverState(game));

        assertThrows(IllegalStateException.class, game::getTurnExecutor);
    }
}
