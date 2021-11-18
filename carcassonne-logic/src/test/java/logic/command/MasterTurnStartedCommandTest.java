package logic.command;

import logic.Game;
import logic.TestUtils;
import logic.config.GameConfig;
import logic.player.Player;
import logic.tile.Tile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MasterTurnStartedCommandTest {
    @Test
    public void testExecute() {
        Game game = TestUtils.initGameEnv(5, false, true);
        Tile tile = game.getTurn().getTileToDraw();

        game.getStack().clear();
        game.setMaster(false);

        assertTrue(game.getCommandExecutor().execute(new MasterTurnStartedCommand(tile, game)));
    }

    @Test
    public void testPreventOnMaster() {
        Game game = TestUtils.initGameEnv(5, false, true);

        assertTrue(game.isMaster());
        assertFalse(game.getCommandExecutor().execute(new MasterTurnStartedCommand(game.getTurn().getTileToDraw(), game)));
    }
}
