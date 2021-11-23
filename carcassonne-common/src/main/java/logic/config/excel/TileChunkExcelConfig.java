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

    public TileChunkAreaConfig getArea() {
        return area;
    }

    public void setArea(TileChunkAreaConfig area) {
        this.area = area;
    }

    public Chunk createChunk(Tile parent) {
        return switch (type) {
            case ROAD -> new RoadChunk(parent);
            case TOWN -> new TownChunk(parent);
            case RIVER -> new RiverChunk(parent);
            case ABBEY -> new AbbeyChunk(parent);
            case FIELD -> new FieldChunk(parent);
            case ROAD_END -> new RoadEndChunk(parent);
        };
    }
}
