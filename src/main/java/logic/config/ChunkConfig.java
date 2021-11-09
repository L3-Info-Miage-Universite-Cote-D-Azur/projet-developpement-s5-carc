package logic.config;

import logic.tile.Chunk;
import logic.tile.ChunkType;

public class ChunkConfig {
    public ChunkType type;

    public final Chunk createChunk() {
        return new Chunk(type);
    }
}
