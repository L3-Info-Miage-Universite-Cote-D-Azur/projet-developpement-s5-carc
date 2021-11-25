package logic.tile.chunk;

import logic.tile.Tile;

/**
 * Represents an abbey chunk.
 */
public class AbbeyChunk extends Chunk {
    public AbbeyChunk(Tile parent) {
        super(parent);
    }

    @Override
    public boolean isCompatibleWith(Chunk chunk) {
        return false;
    }

    @Override
    public ChunkType getType() {
        return ChunkType.ABBEY;
    }
}
