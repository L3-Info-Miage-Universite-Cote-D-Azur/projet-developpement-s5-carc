package logic.config;

import logic.tile.Chunk;
import logic.tile.ChunkOffset;
import logic.tile.ChunkType;
import logic.tile.Tile;

public class ChunkConfig {
    public ChunkType type;
    public ChunkOffset[] relations;

    public ChunkConfig() {
    }

    public ChunkConfig(ChunkType type, ChunkOffset[] relations) {
        this.type = type;
        this.relations = relations;
    }

    public Chunk createChunk(Tile parent) {
        return new Chunk(parent, type, relations);
    }
}
