package logic.tile;

public enum ChunkOffset {
    CENTER(0),
    LEFT(1),
    RIGHT(2),
    UP(3),
    DOWN(4);

    public final int value;

    ChunkOffset(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
