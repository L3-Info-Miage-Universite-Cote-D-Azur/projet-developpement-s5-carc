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
    private boolean justSpawned;

    public Fairy(GameBoard board) {
        this.board = board;
        this.justSpawned = true;
    }

    public Fairy(GameBoard board, Chunk chunk) {
        this.board = board;
        this.chunk = chunk;
        this.justSpawned = true;
    }

    /**
     * Gets the board this fairy is on.
     *
     * @return the board this fairy is on.
     */
    public GameBoard getBoard() {
        return board;
    }

    /**
     * Gets the chunk this fairy is protecting.
     *
     * @return the chunk this fairy is protecting.
     */
    public Chunk getChunk() {
        return chunk;
    }

    /**
     * Gets the position of this fairy.
     *
     * @return the position of this fairy.
     */
    public Vector2 getTilePosition() {
        return chunk.getParent().getPosition();
    }

    /**
     * Encodes this fairy into a byte stream.
     *
     * @param stream the stream to encode this fairy into.
     */
    public void encode(ByteOutputStream stream) {
        ByteStreamHelper.encodeVector(stream, chunk.getParent().getPosition());
        stream.writeInt(chunk.getCurrentId().ordinal());
        stream.writeBoolean(justSpawned);
    }

    /**
     * Decodes this fairy from a byte stream.
     *
     * @param stream the stream to decode this fairy from.
     */
    public void decode(ByteInputStream stream) {
        Vector2 position = ByteStreamHelper.decodeVector(stream);
        chunk = board.getTileAt(position).getChunk(ChunkId.values()[stream.readInt()]);
        justSpawned = stream.readBoolean();
    }

    /**
     * Evaluates the fairy and gives a point to the player if the fairy was not just spawned.
     */
    public void evaluate() {
        if (justSpawned) {
            justSpawned = false;
            return;
        }

        chunk.getMeeple().getOwner().addScore(1, chunk.getType());
    }
}
