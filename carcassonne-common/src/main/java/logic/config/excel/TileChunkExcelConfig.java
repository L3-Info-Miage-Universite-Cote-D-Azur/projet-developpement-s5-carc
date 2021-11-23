package logic.config.excel;

import logic.tile.chunk.*;
import logic.tile.Tile;

/**
 * Represents a tile chunk excel configuration.
 */
public class TileChunkExcelConfig {
    public ChunkType type;

    public TileChunkExcelConfig(ChunkType type) {
        this.type = type;
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
