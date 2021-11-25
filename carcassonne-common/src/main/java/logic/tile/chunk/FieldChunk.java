package logic.tile.chunk;

import logic.tile.Tile;

/**
 * Represents a field chunk.
 */
public class FieldChunk extends Chunk {
    public FieldChunk(Tile parent) {
        super(parent);
    }

    @Override
    public boolean isCompatibleWith(Chunk chunk) {
        return chunk.getType() == ChunkType.FIELD;
    }

    @Override
    public ChunkType getType() {
        return ChunkType.FIELD;
    }
}
