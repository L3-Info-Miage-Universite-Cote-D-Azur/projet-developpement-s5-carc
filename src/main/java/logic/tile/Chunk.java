package logic.tile;

import logic.meeple.Meeple;

public class Chunk {
    private final ChunkType type;
    private Meeple meeple;

    public Chunk(ChunkType type, Meeple meeple) {
        this.type = type;
        this.meeple = meeple;
    }

    public ChunkType getType() {
        return type;
    }

    public Meeple getMeeple(){
        return meeple;
    }

    @Override
    public String toString() {
        return "Chunk{" +
                "type=" + type +
                '}';
    }
}
