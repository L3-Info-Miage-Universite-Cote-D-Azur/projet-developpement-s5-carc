package logic.tile;

import java.util.PriorityQueue;
import java.util.Queue;

public class TileStack {
    private final Queue<TileData> tiles;

    public TileStack() {
        this.tiles = new PriorityQueue<>();
    }

    public int getNumTiles() {
        return tiles.size();
    }

    public TileData pick() {
        return tiles.remove();
    }
    public boolean addTileData(TileData data){
        return tiles.add(data);
    }
}
