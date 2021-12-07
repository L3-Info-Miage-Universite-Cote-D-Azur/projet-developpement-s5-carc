package logic.tile.area;

import logic.tile.chunk.Chunk;

import java.util.List;

/**
 * Factory for creating areas.
 */
public class AreaFactory {
    /**
     * Creates an area from a list of chunks.
     *
     * @param chunks The list of chunks to create the area from.
     * @return The created area.
     */
    public static Area create(List<Chunk> chunks) {
        return switch (chunks.iterator().next().getType()) {
            case ROAD -> new RoadArea(chunks);
            case ROAD_END -> new RoadEndArea(chunks);
            case TOWN -> new TownArea(chunks);
            case ABBEY -> new AbbeyArea(chunks);
            case FIELD -> new FieldArea(chunks);
            default -> throw new IllegalStateException("Unexpected chunk type.");
        };
    }
}
