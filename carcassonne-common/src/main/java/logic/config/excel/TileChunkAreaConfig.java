package logic.config.excel;

import logic.tile.chunk.ChunkId;
import logic.tile.chunk.ChunkType;

import java.util.List;

public class TileChunkAreaConfig {
    private final ChunkType chunkType;
    private final List<ChunkId> chunkIds;

    public TileChunkAreaConfig(ChunkType chunkType, List<ChunkId> chunks) {
        this.chunkType = chunkType;
        this.chunkIds = chunks;
    }

    public ChunkType getChunkType() {
        return chunkType;
    }

    public List<ChunkId> getChunkIds() {
        return chunkIds;
    }
}
