package stream;

import logic.Game;
import logic.math.Vector2;
import logic.tile.Tile;

/**
 * Helper class for byte-streams classes.
 */
public class ByteStreamHelper {
    private ByteStreamHelper(){
        // ignored
    }

    /**
     * Encodes a tile into a byte-stream.
     *
     * @param stream The byte-stream to write to.
     * @param tile   The tile to encode.
     * @param game   The game.
     */
    public static void encodeTile(ByteOutputStream stream, Tile tile, Game game) {
        int tileConfigIndex = game.getConfig().getTileIndex(tile.getConfig());
        assert tileConfigIndex != -1;
        stream.writeInt(tileConfigIndex);
        tile.encode(stream);
    }

    /**
     * Decodes a tile from a byte-stream.
     *
     * @param stream The byte-stream to read from.
     * @param game   The game.
     * @return The decoded tile.
     */
    public static Tile decodeTile(ByteInputStream stream, Game game) {
        int tileConfigIndex = stream.readInt();
        Tile tile = game.getConfig().getTile(tileConfigIndex).createTile(game);
        tile.decode(stream);
        return tile;
    }

    /**
     * Encodes a vector into a byte-stream.
     *
     * @param stream The byte-stream to write to.
     * @param vector The vector to encode.
     */
    public static void encodeVector(ByteOutputStream stream, Vector2 vector) {
        stream.writeInt(vector.x());
        stream.writeInt(vector.y());
    }

    /**
     * Decodes a vector from a byte-stream.
     *
     * @param stream The byte-stream to read from.
     * @return The decoded vector.
     */
    public static Vector2 decodeVector(ByteInputStream stream) {
        return new Vector2(stream.readInt(), stream.readInt());
    }
}
