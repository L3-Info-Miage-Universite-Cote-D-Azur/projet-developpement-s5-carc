package logic.tile.chunk;

import logic.tile.Tile;

import java.util.ArrayList;

public class AreaChunk {
    private final ArrayList<Chunk> chunks;
    private final ArrayList<Tile> tiles;
    private boolean completed;

    public AreaChunk() {
        chunks = new ArrayList<>();
        tiles = new ArrayList<>();
        completed = false;
    }

    public void addChunk(Chunk chunk) {
        chunks.add(chunk);
    }

    public void combine(AreaChunk other) {
        chunks.addAll(other.chunks);
        tiles.addAll(other.tiles);
    }
}
