package logic.tile;

public class TownChunkTile extends Tile {
    @Override
    public TileType getType() {
        return TileType.TOWN_CHUNK;
    }

    @Override
    public String toString() {
        return "TownChunkTile{}";
    }
}