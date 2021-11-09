package logic.tile;

public class Chunk {
    private final ChunkType type;
    private final int plugId;

    public Chunk(ChunkType type, int plugId) {
        this.type = type;
        this.plugId = plugId;
    }

    public ChunkType getType() {
        return type;
    }

    public int getPlugId() {
        return plugId;
    }
}
