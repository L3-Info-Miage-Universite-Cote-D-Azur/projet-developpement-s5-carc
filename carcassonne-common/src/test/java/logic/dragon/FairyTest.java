package logic.dragon;

import logic.Game;
import logic.TestUtils;
import logic.board.GameBoard;
import logic.meeple.Meeple;
import logic.tile.chunk.Chunk;
import logic.tile.chunk.ChunkId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FairyTest {
    @Test
    void test() {
        Game game = TestUtils.initGameEnv(2, false, true);

        TestUtils.placeTileRandomly(game);

        Chunk chunk = game.getBoard().getTileAt(GameBoard.STARTING_TILE_POSITION).getChunk(ChunkId.CENTER_MIDDLE);
        chunk.setMeeple(new Meeple(game.getTurnExecutor()));
        game.getBoard().spawnFairy(chunk);
        game.getBoard().getFairy().evaluate();

        assertEquals(game.getTurnExecutor().getScore(), 0);
        game.getBoard().getFairy().evaluate();
        assertEquals(game.getTurnExecutor().getScore(), 1);
    }
}
