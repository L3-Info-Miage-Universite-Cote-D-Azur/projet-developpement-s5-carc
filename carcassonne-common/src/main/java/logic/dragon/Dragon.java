package logic.dragon;

import logic.board.GameBoard;
import logic.command.RemoveMeepleCommand;
import logic.math.Vector2;
import logic.tile.Direction;
import logic.tile.Tile;
import logic.tile.chunk.Chunk;
import logic.tile.chunk.ChunkId;
import stream.ByteInputStream;
import stream.ByteOutputStream;
import stream.ByteStreamHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the dragon spawned by the volcano.
 */
public final class Dragon {
    public static final int NUM_MOVES = 6;

    private final GameBoard board;
    private final ArrayList<Vector2> path;

    public Dragon(GameBoard board) {
        this.board = board;
        this.path = new ArrayList<>();
    }

    public Dragon(GameBoard board, Vector2 position) {
        this.board = board;
        this.path = new ArrayList<>();
        this.path.add(position);
    }

    /**
     * Moves the dragon to the specified position.
     *
     * @param position the position to move to
     */
    public void moveTo(Vector2 position) {
        if (path.contains(position)) {
            throw new IllegalArgumentException("Dragon cannot move to position " + position + " because it is already on the path.");
        }

        path.add(position);
        checkAreas();
    }

    /**
     * Removes the meeple in the areas of the dragon.
     */
    private void checkAreas() {
        Tile tile = board.getTileAt(getPosition());

        for (ChunkId chunkId : ChunkId.values()) {
            Chunk chunk = tile.getChunk(chunkId);

            if (chunk.hasMeeple()) {
                RemoveMeepleCommand.removeMeeple(chunk);
            }
        }
    }

    /**
     * Gets the current dragon position.
     *
     * @return the current dragon position
     */
    public Vector2 getPosition() {
        return path.get(path.size() - 1);
    }

    /**
     * Gets the path of the dragon.
     * @return the path of the dragon
     */
    public List<Vector2> getPath() {
        return path;
    }

    /**
     * Gets whether the dragon can move to the specified position.
     *
     * @return true if the dragon can move to the specified position, false otherwise
     */
    public boolean canMoveTo(Vector2 position) {
        Fairy fairy = board.getFairy();

        if (fairy != null && fairy.getTilePosition().equals(position)) {
            return false;
        }

        return board.hasTileAt(position) && !path.contains(position) && getPosition().subtract(position).sqrLength() == 1;
    }

    /**
     * Gets whether the dragon has moved at all.
     *
     * @return true if the dragon has moved at all, false otherwise
     */
    public boolean isBlocked() {
        Vector2 position = getPosition();

        for (Direction direction : Direction.values()) {
            Vector2 movePosition = position.add(direction.value());

            if (canMoveTo(movePosition)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns whether the dragon has finished moving.
     *
     * @return true if the dragon has finished moving, false otherwise
     */
    public boolean hasFinished() {
        return path.size() == NUM_MOVES;
    }

    /**
     * Encodes the dragon into a byte stream.
     *
     * @param stream the byte stream to encode the dragon into
     */
    public void encode(ByteOutputStream stream) {
        stream.writeInt(path.size());

        for (Vector2 position : path) {
            ByteStreamHelper.encodeVector(stream, position);
        }
    }

    /**
     * Decodes the dragon from a byte stream.
     *
     * @param stream the byte stream to decode the dragon from
     */
    public void decode(ByteInputStream stream) {
        path.clear();

        int size = stream.readInt();

        for (int i = 0; i < size; i++) {
            path.add(ByteStreamHelper.decodeVector(stream));
        }
    }
}