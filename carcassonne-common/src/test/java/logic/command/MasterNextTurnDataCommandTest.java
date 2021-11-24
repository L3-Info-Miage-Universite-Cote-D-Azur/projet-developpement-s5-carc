package logic.command;

import logic.Game;
import logic.TestUtils;
import logic.state.turn.GameTurnPlaceTileState;
import logic.tile.Tile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MasterNextTurnDataCommandTest {
    @Test
    public void testExecute() {
        Game game = TestUtils.initGameEnv(5, false, true);
        Tile tile = ((GameTurnPlaceTileState) game.getState()).getTileDrawn();

        game.start();
        game.setMaster(false);

        game.getState().complete();
        game.getState().complete();
        assertTrue(game.getCommandExecutor().execute(new MasterNextTurnDataCommand(tile, game)));
    }

    @Test
    public void testPreventOnMaster() {
        Game game = TestUtils.initGameEnv(5, false, true);
        Tile tile = ((GameTurnPlaceTileState) game.getState()).getTileDrawn();

        assertTrue(game.isMaster());
        assertFalse(game.getCommandExecutor().execute(new MasterNextTurnDataCommand(tile, game)));
    }
}
