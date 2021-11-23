package logic.tile;

import logic.Game;
import logic.board.GameBoard;
import logic.config.excel.TileExcelConfig;
import logic.math.Vector2;
import stream.ByteInputStream;
import stream.ByteOutputStream;

import java.util.Arrays;

/**
 * Represents a tile on the game board.
 */
public class Tile {
    private Vector2 position;
    private Chunk[] chunks;
    private TileExcelConfig config;

    private int rotation;

    public Tile(TileExcelConfig config) {
        chunks = new Chunk[ChunkId.values().length];
        this.config = config;
    }

    /**
     * Rotate the tile.
     */
    public void rotate() {
        Chunk[] originalChunkOrder = chunks.clone();

        for (int i = 0; i< originalChunkOrder.length; i++) {
            Chunk chunk = originalChunkOrder[(i + 3) % (ChunkId.values().length - 1)];
            chunks[i] = chunk;
        }

        rotation = (rotation + 1) % 4;
    }

    /**
     * Gets the position of the tile.
     * @return The position of the tile.
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * Sets the position of the tile.
     * @param position The position of the tile.
     */
    public void setPosition(Vector2 position) {
        this.position = position;
    }

    /**
     * Gets the chunk with the given id.
     * @param id The id of the chunk.
     * @return The chunk with the given id.
     */
    public Chunk getChunk(ChunkId id) {
        return chunks[id.ordinal()];
    }

    /**
     * Sets the chunk with the given id.
     * @param id The id of the chunk.
     * @param chunk The chunk to set.
     */

    public void setChunk(ChunkId id, Chunk chunk) {
        chunks[id.ordinal()] = chunk;
    }

    /**
     * Determines if the tile has the specified flag.
     * @param flag The flag to check.
     * @return True if the tile has the specified flag, false otherwise.
     */
    public boolean hasFlags(TileFlags flag) {
        return config.flags.contains(flag);
    }

    /**
     * Checks if the chunks that are connected to the given tile chunks are compatible.
     * @param tile The tile to check.
     * @param edgeConnection The edge connection which is being checked.
     * @return True if the chunks are compatible, false otherwise.
     */
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

    /**
     * Determines if the tile can be placed at the given position.
     * @param position The position to check.
     * @param board The board to check.
     * @return True if the tile can be placed at the given position, false otherwise.
     */
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

    /**
     * Gets the tile's config.
     * @return The tile's config.
     */
    public TileExcelConfig getConfig() {
        return config;
    }

    /**
     * Encodes the tile.
     * @param stream The stream to encode to.
     */
    public void encode(ByteOutputStream stream) {
        if (position != null) {
            stream.writeBoolean(true);
            stream.writeInt(position.getX());
            stream.writeInt(position.getY());
        } else {
            stream.writeBoolean(false);
        }

        for (Chunk chunk : chunks) {
            chunk.encode(stream);
        }
    }

    /**
     * Decodes the tile.
     * @param stream The stream to decode from.
     */
    public void decode(ByteInputStream stream, Game game) {
        if (stream.readBoolean()) {
            position = new Vector2(stream.readInt(), stream.readInt());
        } else {
            position = null;
        }

        for (Chunk chunk : chunks) {
            chunk.decode(stream, game);
        }
    }

    @Override
    public String toString() {
        return "Tile{" +
                "chunks=" + Arrays.toString(chunks) +
                ", flags=" + config.flags +
                '}';
    }
}
