package logic.tile;

import java.util.ArrayList;

public class TileDeck {
    private final ArrayList<TileData> tiles;

    public TileDeck() {
        this.tiles = new ArrayList<>();
    }

    public int getNumTiles() {
        return tiles.size();
    }

    public TileData getTileAt(int index) {
        return tiles.get(index);
    }
}
