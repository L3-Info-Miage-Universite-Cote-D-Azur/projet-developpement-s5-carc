package stream;

import logic.Game;
import logic.tile.Tile;

public class ByteStreamHelper {
    public static void encodeTile(ByteOutputStream stream, Tile tile, Game game) {
        int tileConfigIndex = game.getConfig().tiles.indexOf(tile.getConfig());
        assert tileConfigIndex != -1;
        stream.writeInt(tileConfigIndex);
        tile.encode(stream);
    }

    public static Tile decodeTile(ByteInputStream stream, Game game) {
        int tileConfigIndex = stream.readInt();
        Tile tile = game.getConfig().tiles.get(tileConfigIndex).createTile();
        tile.decode(stream, game);
        return tile;
    }
}
