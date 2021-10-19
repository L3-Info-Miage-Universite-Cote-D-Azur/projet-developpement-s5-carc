package logic.tile;

import logic.math.Vector2;

public class TileFactory {
    public static Tile create(TileData data, Vector2 position) {
        return switch (data.getType()) {
            case START -> new StartingTile(data, position);
            case ROAD -> new RoadTile(data, position);
            default -> throw new IllegalArgumentException("data");
        };
    }
}
