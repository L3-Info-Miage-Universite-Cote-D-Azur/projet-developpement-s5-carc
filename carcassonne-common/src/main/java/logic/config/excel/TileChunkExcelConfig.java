package logic.config.excel;

import logic.tile.chunk.*;
import logic.tile.Tile;

/**
 * Represents a tile chunk excel configuration.
 */
public class TileChunkExcelConfig {
    private final ChunkType type;
    private TileChunkAreaConfig area;

    public TileChunkExcelConfig(ChunkType type) {
        this.type = type;
    }

    public ChunkType getType() {
        return type;
    }

    public TileChunkAreaConfig getArea() {
        return area;
    }

    public void setArea(TileChunkAreaConfig area) {
        if (area.getChunkType() != type) {
            throw new IllegalArgumentException("Chunk type mismatch");
        }
        this.area = area;
    }

    /**
     * Instantiates a tile chunk from this configuration.
     * @param tile The tile.
     * @return The tile chunk.
     */
    public Chunk createChunk(Tile tile) {
        return ChunkFactory.createByType(type, tile);
    }
}
