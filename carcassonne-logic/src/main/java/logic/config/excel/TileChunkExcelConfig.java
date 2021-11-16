package logic.config.excel;

import logic.tile.Chunk;
import logic.tile.ChunkId;
import logic.tile.ChunkType;
import logic.tile.Tile;

/**
 * Represents a tile chunk excel configuration.
 */
public class TileChunkExcelConfig {
    public ChunkType type;
    public ChunkId[] references;

    public TileChunkExcelConfig(ChunkType type, ChunkId[] references) {
        this.type = type;
        this.references = references;
    }

    public Chunk createChunk(Tile parent) {
        return new Chunk(parent, type, references);
    }
}
