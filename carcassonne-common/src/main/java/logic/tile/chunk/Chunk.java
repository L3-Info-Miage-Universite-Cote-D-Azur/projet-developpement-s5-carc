package logic.tile.chunk;

import logic.meeple.Meeple;
import logic.tile.Tile;
import logic.tile.area.Area;
import stream.ByteInputStream;
import stream.ByteOutputStream;

/**
 * Represents a chunk of tiles.
 * A chunk is a part of tile grid.
 */
public class Chunk {
    private final Tile parent;
    private final ChunkType type;

    private ChunkId currentId;
    private Meeple meeple;
    private Area area;

    public Chunk(ChunkType type, Tile parent) {
        this.type = type;
        this.parent = parent;
    }

    /**
     * Gets the parent tile of this chunk.
     *
     * @return The parent tile of this chunk.
     */
    public Tile getParent() {
        return parent;
    }

    /**
     * Gets the current chunk id.
     *
     * @return The current chunk id.
     */
    public ChunkId getCurrentId() {
        return currentId;
    }

    /**
     * Sets the current chunk id.
     *
     * @param id The new chunk id.
     */
    public void setCurrentId(ChunkId id) {
        this.currentId = id;
    }

    /**
     * Gets the meeple on this chunk.
     *
     * @return The meeple on this chunk.
     */
    public Meeple getMeeple() {
        return meeple;
    }

    /**
     * Sets the meeple on this chunk.
     *
     * @param meeple The meeple to set.
     */
    public void setMeeple(Meeple meeple) {
        this.meeple = meeple;
    }

    /**
     * Gets the chunk area of this chunk.
     *
     * @return The chunk area of this chunk.
     */
    public Area getArea() {
        return area;
    }

    /**
     * Sets the area of this chunk.
     *
     * @param area The area to set.
     */
    public void setArea(Area area) {
        this.area = area;
    }

    /**
     * Determines if this chunk has a meeple.
     *
     * @return True if this chunk has a meeple, false otherwise.
     */
    public boolean hasMeeple() {
        return meeple != null;
    }

    /**
     * Determines if this chunk is compatible with the given chunk.
     *
     * @param chunk The chunk to check compatibility with.
     * @return True if this chunk is compatible with the given chunk, false otherwise.
     */
    public boolean isCompatibleWith(Chunk chunk) {
        return chunk.type == type;
    }

    /**
     * Gets the type of this chunk.
     *
     * @return The type of this chunk.
     */
    public ChunkType getType() {
        return type;
    }

    /**
     * Encodes this chunk attributes into the given stream.
     *
     * @param stream
     */
    public void encode(ByteOutputStream stream) {
        if (meeple != null) {
            stream.writeBoolean(true);
            stream.writeInt(meeple.getOwner().getId());
        } else {
            stream.writeBoolean(false);
        }
    }

    /**
     * Decodes this chunk attributes from the given stream.
     *
     * @param stream
     */
    public void decode(ByteInputStream stream) {
        if (stream.readBoolean()) {
            meeple = new Meeple(parent.getGame().getPlayerById(stream.readInt()));
        } else {
            meeple = null;
        }
    }

    @Override
    public String toString() {
        return "Chunk{" +
                "area=" + area +
                " meeple=" + meeple +
                '}';
    }
}
