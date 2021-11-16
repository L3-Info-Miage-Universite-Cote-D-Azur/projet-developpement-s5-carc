package logic.config;

import logic.math.Direction;
import logic.tile.ChunkId;
import logic.tile.Tile;

import java.util.Map;

public class TileConfig {
    public Map<Direction, Map<Direction, ChunkConfig>> chunks;
    public TileData details;


    public TileConfig() {
    }

    public boolean validate() {
        if (chunks.size() != ChunkId.values().length) {
            return false;
        }

        for (Map<Direction, ChunkConfig> chunk : chunks.values()) {
            if (chunk.size() != Direction.values().length) {
                return false;
            }
        }

        return true;
    }

    public Tile createTile() {
        Tile tile = new Tile(details);

        for (Map.Entry<Direction, Map<Direction, ChunkConfig>> columnEntry : chunks.entrySet()) {
            Direction directionColumn = columnEntry.getKey();

            for (Map.Entry<Direction, ChunkConfig> rowEntry : columnEntry.getValue().entrySet()) {
                Direction directionRow = rowEntry.getKey();
                tile.setChunk(ChunkId.getChunkId(directionColumn, directionRow), rowEntry.getValue().createChunk(tile));
            }
        }

        return tile;
    }
}
