package logic.state;

import logic.Game;
import logic.TestUtils;
import logic.board.GameBoard;
import logic.command.PlaceTileDrawnCommand;
import logic.state.turn.GameTurnExtraActionState;
import logic.state.turn.GameTurnPlaceTileState;
import logic.state.turn.GameTurnWaitingMasterDataState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class GameStateTest {
    @Test
    void testInitialGameStateTransition() {
        Game game = TestUtils.initGameEnv(5, false, false);
        assertNull(game.getState());

        game.start();
        assertEquals(GameTurnPlaceTileState.class, game.getState().getClass());

        game.setState(new GameStartState(game));
        assertEquals(GameTurnPlaceTileState.class, game.getState().getClass());
    }

    @Test
    void testPlaceTileTransition() {
        Game game = TestUtils.initGameEnv(5, false, false);

        game.start();
        assertEquals(GameTurnPlaceTileState.class, game.getState().getClass());

        game.getState().complete();
        assertEquals(GameTurnExtraActionState.class, game.getState().getClass());
    }

    @Test
    void testExtraTurnStateTransition() {
        Game game = TestUtils.initGameEnv(5, false, false);
        game.start();

        assertEquals(GameTurnPlaceTileState.class, game.getState().getClass());

        game.getCommandExecutor().execute(new PlaceTileDrawnCommand(GameBoard.STARTING_TILE_POSITION));
        assertEquals(GameTurnExtraActionState.class, game.getState().getClass());

        int turnCount = game.getTurnCount();

        game.getState().complete();
        assertEquals(GameTurnPlaceTileState.class, game.getState().getClass());
        assertEquals(turnCount + 1, game.getTurnCount());

        /* Clears the stack so the next state after extra action will be game over */
        game.getStack().clear();

        game.getState().complete();
        assertEquals(GameTurnExtraActionState.class, game.getState().getClass());

        game.getState().complete();
        assertEquals(GameOverState.class, game.getState().getClass());

        game = TestUtils.initGameEnv(5, false, false);
        game.start();
        game.setMaster(false);

        game.getState().complete();
        game.getState().complete();
        assertEquals(GameTurnWaitingMasterDataState.class, game.getState().getClass());
    }

    @Test
    void testWaitingMasterDataStateTransition() {
        Game game = TestUtils.initGameEnv(5, false, false);

        game.start();
        game.setMaster(false);
        assertEquals(GameTurnPlaceTileState.class, game.getState().getClass());

        game.getCommandExecutor().execute(new PlaceTileDrawnCommand(GameBoard.STARTING_TILE_POSITION));
        assertEquals(GameTurnExtraActionState.class, game.getState().getClass());

        game.getState().complete();
        assertEquals(GameTurnWaitingMasterDataState.class, game.getState().getClass());

        game.getState().complete();
        assertEquals(GameTurnPlaceTileState.class, game.getState().getClass());

        game.getStack().clear();

        game.getState().complete();
        assertEquals(GameTurnExtraActionState.class, game.getState().getClass());
        game.getState().complete();
        assertEquals(GameTurnWaitingMasterDataState.class, game.getState().getClass());
        game.getState().complete();
        assertEquals(GameOverState.class, game.getState().getClass());
    }


}
