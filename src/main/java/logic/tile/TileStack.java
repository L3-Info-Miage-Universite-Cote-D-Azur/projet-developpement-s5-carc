package logic.tile;

import logic.config.GameConfig;
import logic.config.TileConfig;

import java.util.*;

public class TileStack {
    private final LinkedList<TileData> tiles;

    public TileStack() {
        this.tiles = new LinkedList<>();
    }

    public void fill(GameConfig config) {
        for (Map.Entry<TileType, TileConfig> e : config.TILES.entrySet()) {
            TileType tileType = e.getKey();
            TileConfig tileConfig = e.getValue();

            for (int i = tileConfig.DECK_COUNT; i > 0; i--) {
                tiles.add(new TileData(tileType));
            }
        }
    }

    public int getNumTiles() {
        return tiles.size();
    }

    public TileData pick() {
        return tiles.remove();
    }

    public void shuffle() {
        Collections.shuffle(tiles);

        if (tiles.removeIf(t -> t.getType() == TileType.START)) {
            tiles.addFirst(new TileData(TileType.START));
        }
    }
}
