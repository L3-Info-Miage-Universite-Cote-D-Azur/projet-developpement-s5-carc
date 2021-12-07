package logic.tile.area;

import logic.tile.chunk.Chunk;
import logic.tile.chunk.ChunkType;

import java.util.List;

public class RoadArea extends Area {
    /**
     * Constructor for the area.
     *
     * @param chunks
     */
    public RoadArea(List<Chunk> chunks) {
        super(chunks);
    }

    /**
     * Gets the area type.
     *
     * @return The area type.
     */
    @Override
    public ChunkType getType() {
        return ChunkType.ROAD;
    }

    /**
     * Checks if the given area can be merged.
     *
     * @param other The other area to merge with.
     * @return True if the areas can be merged, false otherwise.
     */
    @Override
    public boolean canBeMerged(Area other) {
        return other.getType() == ChunkType.ROAD;
    }

    /**
     * Gets the points earned by the area closing.
     *
     * @return The points earned by the area closing.
     */
    @Override
    public int getClosingPoints() {
        return 1 * getNumTiles();
    }
}
