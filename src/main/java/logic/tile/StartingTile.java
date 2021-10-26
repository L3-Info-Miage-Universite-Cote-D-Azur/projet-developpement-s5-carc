package logic.tile;

public class StartingTile extends Tile {
    @Override
    public TileType getType() {
        return TileType.START;
    }

    @Override
    public String toString() {
        return "StartingTile{}";
    }
}
