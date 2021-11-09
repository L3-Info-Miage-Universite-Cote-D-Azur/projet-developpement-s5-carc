package logic.config;

import logic.tile.Chunk;
import logic.tile.Tile;

public class TileConfig {
    public ChunkConfig center;
    public ChunkConfig left;
    public ChunkConfig right;
    public ChunkConfig up;
    public ChunkConfig down;

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
                center.createChunk(),
                left.createChunk(),
                right.createChunk(),
                up.createChunk(),
                down.createChunk()
        });
    }
}
