package stream;

import logic.Game;
import logic.tile.Tile;

/**
 * Helper class for byte-streams classes.
 */
public class ByteStreamHelper {
    /**
     * Encodes a tile into a byte-stream.
     * @param stream The byte-stream to write to.
     * @param tile The tile to encode.
     * @param game The game.
     */
    public static void encodeTile(ByteOutputStream stream, Tile tile, Game game) {
        int tileConfigIndex = game.getConfig().tiles.indexOf(tile.getConfig());
        assert tileConfigIndex != -1;
        stream.writeInt(tileConfigIndex);
        tile.encode(stream);
    }

    /**
     * Decodes a tile from a byte-stream.
     * @param stream The byte-stream to read from.
     * @param game The game.
     * @return The decoded tile.
     */
    public static Tile decodeTile(ByteInputStream stream, Game game) {
        int tileConfigIndex = stream.readInt();
        Tile tile = game.getConfig().tiles.get(tileConfigIndex).createTile(game);
        tile.decode(stream);
        return tile;
    }
}
