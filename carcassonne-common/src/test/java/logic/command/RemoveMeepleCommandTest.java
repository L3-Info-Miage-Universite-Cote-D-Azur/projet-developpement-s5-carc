package logic.command;

import logic.Game;
import logic.TestUtils;
import logic.board.GameBoard;
import logic.tile.chunk.ChunkId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RemoveMeepleCommandTest {

    @Test
    public void testRemoveOnOccupiedTile() {
        Game game = TestUtils.initGameEnv(5, false, true);

        assertTrue(game.getCommandExecutor().execute(new PlaceTileDrawnCommand(GameBoard.STARTING_TILE_POSITION)));
        assertTrue(game.getCommandExecutor().execute(new PlaceMeepleCommand(GameBoard.STARTING_TILE_POSITION, ChunkId.CENTER_MIDDLE)));
    }
}
