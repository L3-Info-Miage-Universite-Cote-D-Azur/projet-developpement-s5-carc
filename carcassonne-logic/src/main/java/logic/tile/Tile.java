package logic.tile;

import logic.board.GameBoard;
import logic.config.TileData;
import logic.math.Vector2;

import java.util.Arrays;

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

    public Chunk getChuckPluggedWith(TileEdge edgeConnection) {
        return switch (edgeConnection) {
            case UP -> getChunk(ChunkOffset.UP);
            case DOWN -> getChunk(ChunkOffset.DOWN);
            case LEFT -> getChunk(ChunkOffset.LEFT);
            case RIGHT -> getChunk(ChunkOffset.RIGHT);
            default -> throw new IllegalArgumentException("Illegal edge connection");
        };
    }

    public boolean checkChunkCompatibility(Tile tile, TileEdge edgeConnection) {
        return getChuckPluggedWith(edgeConnection).isCompatibleWith(tile.getChuckPluggedWith(edgeConnection.negate()));
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
