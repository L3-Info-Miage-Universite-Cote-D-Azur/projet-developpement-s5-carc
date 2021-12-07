package logic.dragon;

import logic.board.GameBoard;
import logic.math.Vector2;
import logic.tile.chunk.Chunk;
import logic.tile.chunk.ChunkId;
import stream.ByteInputStream;
import stream.ByteOutputStream;
import stream.ByteStreamHelper;

/**
 * Class representing a fairy protecting the fairy.
 */
public class Fairy {
    private final GameBoard board;
    private Chunk chunk;

    public Fairy(GameBoard board) {
        this.board = board;
    }

    public Fairy(GameBoard board, Chunk chunk) {
        this.board = board;
        this.chunk = chunk;
    }

    /**
     * Gets the board this fairy is on.
     * @return the board this fairy is on.
     */
    public GameBoard getBoard() {
        return board;
    }

    /**
     * Gets the chunk this fairy is protecting.
     * @return the chunk this fairy is protecting.
     */
    public Chunk getChunk() {
        return chunk;
    }

    /**
     * Encodes this fairy into a byte stream.
     * @param stream the stream to encode this fairy into.
     */
    public void encode(ByteOutputStream stream) {
        ByteStreamHelper.encodeVector(stream, chunk.getParent().getPosition());
        stream.writeInt(chunk.getCurrentId().ordinal());
    }

    /**
     * Decodes this fairy from a byte stream.
     * @param stream the stream to decode this fairy from.
     */
    public void decode(ByteInputStream stream) {
        Vector2 position = ByteStreamHelper.decodeVector(stream);
        chunk = board.getTileAt(position).getChunk(ChunkId.values()[stream.readInt()]);
    }
}
