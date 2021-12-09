package logic.dragon;

import logic.Game;
import logic.TestUtils;
import logic.board.GameBoard;
import logic.meeple.Meeple;
import logic.tile.chunk.Chunk;
import logic.tile.chunk.ChunkId;
import org.junit.jupiter.api.Test;
import stream.ByteInputStream;
import stream.ByteOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FairyTest {
    @Test
    void test() {
        Game game = TestUtils.initGameEnv(2, false, true);

        TestUtils.placeTileRandomly(game);

        Chunk chunk = game.getBoard().getTileAt(GameBoard.STARTING_TILE_POSITION).getChunk(ChunkId.CENTER_MIDDLE);
        chunk.setMeeple(new Meeple(game.getTurnExecutor()));
        game.getBoard().spawnFairy(chunk);
        game.getBoard().getFairy().evaluate();

        assertEquals(0, game.getTurnExecutor().getScore());
        game.getBoard().getFairy().evaluate();
        assertEquals(1, game.getTurnExecutor().getScore());
    }

    @Test
    void testEncodeDecode() {
        Game game = TestUtils.initGameEnv(2, true, true);
        GameBoard board = game.getBoard();

        board.spawnFairy(board.getTileAt(GameBoard.STARTING_TILE_POSITION).getChunk(ChunkId.CENTER_MIDDLE));

        ByteOutputStream outputStream = new ByteOutputStream(100);
        board.getFairy().encode(outputStream);

        Fairy copyFairy = new Fairy(board);
        ByteInputStream readStream = new ByteInputStream(outputStream.getBytes(), outputStream.getLength());
        copyFairy.decode(readStream);

        assertEquals(copyFairy.getChunk(), board.getFairy().getChunk());
    }
}
