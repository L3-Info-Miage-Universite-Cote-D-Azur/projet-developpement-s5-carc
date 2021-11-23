package logic.tile.chunk;

import logic.Game;
import logic.meeple.Meeple;
import logic.tile.Tile;
import stream.ByteInputStream;
import stream.ByteOutputStream;

import java.util.ArrayList;

/**
 * Represents a chunk of tiles. A chunk is a part of tile grid.
 */
public abstract class Chunk {
    private final Tile parent;

    private Meeple meeple;
    private AreaChunk area;

    public Chunk(Tile parent) {
        this.parent = parent;
    }

    /**
     * Gets the parent tile of this chunk.
     * @return The parent tile of this chunk.
     */
    public Tile getParent() {
        return parent;
    }

    /**
     * Gets the meeple on this chunk.
     * @return The meeple on this chunk.
     */
    public Meeple getMeeple(){
        return meeple;
    }

    /**
     * Sets the area of this chunk.
     * @param area The area to set.
     */
    public void setArea(AreaChunk area){
        this.area = area;
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
    public abstract boolean isCompatibleWith(Chunk chunk);

    /**
     * Gets the type of this chunk.
     * @return The type of this chunk.
     */
    public abstract ChunkType getType();

    @Override
    public String toString() {
        return "Chunk{" +
                "meeple=" + meeple +
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
