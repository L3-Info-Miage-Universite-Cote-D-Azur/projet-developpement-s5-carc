package logic.tile;

import logic.math.Vector2;

import java.util.Arrays;
import java.util.EnumSet;

public class Tile {
    private Vector2 position;
    private Chunk[] chunks;
    private EnumSet<TileFlags> flags;

    public Tile() {
        chunks = new Chunk[ChunkOffset.values().length];
        flags = EnumSet.noneOf(TileFlags.class);
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Chunk getChunk(ChunkOffset offset) {
        return chunks[offset.value];
    }

    public void setChunk(ChunkOffset offset, Chunk chunk) {
        chunks[offset.value] = chunk;
    }

    public boolean hasFlags(TileFlags flag) {
        return flags.contains(flag);
    }

    public void setFlags(TileFlags flag, boolean value) {
        if (value) {
            if (!hasFlags(flag)) {
                flags.add(flag);
            }
        } else {
            flags.remove(flag);
        }
    }
}
