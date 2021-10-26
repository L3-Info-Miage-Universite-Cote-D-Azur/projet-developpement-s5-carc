package logic.tile;

import logic.math.Vector2;

public class TownChunkTile extends Tile {
    public TownChunkTile(TileData data, Vector2 position) {
        super(data, position);
    }

    @Override
    public TileType getType() {
        return TileType.TOWN_CHUNK;
    }
}