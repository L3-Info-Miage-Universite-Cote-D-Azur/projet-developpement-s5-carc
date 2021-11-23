package logic.config.excel;

import logic.tile.chunk.ChunkId;

import java.util.List;

public class TileChunkAreaConfig {
    private final List<ChunkId> chunkIds;

    public TileChunkAreaConfig(List<ChunkId> chunks) {
        this.chunkIds = chunks;
    }

    public List<ChunkId> getChunkIds() {
        return chunkIds;
    }
}
