package logic.tile;

public enum ChunkType {
    ROAD(0, 0),
    TOWN(1, 1, 2),
    TOWN_WALL(2, 1, 2),
    RIVER(3, 3),
    ABBEY(4, 4),
    FIELD(5, 5);

    private final int type;
    private final int[] compatibleTypes;

    ChunkType(int type, int... compatibleChunks) {
        this.type = type;
        this.compatibleTypes = compatibleChunks;
    }

    public final boolean isCompatibleWith(ChunkType type) {
        for (int compatibleType : compatibleTypes) {
            if (compatibleType == type.type) {
                return true;
            }
        }

        return false;
    }
}
