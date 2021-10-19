package logic.tile;

import logic.config.GameConfig;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class TileStack {
    private final Queue<TileData> tiles;

    public TileStack() {
        this.tiles = new LinkedList<>();
    }

    public void fill(GameConfig config) {
        tiles.add(new TileData(TileType.START));
        tiles.add(new TileData(TileType.ROAD));
        tiles.add(new TileData(TileType.ROAD));
        tiles.add(new TileData(TileType.ROAD));
        tiles.add(new TileData(TileType.ROAD));
        tiles.add(new TileData(TileType.ROAD));
    }

    public int getNumTiles() {
        return tiles.size();
    }

    public TileData pick() {
        return tiles.remove();
    }
}
