package logic.tile;

import logic.board.GameBoard;
import logic.config.excel.TileExcelConfig;
import logic.math.Vector2;

import java.util.Arrays;

public class Tile {
    private Vector2 position;
    private Chunk[] chunks;
    private TileExcelConfig config;

    public Tile(TileExcelConfig config) {
        this.config = config;
        chunks = new Chunk[ChunkId.values().length];

        for (ChunkId chunkId : ChunkId.values()) {
            chunks[chunkId.ordinal()] = new Chunk(this, ChunkType.FIELD, new ChunkId[0]);
        }
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Chunk getChunk(ChunkId id) {
        return chunks[id.ordinal()];
    }

    public void setChunk(ChunkId id, Chunk chunk) {
        chunks[id.ordinal()] = chunk;
    }

    public boolean hasFlags(TileFlags flag) {
        return config.flags.contains(flag);
    }

    public boolean checkChunkCompatibility(Tile tile, TileEdge edgeConnection) {
        ChunkId[] ownChunkIds = edgeConnection.getChunkIds();
        ChunkId[] oppositeChunkIds = edgeConnection.negate().getChunkIds();

        for (int i = 0; i < ownChunkIds.length; i++) {
            ChunkId ownChunkId = ownChunkIds[i];
            ChunkId oppositeChunkId = oppositeChunkIds[i];

            if (!getChunk(ownChunkId).isCompatibleWith(tile.getChunk(oppositeChunkId))) {
                return false;
            }
        }

        return true;
    }

    public boolean canBePlacedAt(Vector2 position, GameBoard board) {
        boolean hasContactWithTile = false;

        for (TileEdge edge : TileEdge.values()) {
            Tile edgeTile = board.getTileAt(position.add(edge.getValue()));

            if (edgeTile != null) {
                hasContactWithTile = true;

                if (!checkChunkCompatibility(edgeTile, edge)) {
                    return false;
                }
            }
        }

        return hasContactWithTile;
    }

    public TileExcelConfig getConfig() {
        return config;
    }

    @Override
    public String toString() {
        return "Tile{" +
                "chunks=" + Arrays.toString(chunks) +
                ", flags=" + config.flags +
                '}';
    }
}
