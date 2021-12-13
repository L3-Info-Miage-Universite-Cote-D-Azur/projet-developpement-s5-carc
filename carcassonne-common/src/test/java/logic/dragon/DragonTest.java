package logic.dragon;

import logic.Game;
import logic.TestUtils;
import logic.board.GameBoard;
import logic.math.Vector2;
import logic.meeple.Meeple;
import logic.tile.Direction;
import logic.tile.Tile;
import logic.tile.TileRotation;
import logic.tile.chunk.ChunkId;
import org.junit.jupiter.api.Test;
import stream.ByteInputStream;
import stream.ByteOutputStream;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DragonTest {
    private static List<Tile> findNeighborTiles(GameBoard board, Vector2 position) {
        return Arrays.stream(Direction.values()).map(d -> d.value().add(position)).filter(board::hasTileAt).map(board::getTileAt).toList();
    }

    @Test
    void testMoveTo() {
        Game game = TestUtils.initGameEnv(2, true, true);
        GameBoard board = game.getBoard();

        Dragon dragon = board.spawnDragon(GameBoard.STARTING_TILE_POSITION);
        assertEquals(GameBoard.STARTING_TILE_POSITION, dragon.getPosition());

        Vector2 newPosition = null;

        for (Direction direction : Direction.values()) {
            newPosition = dragon.getPosition().add(direction.value());

            if (dragon.canMoveTo(newPosition)) {
                Tile tile = board.getTileAt(newPosition);

                tile.getChunk(ChunkId.CENTER_MIDDLE).setMeeple(new Meeple(game.getPlayer(0)));
                tile.getChunk(ChunkId.EAST_BOTTOM).setMeeple(new Meeple(game.getPlayer(1)));

                break;
            }
        }

        dragon.moveTo(newPosition);

        assertEquals(newPosition, dragon.getPosition());

        Tile newTile = board.getTileAt(newPosition);

        assertNull(newTile.getChunk(ChunkId.CENTER_MIDDLE).getMeeple());
        assertNull(newTile.getChunk(ChunkId.EAST_BOTTOM).getMeeple());
    }

    @Test
    void testMoveOnWrongPosition() {
        Game game = TestUtils.initGameEnv(2, true, true);
        GameBoard board = game.getBoard();

        Vector2 startingPosition = GameBoard.STARTING_TILE_POSITION;
        Dragon dragon = board.spawnDragon(startingPosition);
        assertEquals(startingPosition, dragon.getPosition());

        /* Cannot move on the current position */
        assertFalse(dragon.canMoveTo(startingPosition));

        List<Tile> neighborTiles = findNeighborTiles(board, startingPosition);

        /* Can move on a neighbor tile */
        for (Tile tile : neighborTiles) {
            assertTrue(dragon.canMoveTo(tile.getPosition()));
        }

        board.spawnFairy(neighborTiles.get(0).getChunk(ChunkId.CENTER_MIDDLE));

        assertFalse(dragon.canMoveTo(neighborTiles.get(0).getPosition()));

        board.killFairy();

        dragon.moveTo(neighborTiles.get(0).getPosition());

        /* Cannot move on the previous position */
        assertFalse(dragon.canMoveTo(startingPosition));
    }

    @Test
    void testMoveToThrowIfMoveOnPathPosition() {
        Dragon dragon = new Dragon(null, new Vector2(0, 0));

        try {
            dragon.moveTo(new Vector2(0, 0));
            fail("Expected IllegalArgumentException to be thrown");
        }
        catch (IllegalArgumentException e){
            // Test exception message...
        }
    }

    @Test
    void testBlocked() {
        Game game = TestUtils.initGameEnv(2, true, false);

        Tile tile1 = game.getConfig().getTiles().stream().filter(t -> t.getModel().equals("S")).findFirst().get().createTile(game);
        Tile tile2 = game.getConfig().getTiles().stream().filter(t -> t.getModel().equals("S")).findFirst().get().createTile(game);
        Tile tile3 = game.getConfig().getTiles().stream().filter(t -> t.getModel().equals("S")).findFirst().get().createTile(game);
        Tile tile4 = game.getConfig().getTiles().stream().filter(t -> t.getModel().equals("S")).findFirst().get().createTile(game);

        tile1.setPosition(new Vector2(0, 0));
        tile2.setPosition(new Vector2(1, 0));

        tile3.setRotation(TileRotation.DOWN);
        tile3.setPosition(new Vector2(0, 1));

        tile4.setRotation(TileRotation.DOWN);
        tile4.setPosition(new Vector2(1, 1));

        game.getBoard().place(tile1);
        game.getBoard().place(tile2);
        game.getBoard().place(tile3);
        game.getBoard().place(tile4);

        Dragon dragon = game.getBoard().spawnDragon(new Vector2(0, 0));

        assertFalse(dragon.canMoveTo(new Vector2(1, 1)));

        assertTrue(dragon.canMoveTo(new Vector2(1, 0)));
        dragon.moveTo(new Vector2(1, 0));
        assertFalse(dragon.isBlocked());

        assertTrue(dragon.canMoveTo(new Vector2(1, 1)));
        dragon.moveTo(new Vector2(1, 1));
        assertFalse(dragon.isBlocked());

        assertTrue(dragon.canMoveTo(new Vector2(0, 1)));
        dragon.moveTo(new Vector2(0, 1));
        assertTrue(dragon.isBlocked());
    }

    @Test
    void testEncodingDecoding() {
        Game game = TestUtils.initGameEnv(2, true, true);
        GameBoard board = game.getBoard();

        Dragon dragon = board.spawnDragon(GameBoard.STARTING_TILE_POSITION);

        // fill path
        for (int i = 0; i < 4; i++) {
            for (Direction direction : Direction.values()) {
                Vector2 newPosition = dragon.getPosition().add(direction.value());

                if (dragon.canMoveTo(newPosition)) {
                    dragon.moveTo(newPosition);
                    break;
                }
            }
        }

        Dragon copy = new Dragon(board);

        ByteOutputStream out = new ByteOutputStream(10);
        dragon.encode(out);
        ByteInputStream in = new ByteInputStream(out.getBytes(), out.getLength());
        copy.decode(in);

        List<Vector2> originalPath = dragon.getPath();
        List<Vector2> copyPath = copy.getPath();

        assertEquals(originalPath.size(), copyPath.size());

        for (int i = 0; i < originalPath.size(); i++) {
            assertEquals(originalPath.get(i), copyPath.get(i));
        }
    }
}
