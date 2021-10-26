package logic.tile;

public class RoadTile extends Tile{
    @Override
    public TileType getType() {
        return TileType.ROAD;
    }

    @Override
    public String toString() {
        return "RoadTile{}";
    }
}
