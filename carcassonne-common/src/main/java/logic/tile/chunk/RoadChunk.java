package logic.tile.chunk;

import logic.tile.Tile;

/**
 * Represents a road chunk.
 */
public class RoadChunk extends Chunk {
    public RoadChunk(Tile parent) {
        super(parent);
    }

    @Override
    public boolean isCompatibleWith(Chunk chunk) {
        return chunk.getType() == ChunkType.ROAD || chunk.getType() == ChunkType.ROAD_END;
    }

    @Override
    public ChunkType getType() {
        return ChunkType.ROAD;
    }
}
