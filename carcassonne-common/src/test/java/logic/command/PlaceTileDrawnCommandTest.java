package logic.command;

import logic.Game;
import logic.TestUtils;
import logic.board.GameBoard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlaceTileDrawnCommandTest {
    @Test
    void testInvalidPosition() {
        Game game = TestUtils.initGameEnv(5, false, true);
        assertTrue(game.getCommandExecutor().execute(new PlaceTileDrawnCommand(GameBoard.STARTING_TILE_POSITION)));
        assertTrue(game.getCommandExecutor().execute(new SkipMeeplePlacementCommand()));
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
        assertTrue(game.getCommandExecutor().execute(new SkipMeeplePlacementCommand()));
        assertFalse(game.getCommandExecutor().execute(new PlaceTileDrawnCommand(GameBoard.STARTING_TILE_POSITION)));
    }
}
