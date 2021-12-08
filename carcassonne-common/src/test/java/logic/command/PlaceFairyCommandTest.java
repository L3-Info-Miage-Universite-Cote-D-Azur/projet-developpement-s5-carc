package logic.command;

import logic.Game;
import logic.TestUtils;
import logic.board.GameBoard;
import logic.meeple.Meeple;
import logic.tile.chunk.ChunkId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlaceFairyCommandTest {
    @Test
    void testPlacement() {
        Game game = TestUtils.initGameEnv(2, false, true);

        TestUtils.placeTileRandomly(game);

        assertFalse(game.executeCommand(new PlaceFairyCommand(GameBoard.STARTING_TILE_POSITION, ChunkId.CENTER_MIDDLE)));
        game.getBoard().getTileAt(GameBoard.STARTING_TILE_POSITION).getChunk(ChunkId.CENTER_MIDDLE).setMeeple(new Meeple(game.getTurnExecutor()));
        assertTrue(game.executeCommand(new PlaceFairyCommand(GameBoard.STARTING_TILE_POSITION, ChunkId.CENTER_MIDDLE)));
    }
}
