package logic.state;

import logic.Game;
import logic.TestUtils;
import logic.board.GameBoard;
import logic.command.PlaceTileDrawnCommand;
import logic.state.turn.GameTurnEndingState;
import logic.state.turn.GameTurnInitState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {
    @Test
    void testInitialGameStateTransition() {
        Game game = TestUtils.initGameEnv(5, false, false);
        assertNull(game.getState());

        game.start();
        TestUtils.assertState(game, GameStateType.TURN_PLACE_TILE);

        game.setState(new GameStartState(game));
        TestUtils.assertState(game, GameStateType.TURN_PLACE_TILE);
    }

    @Test
    void testPlaceTileTransition() {
        Game game = TestUtils.initGameEnv(5, false, false);

        game.start();
        TestUtils.assertState(game, GameStateType.TURN_PLACE_TILE);

        TestUtils.placeTileRandomly(game);
        TestUtils.assertState(game, GameStateType.TURN_PLACE_MEEPLE);
    }

    @Test
    void testExtraTurnStateTransition() {
        Game game = TestUtils.initGameEnv(5, false, false);
        game.start();

        TestUtils.assertState(game, GameStateType.TURN_PLACE_TILE);

        game.executeCommand(new PlaceTileDrawnCommand(GameBoard.STARTING_TILE_POSITION));
        TestUtils.assertState(game, GameStateType.TURN_PLACE_MEEPLE);

        int turnCount = game.getTurnCount();

        TestUtils.skipStateIfNeeded(game, GameStateType.TURN_PLACE_MEEPLE);
        TestUtils.skipStateIfNeeded(game, GameStateType.TURN_MOVE_DRAGON);

        TestUtils.assertState(game, GameStateType.TURN_PLACE_TILE);

        assertEquals(turnCount + 1, game.getTurnCount());

        /* Clears the stack so the next state after extra action will be game over */
        game.getStack().clear();

        TestUtils.assertState(game, GameStateType.TURN_PLACE_TILE);
        TestUtils.placeTileRandomly(game);
        TestUtils.assertState(game, GameStateType.TURN_PLACE_MEEPLE);
        TestUtils.skipStateIfNeeded(game, GameStateType.TURN_PLACE_MEEPLE);
        TestUtils.skipStateIfNeeded(game, GameStateType.TURN_MOVE_DRAGON);
        TestUtils.assertState(game, GameStateType.OVER);

        game = TestUtils.initGameEnv(5, false, false);
        game.start();
        game.setMaster(false);

        TestUtils.assertState(game, GameStateType.TURN_PLACE_TILE);
        TestUtils.placeTileRandomly(game);
        TestUtils.skipStateIfNeeded(game, GameStateType.TURN_PLACE_MEEPLE);
        TestUtils.skipStateIfNeeded(game, GameStateType.TURN_MOVE_DRAGON);
        TestUtils.assertState(game, GameStateType.TURN_WAITING_MASTER_DATA);
    }

    @Test
    void testWaitingMasterDataStateTransition() {
        Game game = TestUtils.initGameEnv(5, false, false);

        game.start();
        game.setMaster(false);

        TestUtils.assertState(game, GameStateType.TURN_PLACE_TILE);
        TestUtils.placeTileRandomly(game);
        TestUtils.assertState(game, GameStateType.TURN_PLACE_MEEPLE);
        TestUtils.skipStateIfNeeded(game, GameStateType.TURN_PLACE_MEEPLE);
        TestUtils.skipStateIfNeeded(game, GameStateType.TURN_MOVE_DRAGON);
        TestUtils.assertState(game, GameStateType.TURN_WAITING_MASTER_DATA);
        TestUtils.skipStateIfNeeded(game, GameStateType.TURN_WAITING_MASTER_DATA);
        TestUtils.assertState(game, GameStateType.TURN_PLACE_TILE);

        game.getStack().clear();

        TestUtils.placeTileRandomly(game);
        TestUtils.skipStateIfNeeded(game, GameStateType.TURN_PLACE_MEEPLE);
        TestUtils.skipStateIfNeeded(game, GameStateType.TURN_MOVE_DRAGON);
        TestUtils.assertState(game, GameStateType.TURN_WAITING_MASTER_DATA);
        TestUtils.skipStateIfNeeded(game, GameStateType.TURN_WAITING_MASTER_DATA);
        TestUtils.assertState(game, GameStateType.OVER);
    }

    @Test
    void testUncompleteableState() {
        try {
            new GameStartState(TestUtils.initGameEnv(2, false, false)).complete();
            fail("Expected IllegalStateException to be thrown");
        }
        catch (IllegalStateException e){
            // Test exception message...
        }
        try {
            new GameOverState(TestUtils.initGameEnv(2, false, false)).complete();
            fail("Expected IllegalStateException to be thrown");
        }
        catch (IllegalStateException e){
            // Test exception message...
        }
        try {
            new GameTurnInitState(TestUtils.initGameEnv(2, false, false)).complete();
            fail("Expected IllegalStateException to be thrown");
        }
        catch (IllegalStateException e){
            // Test exception message...
        }
        try {
            new GameTurnEndingState(TestUtils.initGameEnv(2, false, false)).complete();
            fail("Expected IllegalStateException to be thrown");
        }
        catch (IllegalStateException e){
            // Test exception message...
        }
    }
}
