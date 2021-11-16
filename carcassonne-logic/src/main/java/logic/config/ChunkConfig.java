package logic.config;

import logic.tile.Chunk;
import logic.tile.ChunkId;
import logic.tile.ChunkType;
import logic.tile.Tile;

public class ChunkConfig {
    public ChunkType type;
    public ChunkId[] relations;

    public ChunkConfig() {
    }

    public ChunkConfig(ChunkType type, ChunkId[] relations) {
        this.type = type;
        this.relations = relations;
    }

    public Chunk createChunk(Tile parent) {
        return new Chunk(parent, type, relations);
    }
}
