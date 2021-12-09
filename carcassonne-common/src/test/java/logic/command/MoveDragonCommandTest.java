package logic.command;

import logic.Game;
import logic.TestUtils;
import logic.board.GameBoard;
import logic.state.GameStateType;
import logic.tile.Direction;
import logic.tile.TileFlags;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoveDragonCommandTest {
    @Test
    void testExecution() {
        Game game = TestUtils.initGameEnv(2, false, true);
        GameBoard board = game.getBoard();

        assertEquals(MoveDragonCommand.ERROR_NO_DRAGON, new MoveDragonCommand(Direction.TOP).canBeExecuted(game));

        board.spawnDragon(GameBoard.STARTING_TILE_POSITION.subtract(Direction.TOP.value()));
        assertEquals(MoveDragonCommand.ERROR_CANNOT_MOVE, new MoveDragonCommand(Direction.TOP).canBeExecuted(game));

        /* First tile is always placed to (0,0) position */
        TestUtils.placeTileRandomly(game);
        game.getBoard().getStartingTile().getConfig().getFlags().add(TileFlags.DRAGON);
        TestUtils.skipStateIfNeeded(game, GameStateType.TURN_PLACE_MEEPLE);
        TestUtils.assertState(game, GameStateType.TURN_MOVE_DRAGON);

        assertTrue(game.executeCommand(new MoveDragonCommand(Direction.TOP)));
    }
}
