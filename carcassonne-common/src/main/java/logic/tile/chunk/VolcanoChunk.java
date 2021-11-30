package logic.tile.chunk;

import logic.tile.Tile;

/**
 * Represents a volcano chunk.
 */
public class VolcanoChunk extends Chunk {
    public VolcanoChunk(Tile parent) {
        super(parent);
    }

    @Override
    public boolean isCompatibleWith(Chunk chunk) {
        return chunk.getType() == ChunkType.VOLCANO;
    }

    @Override
    public ChunkType getType() {
        return ChunkType.VOLCANO;
    }
}
