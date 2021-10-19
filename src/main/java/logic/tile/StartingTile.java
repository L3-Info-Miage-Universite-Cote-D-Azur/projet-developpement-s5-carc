package logic.tile;

import logic.math.Vector2;

public class StartingTile extends Tile {
    public StartingTile(TileData data, Vector2 position) {
        super(data, position);
    }

    @Override
    public TileType getType() {
        return TileType.START;
    }
}
