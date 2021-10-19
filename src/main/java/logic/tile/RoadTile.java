package logic.tile;

import logic.math.Vector2;

public class RoadTile extends Tile{
    public RoadTile(TileData data, Vector2 position) {
        super(data, position);
    }

    @Override
    public TileType getType() {
        return TileType.ROAD;
    }
}
