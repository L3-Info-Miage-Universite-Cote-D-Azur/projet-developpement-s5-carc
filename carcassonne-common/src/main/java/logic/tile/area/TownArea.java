package logic.tile.area;

import logic.tile.TileFlags;
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
     *
     * @return The area type.
     */
    @Override
    public ChunkType getType() {
        return ChunkType.TOWN;
    }

    /**
     * Checks if the given area can be merged.
     *
     * @param other The other area to merge with.
     * @return True if the areas can be merged, false otherwise.
     */
    @Override
    public boolean canBeMerged(Area other) {
        return other.getType() == ChunkType.TOWN;
    }

    /**
     * Gets the points earned by the area closing.
     *
     * @return The points earned by the area closing.
     */
    @Override
    public int getClosingPoints() {
        int numTiles = getNumTiles();
        int numShields = getNumTiles(TileFlags.SHIELD);

        return 2 * numTiles + 2 * numShields;
    }

    /**
     * Gets the points earned by the area opening.
     *
     * @return The points earned by the area opening.
     */
    @Override
    public int getOpenPoints() {
        return getNumTiles() + getNumTiles(TileFlags.SHIELD);
    }
}
