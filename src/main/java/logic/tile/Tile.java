package logic.tile;

import logic.board.GameBoard;
import logic.config.TileData;
import logic.math.Vector2;

import java.util.Arrays;
import java.util.EnumSet;

public class Tile {
    private Vector2 position;
    private Chunk[] chunks;
    private TileData data;

    public Tile(TileData data) {
        this.data = data;
        chunks = new Chunk[ChunkOffset.values().length];
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Chunk getChunk(ChunkOffset offset) {
        return chunks[offset.value];
    }

    public void setChunk(ChunkOffset offset, Chunk chunk) {
        chunks[offset.value] = chunk;
    }

    public boolean hasFlags(TileFlags flag) {
        return data.flags.contains(flag);
    }

    public boolean checkChunkCompatibility(Tile tile, TileEdge edgeConnection) {
        return switch (edgeConnection) {
            case UP -> tile.getChunk(ChunkOffset.DOWN).isCompatibleWith(getChunk(ChunkOffset.UP));
            case DOWN -> tile.getChunk(ChunkOffset.UP).isCompatibleWith(getChunk(ChunkOffset.DOWN));
            case LEFT -> tile.getChunk(ChunkOffset.RIGHT).isCompatibleWith(getChunk(ChunkOffset.LEFT));
            case RIGHT -> tile.getChunk(ChunkOffset.LEFT).isCompatibleWith(getChunk(ChunkOffset.RIGHT));
            default -> throw new IllegalArgumentException("Illegal edge connection");
        };
    }

    public boolean isPlaceableAt(Vector2 position, GameBoard board) {
        for (TileEdge edge : TileEdge.values()) {
            Tile edgeTile = board.getTileAt(position.add(edge.getValue()));

            if (edgeTile != null && !checkChunkCompatibility(edgeTile, edge)) {
                return false;
            }
        }

        return true;
    }

    public TileData getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Tile{" +
                "chunks=" + Arrays.toString(chunks) +
                ", flags=" + data.flags +
                '}';
    }
}
