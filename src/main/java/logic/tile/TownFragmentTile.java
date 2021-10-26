package logic.tile;

import logic.math.Vector2;

public class TownFragmentTile extends Tile {

    public TownFragmentTile(TileData data, Vector2 position) {
        super(data, position);
    }

    @Override
    public TileType getType() {
        return TileType.TOWN;
    }

}