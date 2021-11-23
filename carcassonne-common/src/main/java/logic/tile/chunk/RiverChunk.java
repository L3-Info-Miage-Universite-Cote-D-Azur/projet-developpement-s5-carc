package logic.tile.chunk;

import logic.tile.Tile;

public class RiverChunk extends Chunk {
    public RiverChunk(Tile parent) {
        super(parent);
    }

    @Override
    public boolean isCompatibleWith(Chunk chunk) {
        return chunk.getType() == ChunkType.RIVER;
    }

    @Override
    public ChunkType getType() {
        return ChunkType.RIVER;
    }
}
