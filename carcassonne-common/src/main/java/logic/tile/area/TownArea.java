package logic.tile.area;

import logic.tile.chunk.Chunk;
import logic.tile.chunk.ChunkType;

import java.util.List;

public class TownArea extends Area {
    /**
     * Constructor for the area.
     *
     * @param chunks
     */
    public TownArea(List<Chunk> chunks) {
        super(chunks);
    }

    /**
     * Gets the area type.
     * @return The area type.
     */
    @Override
    public ChunkType getType() {
        return ChunkType.TOWN;
    }

    /**
     * Checks if the given area can be merged.
     * @param other The other area to merge with.
     * @return True if the areas can be merged, false otherwise.
     */
    @Override
    public boolean canBeMerged(Area other) {
        return other.getType() == ChunkType.TOWN;
    }
}
