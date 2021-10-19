package logic.tile;

import logic.math.Vector2;

public class StartingTile extends Tile {
    public StartingTile(Vector2 position) {
        super(position);
    }

    @Override
    public TileType getType() {
        return TileType.START;
    }
}
