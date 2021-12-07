package logic.tile;

import logic.Game;
import logic.board.GameBoard;
import logic.config.excel.TileConfig;
import logic.math.Vector2;
import logic.tile.chunk.Chunk;
import logic.tile.area.Area;
import logic.tile.chunk.ChunkId;
import logic.tile.chunk.ChunkType;
import stream.ByteInputStream;
import stream.ByteOutputStream;
import stream.ByteStreamHelper;

import java.util.Arrays;
import java.util.List;

/**
 * Represents a tile on the game board.
 */
public class Tile {
    private final TileConfig config;
    private final Game game;

    private Vector2 position;
    private Chunk[] chunks;
    private TileRotation rotation;

    public Tile(TileConfig config, Game game) {
        chunks = new Chunk[ChunkId.values().length];
        this.config = config;
        this.game = game;
        this.rotation = TileRotation.UP;
    }

    /**
     * Rotates the tile to 90 degrees in the clockwise direction.
     */
    public void rotate() {
        Chunk[] originalChunkOrder = chunks.clone();

        for (int i = 0; i < originalChunkOrder.length - 1; i++) {
            setChunk(ChunkId.values()[(i + 3) % (ChunkId.values().length - 1)], originalChunkOrder[i]);
        }

        this.rotation = this.rotation.next();
    }

    /**
     * Gets the tile's rotation.
     *
     * @return The tile's rotation.
     */
    public TileRotation getRotation() {
        return rotation;
    }

    /**
     * Rotates the tile to the given rotation.
     */
    public void setRotation(TileRotation rotation) {
        while (this.rotation != rotation) {
            rotate();
        }
    }

    /**
     * Gets the game the tile is in.
     *
     * @return The game the tile is in.
     */
    public final Game getGame() {
        return game;
    }

    /**
     * Gets the position of the tile.
     *
     * @return The position of the tile.
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * Sets the position of the tile.
     *
     * @param position The position of the tile.
     */
    public void setPosition(Vector2 position) {
        this.position = position;
    }

    /**
     * Determines if the tile is on the board.
     *
     * @return True if the tile is on the board, false otherwise.
     */
    public boolean isOnBoard() {
        return position != null;
    }

    /**
     * Gets the chunk with the given id.
     *
     * @param id The id of the chunk.
     * @return The chunk with the given id.
     */
    public Chunk getChunk(ChunkId id) {
        return chunks[id.ordinal()];
    }

    /**
     * Sets the chunk with the given id.
     *
     * @param id    The id of the chunk.
     * @param chunk The chunk to set.
     */

    public void setChunk(ChunkId id, Chunk chunk) {
        chunks[id.ordinal()] = chunk;
        chunk.setCurrentId(id);
    }

    /**
     * Determines if the tile has the specified flag.
     *
     * @param flag The flag to check.
     * @return True if the tile has the specified flag, false otherwise.
     */
    public boolean hasFlag(TileFlags flag) {
        return config.flags.contains(flag);
    }

    /**
     * Gets whether the tile has a portal.
     *
     * @return True if the tile has a portal, false otherwise.
     */
    public boolean hasPortal() {
        return config.flags.contains(TileFlags.PORTAL);
    }

    /**
     * Gets whether the tile has a volcano.
     *
     * @return True if the tile has a volcano, false otherwise.
     */
    public boolean hasVolcano() {
        return config.flags.contains(TileFlags.VOLCANO);
    }

    /**
     * Checks if the chunks that are connected to the given tile chunks are compatible.
     *
     * @param tile           The tile to check.
     * @param edgeConnection The edge connection which is being checked.
     * @return True if the chunks are compatible, false otherwise.
     */
    public boolean checkChunkCompatibility(Tile tile, Direction edgeConnection) {
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
     *
     * @param position The position to check.
     * @return True if the tile can be placed at the given position, false otherwise.
     */
    public boolean canBePlacedAt(Vector2 position) {
        boolean hasContactWithTile = false;

        for (Direction edge : Direction.values()) {
            Tile edgeTile = game.getBoard().getTileAt(position.add(edge.value()));

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
     * Gets the areas that are connected to the tile.
     * @return The areas that are connected to the tile.
     */
    public List<Area> getAreas() {
        return Arrays.stream(chunks).map(Chunk::getArea).distinct().toList();
    }

    /**
     * Tries to merge the chunk areas with the areas that are connected to the given edge.
     *
     * @param neighborTile The tile to merge with.
     */
    public void mergeAreas(Tile neighborTile, Direction edgeConnection) {
        ChunkId[] ownChunkIds = edgeConnection.getChunkIds();
        ChunkId[] oppositeChunkIds = edgeConnection.negate().getChunkIds();

        for (int i = 0; i < ownChunkIds.length; i++) {
            ChunkId ownChunkId = ownChunkIds[i];
            ChunkId neighborChunkId = oppositeChunkIds[i];

            Area ownChunkArea = getChunk(ownChunkId).getArea();
            Area neighborChunkArea = neighborTile.getChunk(neighborChunkId).getArea();

            /* We are checking if the area of this chunk is not merged already. */
            if (ownChunkArea != neighborChunkArea) {
                neighborChunkArea.merge(ownChunkArea);
            }
        }
    }

    /**
     * Tries to merge the chunk areas with the areas that are connected to the edges of the tile.
     */
    public void mergeAreas() {
        GameBoard board = game.getBoard();

        for (Direction edge : Direction.values()) {
            Tile edgeTile = board.getTileAt(position.add(edge.value()));

            if (edgeTile != null) {
                mergeAreas(edgeTile, edge);
            }
        }
    }

    /**
     * Called when the tile is placed on the board.
     */
    public void onBoard() {
        mergeAreas();

        if (hasVolcano()) {
            game.getBoard().spawnDragon(getPosition());
        }
    }

    /**
     * Gets the tile's config.
     *
     * @return The tile's config.
     */
    public TileConfig getConfig() {
        return config;
    }

    /**
     * Encodes the tile.
     *
     * @param stream The stream to encode to.
     */
    public void encode(ByteOutputStream stream) {
        if (position != null) {
            stream.writeBoolean(true);
            ByteStreamHelper.encodeVector(stream, position);
            stream.writeInt(rotation.ordinal());
        } else {
            stream.writeBoolean(false);
        }

        for (Chunk chunk : chunks) {
            chunk.encode(stream);
        }
    }

    /**
     * Decodes the tile.
     *
     * @param stream The stream to decode from.
     */
    public void decode(ByteInputStream stream) {
        if (stream.readBoolean()) {
            position = ByteStreamHelper.decodeVector(stream);
            setRotation(TileRotation.values()[stream.readInt()]);
        } else {
            position = null;
        }

        for (Chunk chunk : chunks) {
            chunk.decode(stream);
        }
    }

    @Override
    public String toString() {
        return "Tile{" +
                "position=" + position +
                ", model=" + config.model +
                ", rotation=" + rotation +
                ", flags=" + config.flags +
                '}';
    }
}
