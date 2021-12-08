package logic.command;

import logic.Game;
import logic.TestUtils;
import logic.board.GameBoard;
import logic.state.GameStateType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlaceTileDrawnCommandTest {
    @Test
    void testInvalidPosition() {
        Game game = TestUtils.initGameEnv(5, false, true);
        assertTrue(game.executeCommand(new PlaceTileDrawnCommand(GameBoard.STARTING_TILE_POSITION)));
        assertTrue(game.executeCommand(new SkipMeeplePlacementCommand()));
        TestUtils.skipStateIfNeeded(game, GameStateType.TURN_MOVE_DRAGON);
        assertFalse(game.executeCommand(new PlaceTileDrawnCommand(GameBoard.STARTING_TILE_POSITION.add(10, 10))));
    }

    @Test
    void testNoStartingTilePlacement() {
        Game game = TestUtils.initGameEnv(5, false, true);
        assertFalse(game.executeCommand(new PlaceTileDrawnCommand(GameBoard.STARTING_TILE_POSITION.add(10, 10))));
    }

    @Test
    void testOverlapTilePlacement() {
        Game game = TestUtils.initGameEnv(5, false, true);
        assertTrue(game.executeCommand(new PlaceTileDrawnCommand(GameBoard.STARTING_TILE_POSITION)));
        assertTrue(game.executeCommand(new SkipMeeplePlacementCommand()));
        TestUtils.skipStateIfNeeded(game, GameStateType.TURN_MOVE_DRAGON);
        assertFalse(game.executeCommand(new PlaceTileDrawnCommand(GameBoard.STARTING_TILE_POSITION)));
    }
}
