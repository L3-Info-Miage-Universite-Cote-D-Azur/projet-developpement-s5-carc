package logic.tile;

import logic.math.Vector2;

public class AbbeyTile extends Tile{


    public AbbeyTile(TileData data, Vector2 position) {
        super(data, position);
    }

    @Override
    public TileType getType() {
        return TileType.ABBEY;
    }
}
