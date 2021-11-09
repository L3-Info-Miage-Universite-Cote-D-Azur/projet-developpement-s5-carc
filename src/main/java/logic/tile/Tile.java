package logic.tile;

import logic.math.Vector2;
import logic.tile.components.Component;

public class Tile {
    private Vector2 position;
    private Chunk[] chunks;

    public Tile() {
        chunks = new Chunk[ChunkOffset.values().length];
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
}
