package logic.tile.chunk;

import logic.tile.Tile;

public class RoadEndChunk extends Chunk {
    public RoadEndChunk(Tile parent) {
        super(parent);
    }

    @Override
    public boolean isCompatibleWith(Chunk chunk) {
        return chunk.getType() == ChunkType.ROAD || chunk.getType() == ChunkType.ROAD_END;
    }

    @Override
    public ChunkType getType() {
        return ChunkType.ROAD_END;
    }
}
