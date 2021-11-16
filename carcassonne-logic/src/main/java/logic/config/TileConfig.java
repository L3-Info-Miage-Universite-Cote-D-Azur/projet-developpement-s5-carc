package logic.config;

import logic.tile.ChunkOffset;
import logic.tile.Tile;
import logic.tile.TileFlags;

public class TileConfig {
    public ChunkConfig center;
    public ChunkConfig left;
    public ChunkConfig right;
    public ChunkConfig top;
    public ChunkConfig bot;
    public TileData details;


    public TileConfig() {
    }

    public boolean validate() {
        if (center == null) return false;
        if (left == null) return false;
        if (right == null) return false;
        if (top == null) return false;
        if (bot == null) return false;

        return true;
    }

    public Tile createTile() {
        Tile tile = new Tile(details);

        tile.setChunk(ChunkOffset.CENTER, center.createChunk(tile));
        tile.setChunk(ChunkOffset.top, top.createChunk(tile));
        tile.setChunk(ChunkOffset.bot, bot.createChunk(tile));
        tile.setChunk(ChunkOffset.LEFT, left.createChunk(tile));
        tile.setChunk(ChunkOffset.RIGHT, right.createChunk(tile));

        return tile;
    }
}
