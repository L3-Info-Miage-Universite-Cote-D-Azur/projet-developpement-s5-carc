package logic.tile;

public class TileStack {
    public int getNumTiles() {
        return 0;
    }

    public TileType pick() {
        throw new IllegalStateException("stack is empty");
    }
}
