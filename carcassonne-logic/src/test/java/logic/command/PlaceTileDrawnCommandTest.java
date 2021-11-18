package logic.command;

import logic.Game;
import logic.board.GameBoard;
import logic.config.GameConfig;
import logic.player.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlaceTileDrawnCommandTest {
    private static final GameConfig config = GameConfig.loadFromResources();

    @Test
    void testInvalidPosition() {
        Game game = createGameEnv();
        assertTrue(game.getCommandExecutor().execute(new PlaceTileDrawnCommand(GameBoard.STARTING_TILE_POSITION)));
        assertFalse(game.getCommandExecutor().execute(new PlaceTileDrawnCommand(GameBoard.STARTING_TILE_POSITION.add(10, 10))));
    }

    @Test
    void testNoStartingTilePlacement() {
        Game game = createGameEnv();
        assertFalse(game.getCommandExecutor().execute(new PlaceTileDrawnCommand(GameBoard.STARTING_TILE_POSITION.add(10, 10))));
    }

    @Test
    void testOverlapTilePlacement() {
        Game game = createGameEnv();
        assertTrue(game.getCommandExecutor().execute(new PlaceTileDrawnCommand(GameBoard.STARTING_TILE_POSITION)));
        assertFalse(game.getCommandExecutor().execute(new PlaceTileDrawnCommand(GameBoard.STARTING_TILE_POSITION)));
    }

    private static Game createGameEnv() {
        Game game = new Game(config);

        game.addPlayer(new Player(0));
        game.addPlayer(new Player(1));

        game.start();

        return game;
    }
}
