package logic.command;

import logic.Game;
import logic.TestUtils;
import logic.board.GameBoard;
import logic.config.GameConfig;
import logic.player.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlaceTileDrawnCommandTest {
    @Test
    void testInvalidPosition() {
        Game game = TestUtils.initGameEnv(5, false, true);
        assertTrue(game.getCommandExecutor().execute(new PlaceTileDrawnCommand(GameBoard.STARTING_TILE_POSITION)));
        assertFalse(game.getCommandExecutor().execute(new PlaceTileDrawnCommand(GameBoard.STARTING_TILE_POSITION.add(10, 10))));
    }

    @Test
    void testNoStartingTilePlacement() {
        Game game = TestUtils.initGameEnv(5, false, true);
        assertFalse(game.getCommandExecutor().execute(new PlaceTileDrawnCommand(GameBoard.STARTING_TILE_POSITION.add(10, 10))));
    }

    @Test
    void testOverlapTilePlacement() {
        Game game = TestUtils.initGameEnv(5, false, true);
        assertTrue(game.getCommandExecutor().execute(new PlaceTileDrawnCommand(GameBoard.STARTING_TILE_POSITION)));
        assertFalse(game.getCommandExecutor().execute(new PlaceTileDrawnCommand(GameBoard.STARTING_TILE_POSITION)));
    }
}
