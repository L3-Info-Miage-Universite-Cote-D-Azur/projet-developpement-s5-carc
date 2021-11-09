package logic.tile;

public class Chunk {
    private final ChunkType type;

    public Chunk(ChunkType type) {
        this.type = type;
    }

    public ChunkType getType() {
        return type;
    }
}
