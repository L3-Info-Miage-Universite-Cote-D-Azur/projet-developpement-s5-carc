package logic.tile;

/**
 * The type of a chunk.
 */
public enum ChunkType {
    /**
     * The chunk contains a road.
     */
    ROAD(0, 0),

    /**
     * The chunk contains a town.
     */
    TOWN(1, 1, 2),

    /**
     * The chunk contains a town wall.
     */
    TOWN_WALL(2, 1, 2),

    /**
     * The chunk contains a river.
     */
    RIVER(3, 3),

    /**
     * The chunk contains a abbey.
     */
    ABBEY(4, 4),

    /**
     * The chunk contains a field.
     */
    FIELD(5, 5),

    /**
     * The chunk contains a road end.
     */
    ROAD_END(6, 0);

    private final int type;
    private final int[] compatibleTypes;

    ChunkType(int type, int... compatibleChunks) {
        this.type = type;
        this.compatibleTypes = compatibleChunks;
    }

    /**
     * Determines if the given chunk type is compatible with this chunk type.
     * @param type the chunk type to check
     * @return true if the given chunk type is compatible with this chunk type
     */
    public final boolean isCompatibleWith(ChunkType type) {
        for (int compatibleType : compatibleTypes) {
            if (compatibleType == type.type) {
                return true;
            }
        }

        return false;
    }
}
