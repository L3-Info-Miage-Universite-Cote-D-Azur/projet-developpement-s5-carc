package logic.tile.chunk;

import logic.tile.Tile;

public class TownChunk extends Chunk {
    public TownChunk(Tile parent) {
        super(parent);
    }

    @Override
    public boolean isCompatibleWith(Chunk chunk) {
        return chunk.getType() == ChunkType.TOWN;
    }

    @Override
    public ChunkType getType() {
        return ChunkType.TOWN;
    }
}
