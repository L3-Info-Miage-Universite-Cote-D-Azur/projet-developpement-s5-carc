package logic.command;

import logic.Game;
import logic.TestUtils;
import logic.board.GameBoard;
import logic.state.GameStateType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SkipMeeplePlacementCommandTest {
    @Test
    void testPlacement() {
        Game game = TestUtils.initGameEnv(5, false, true);

        assertTrue(game.executeCommand(new PlaceTileDrawnCommand(GameBoard.STARTING_TILE_POSITION)));
        TestUtils.assertState(game, GameStateType.TURN_PLACE_MEEPLE);
        assertTrue(game.executeCommand(new SkipMeeplePlacementCommand()));

        TestUtils.skipStateIfNeeded(game, GameStateType.TURN_MOVE_DRAGON);
        TestUtils.assertState(game, GameStateType.TURN_PLACE_TILE);
    }

    @Test
    void testPlacementOnOccupiedTile() {
        Game game = TestUtils.initGameEnv(5, false, true);

        assertTrue(game.executeCommand(new PlaceTileDrawnCommand(GameBoard.STARTING_TILE_POSITION)));
        assertTrue(game.executeCommand(new SkipMeeplePlacementCommand()));

        TestUtils.skipStateIfNeeded(game, GameStateType.TURN_MOVE_DRAGON);
        TestUtils.assertState(game, GameStateType.TURN_PLACE_TILE);

        assertFalse(game.executeCommand(new SkipMeeplePlacementCommand()));
    }

    @Test
    void testMultiplePlacementsOnSameTurn() {
        Game game = TestUtils.initGameEnv(5, false, true);

        assertTrue(game.executeCommand(new PlaceTileDrawnCommand(GameBoard.STARTING_TILE_POSITION)));
        assertTrue(game.executeCommand(new SkipMeeplePlacementCommand()));
        assertFalse(game.executeCommand(new SkipMeeplePlacementCommand()));
    }
}