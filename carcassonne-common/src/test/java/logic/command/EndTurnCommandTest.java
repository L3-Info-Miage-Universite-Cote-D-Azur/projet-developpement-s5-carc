package logic.command;

import logic.Game;
import logic.TestUtils;
import logic.board.GameBoard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EndTurnCommandTest {
    @Test
    public void testEndTurn() {
        Game game = TestUtils.initGameEnv(5, false, true);

        assertFalse(game.getCommandExecutor().execute(new EndTurnCommand()));
        assertTrue(game.getCommandExecutor().execute(new PlaceTileDrawnCommand(GameBoard.STARTING_TILE_POSITION)));
        assertTrue(game.getCommandExecutor().execute(new EndTurnCommand()));

        assertEquals(game.getTurn().getCount(), 2);
    }
}
