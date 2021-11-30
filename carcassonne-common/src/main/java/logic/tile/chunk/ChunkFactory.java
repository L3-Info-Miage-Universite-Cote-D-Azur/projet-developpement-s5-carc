package logic.tile.chunk;

import logic.tile.Tile;

/**
 * A factory for creating chunks.
 */
public class ChunkFactory {
    /**
     * Creates a chunk.
     *
     * @param type   The type of the chunk.
     * @param parent The parent tile of the chunk.
     * @return the chunk
     */
    public static Chunk createByType(ChunkType type, Tile parent) {
        return switch (type) {
            case ROAD -> new RoadChunk(parent);
            case TOWN -> new TownChunk(parent);
            case ABBEY -> new AbbeyChunk(parent);
            case FIELD -> new FieldChunk(parent);
            case ROAD_END -> new RoadEndChunk(parent);
            case VOLCANO -> new VolcanoChunk(parent);
        };
    }
}
