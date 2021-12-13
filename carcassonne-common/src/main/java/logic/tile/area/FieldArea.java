package logic.tile.area;

import logic.tile.Tile;
import logic.tile.chunk.Chunk;
import logic.tile.chunk.ChunkId;
import logic.tile.chunk.ChunkType;

import java.util.HashSet;
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
     *
     * @return The area type.
     */
    @Override
    public ChunkType getType() {
        return ChunkType.FIELD;
    }

    /**
     * Checks if the given area can be merged.
     *
     * @param other The other area to merge with.
     * @return True if the areas can be merged, false otherwise.
     */
    @Override
    public boolean canBeMerged(Area other) {
        return other.getType() == ChunkType.FIELD;
    }

    /**
     * Gets the points earned by the area closing.
     *
     * @return The points earned by the area closing.
     */
    @Override
    public int getClosingPoints() {
        return 0;
    }

    /**
     * Gets the points earned by the area opening.
     *
     * @return The points earned by the area opening.
     */
    @Override
    public int getOpenPoints() {
        HashSet<Area> closedTownAreas = new HashSet<>();

        for (Tile tile : getTiles()) {
            for (ChunkId chunkId : ChunkId.values()) {
                Chunk chunk = tile.getChunk(chunkId);
                Area area = chunk.getArea();

                if (area.getType() == ChunkType.TOWN) {
                    /* Now we found a town in the tile, we need to check if the town is in contact with this area. */
                    for (ChunkId neighborChunkId : chunk.getCurrentId().getNeighbours()) {
                        Chunk neighborChunk = tile.getChunk(neighborChunkId);

                        if (neighborChunk.getArea() == this) {
                            closedTownAreas.add(neighborChunk.getArea());
                            break;
                        }
                    }
                }
            }
        }

        // We earn 3 points for each closed town areas in contact with the field area.
        return closedTownAreas.size() * 3;
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
