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

    

    @Override
    public String toString() {
        return "Chunk{" +
                "type=" + type +
                ", meeple=" + meeple +
                '}';
    }
}
