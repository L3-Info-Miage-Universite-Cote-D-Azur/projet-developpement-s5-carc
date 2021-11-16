package logic.tile;

import logic.meeple.Meeple;

import java.util.Arrays;

public class Chunk {
    private final Tile parent;
    private final ChunkType type;
    private final ChunkOffset[] relations;

    private Meeple meeple;

    public Chunk(Tile parent, ChunkType type, ChunkOffset[] relations) {
        this.parent = parent;
        this.type = type;
        this.relations = relations;
    }

    public ChunkType getType() {
        return type;
    }

    public Meeple getMeeple(){
        return meeple;
    }

    public void setMeeple(Meeple meeple) {
        this.meeple = meeple;
    }

    public boolean hasMeeple() {
        return meeple != null;
    }

    public boolean isCompatibleWith(Chunk chunk) {
        return this.type.isCompatibleWith(chunk.type);
    }

    @Override
    public String toString() {
        return "Chunk{" +
                "type=" + type +
                ", meeple=" + meeple +
                '}';
    }
}
