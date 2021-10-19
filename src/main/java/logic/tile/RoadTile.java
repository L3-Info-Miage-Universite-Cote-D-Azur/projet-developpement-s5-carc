package logic.tile;

import logic.math.Vector2;

public class RoadTile extends Tile{
    
    public RoadTile(Vector2 position) {
        super(position);
    }

    @Override
    public TileType getType() {
        return TileType.ROAD;
    }
}
