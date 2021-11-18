package logic.tile;

import logic.Game;
import logic.meeple.Meeple;
import stream.ByteInputStream;
import stream.ByteOutputStream;

/**
 * Represents a chunk of tiles. A chunk is a part of tile grid.
 */
public class Chunk {
    private final Tile parent;
    private final ChunkType type;
    private final ChunkId[] relations;

    private Meeple meeple;

    public Chunk(Tile parent, ChunkType type, ChunkId[] relations) {
        this.parent = parent;
        this.type = type;
        this.relations = relations;
    }

    /**
     * Gets the type of this chunk.
     * @return The type of this chunk.
     */
    public ChunkType getType() {
        return type;
    }

    /**
     * Gets the meeple on this chunk.
     * @return The meeple on this chunk.
     */
    public Meeple getMeeple(){
        return meeple;
    }

    /**
     * Sets the meeple on this chunk.
     * @param meeple The meeple to set.
     */
    public void setMeeple(Meeple meeple) {
        this.meeple = meeple;
    }

    /**
     * Determines if this chunk has a meeple.
     * @return
     */
    public boolean hasMeeple() {
        return meeple != null;
    }

    /**
     * Determines if this chunk is compatible with the given chunk.
     * @param chunk
     * @return
     */
    public boolean isCompatibleWith(Chunk chunk) {
        return this.type.isCompatibleWith(chunk.type);
    }

    @Override
    public String toString() {
        return "Chunk{" +
                "type=" + type +
                ", meeple=" + meeple +
                '}';
    }

    public void encode(ByteOutputStream stream) {
        if (meeple != null) {
            stream.writeBoolean(true);
            stream.writeInt(meeple.getOwner().getId());
        } else {
            stream.writeBoolean(false);
        }
    }

    public void decode(ByteInputStream stream, Game game) {
        if (stream.readBoolean()) {
            meeple = new Meeple(game.getPlayerById(stream.readInt()));
        } else {
            meeple = null;
        }
    }
}
