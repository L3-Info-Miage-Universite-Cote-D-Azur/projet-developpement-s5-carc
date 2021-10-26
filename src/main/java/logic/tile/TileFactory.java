package logic.tile;

public class TileFactory {
    public static Tile createByType(TileType type) {
        return switch (type) {
            case START -> new StartingTile();
            case ROAD -> new RoadTile();
            case TOWN_CHUNK -> new TownChunkTile();
            case ABBEY -> new AbbeyTile();
            default -> throw new IllegalArgumentException("data");
        };
    }
}
