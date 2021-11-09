package logic.config;

import logic.tile.Chunk;
import logic.tile.ChunkType;
import logic.tile.Tile;

public class TileConfig {
    public ChunkType center;
    public ChunkType left;
    public ChunkType right;
    public ChunkType up;
    public ChunkType down;
    public boolean isStartingTile;

    public TileConfig() {
    }

    public boolean validate() {
        if (center == null) return false;
        if (left == null) return false;
        if (right == null) return false;
        if (up == null) return false;
        if (down == null) return false;

        return true;
    }

    public Tile createTile() {
        return new Tile(new Chunk[]{
                new Chunk(center),
                new Chunk(left),
                new Chunk(right),
                new Chunk(up),
                new Chunk(down)
        }, isStartingTile);
    }
}
