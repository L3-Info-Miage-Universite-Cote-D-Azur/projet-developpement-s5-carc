package logic.tile;

import logic.board.GameBoard;
import logic.math.Vector2;

import java.util.Arrays;
import java.util.EnumSet;

public class Tile {
    private Vector2 position;
    private Chunk[] chunks;
    private EnumSet<TileFlags> flags;

    public Tile() {
        chunks = new Chunk[ChunkOffset.values().length];
        flags = EnumSet.noneOf(TileFlags.class);
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
        return flags.contains(flag);
    }

    public void setFlags(TileFlags flag, boolean value) {
        if (value) {
            if (!hasFlags(flag)) {
                flags.add(flag);
            }
        } else {
            flags.remove(flag);
        }
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

    @Override
    public String toString() {
        return "Tile{" +
                "chunks=" + Arrays.toString(chunks) +
                ", flags=" + flags +
                '}';
    }
}
