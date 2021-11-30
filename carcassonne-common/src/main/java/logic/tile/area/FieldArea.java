package logic.tile.area;

import logic.tile.chunk.Chunk;
import logic.tile.chunk.ChunkType;

import java.util.List;

public class FieldArea extends Area {
    /**
     * Constructor for the area.
     *
     * @param chunks
     */
    public FieldArea(List<Chunk> chunks) {
        super(chunks);
    }

    /**
     * Gets the area type.
     * @return The area type.
     */
    @Override
    public ChunkType getType() {
        return ChunkType.FIELD;
    }

    /**
     * Checks if the given area can be merged.
     * @param other The other area to merge with.
     * @return True if the areas can be merged, false otherwise.
     */
    @Override
    public boolean canBeMerged(Area other) {
        return other.getType() == ChunkType.FIELD;
    }

    /**
     * Returns whether the area is closed.
     *
     * @return True if the area is closed, false otherwise.
     */
    @Override
    public boolean isClosed() {
        return false;
    }
}
