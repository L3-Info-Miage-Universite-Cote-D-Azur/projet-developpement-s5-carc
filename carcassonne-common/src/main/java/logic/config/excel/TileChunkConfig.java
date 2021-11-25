package logic.config.excel;

import logic.tile.Tile;
import logic.tile.chunk.Chunk;
import logic.tile.chunk.ChunkFactory;
import logic.tile.chunk.ChunkType;

/**
 * Represents a tile chunk excel configuration.
 */
public class TileChunkConfig {
    private final ChunkType type;
    private TileChunkAreaConfig area;

    public TileChunkConfig(ChunkType type) {
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
     *
     * @param tile The tile.
     * @return The tile chunk.
     */
    public Chunk createChunk(Tile tile) {
        return ChunkFactory.createByType(type, tile);
    }
}
