package client.ai.evaluator;

import logic.Game;
import logic.config.GameConfig;
import logic.math.Vector2;
import logic.meeple.Meeple;
import logic.player.Player;
import logic.tile.Tile;
import logic.tile.TileRotation;
import logic.tile.chunk.ChunkId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DragonMovementEvaluatorTest {
    private static DragonMovementEvaluator dragonMovementEvaluator;
    private static Game game;
    private static Tile tile3;
    private static Tile tile9;

    @BeforeAll
    static void setUp() {
        GameConfig config = GameConfig.loadFromResources();
        assertNotNull(config);
        game = new Game(config);
        Player player0 = new Player();
        Player player1 = new Player();
        Player player2 = new Player();
        game.addPlayer(player0);
        game.addPlayer(player1);
        game.addPlayer(player2);
        dragonMovementEvaluator = new DragonMovementEvaluator(game, player0);

        Tile tile1 = config.getTiles().stream().filter(t -> t.getModel().equals("V")).findFirst().get().createTile(game);
        Tile tile2 = config.getTiles().stream().filter(t -> t.getModel().equals("V")).findFirst().get().createTile(game);
        tile3 = config.getTiles().stream().filter(t -> t.getModel().equals("V")).findFirst().get().createTile(game);
        Tile tile4 = config.getTiles().stream().filter(t -> t.getModel().equals("V")).findFirst().get().createTile(game);
        Tile tile5 = config.getTiles().stream().filter(t -> t.getModel().equals("U")).findFirst().get().createTile(game);
        Tile tile6 = config.getTiles().stream().filter(t -> t.getModel().equals("U")).findFirst().get().createTile(game);
        Tile tile7 = config.getTiles().stream().filter(t -> t.getModel().equals("U")).findFirst().get().createTile(game);
        Tile tile8 = config.getTiles().stream().filter(t -> t.getModel().equals("U")).findFirst().get().createTile(game);
        tile9 = config.getTiles().stream().filter(t -> t.getModel().equals("U")).findFirst().get().createTile(game);
        Tile tile10 = config.getTiles().stream().filter(t -> t.getModel().equals("U")).findFirst().get().createTile(game);
        Tile tile11 = config.getTiles().stream().filter(t -> t.getModel().equals("AE")).findFirst().get().createTile(game);
        Tile tile12 = config.getTiles().stream().filter(t -> t.getModel().equals("E")).findFirst().get().createTile(game);

        tile1.setPosition(new Vector2(0, 0));
        tile2.setPosition(new Vector2(2, 0));
        tile3.setPosition(new Vector2(0, 3));
        tile4.setPosition(new Vector2(2, 3));
        tile5.setPosition(new Vector2(0, 1));
        tile6.setPosition(new Vector2(0, 2));
        tile7.setPosition(new Vector2(2, 1));
        tile8.setPosition(new Vector2(2, 2));
        tile9.setPosition(new Vector2(1, 0));
        tile10.setPosition(new Vector2(1, 3));
        tile11.setPosition(new Vector2(1, 2));
        tile12.setPosition(new Vector2(1, 1));

        tile1.setRotation(TileRotation.DOWN);
        tile2.setRotation(TileRotation.RIGHT);
        tile3.setRotation(TileRotation.LEFT);
        tile9.setRotation(TileRotation.LEFT);
        tile10.setRotation(TileRotation.LEFT);
        tile11.setRotation(TileRotation.DOWN);
        tile12.setRotation(TileRotation.UP);

        tile8.getChunk(ChunkId.CENTER_MIDDLE).setMeeple(new Meeple(player2));
        tile3.getChunk(ChunkId.CENTER_MIDDLE).setMeeple(new Meeple(player1));
        tile9.getChunk(ChunkId.CENTER_MIDDLE).setMeeple(new Meeple(player0));
        game.getBoard().spawnFairy(tile8.getChunk(ChunkId.EAST_TOP));

        game.getBoard().place(tile1);
        game.getBoard().place(tile2);
        game.getBoard().place(tile3);
        game.getBoard().place(tile4);
        game.getBoard().place(tile5);
        game.getBoard().place(tile6);
        game.getBoard().place(tile7);
        game.getBoard().place(tile8);
        game.getBoard().place(tile9);
        game.getBoard().place(tile10);
        game.getBoard().place(tile11);
        game.getBoard().place(tile12);
    }

    @Test
    void evaluate0() {
        assertTrue(game.getBoard().hasDragon());
        assertEquals(new Vector2(1, 2), game.getBoard().getDragon().getPosition());

        Vector2 up = new Vector2(1, 3);
        Vector2 down = new Vector2(1, 1);
        Vector2 left = new Vector2(0, 2);
        Vector2 right = new Vector2(2, 2);

        assertTrue(game.getBoard().getDragon().canMoveTo(up));
        assertTrue(game.getBoard().getDragon().canMoveTo(down));
        assertTrue(game.getBoard().getDragon().canMoveTo(left));
        assertFalse(game.getBoard().getDragon().canMoveTo(right));

        Vector2[] vector2s = {up, down, left, right};

        Vector2 minVec = vector2s[0];
        int minscore = Integer.MIN_VALUE;
        for (Vector2 vector2 : vector2s) {
            int score = dragonMovementEvaluator.evaluate(vector2);
            if (score > minscore) {
                minscore = score;
                minVec = vector2;
            }
        }

        int distanceDragonPlayer0Before = Vector2.manhattan(tile9.getPosition(), game.getBoard().getDragon().getPosition()); // Yellow
        int distanceDragonPlayer1Before = Vector2.manhattan(tile3.getPosition(), game.getBoard().getDragon().getPosition()); // Purple

        game.getBoard().getDragon().moveTo(minVec);

        int distanceDragonPlayer0After = Vector2.manhattan(tile9.getPosition(), game.getBoard().getDragon().getPosition()); // Yellow
        int distanceDragonPlayer1After = Vector2.manhattan(tile3.getPosition(), game.getBoard().getDragon().getPosition()); // Purple

        assertTrue(distanceDragonPlayer0Before < distanceDragonPlayer0After);
        assertTrue(distanceDragonPlayer1Before > distanceDragonPlayer1After);
    }

    @Test
    void evaluate1() {
        assertTrue(game.getBoard().hasDragon());
        assertEquals(new Vector2(1, 3), game.getBoard().getDragon().getPosition());

        Vector2 left = new Vector2(0, 3);
        Vector2 right = new Vector2(2, 3);
        Vector2 down = new Vector2(1, 2);

        assertFalse(game.getBoard().getDragon().canMoveTo(down));
        assertTrue(game.getBoard().getDragon().canMoveTo(left));
        assertTrue(game.getBoard().getDragon().canMoveTo(right));

        Vector2[] vector2s = {left, right};

        Vector2 minVec = vector2s[0];
        int minscore = Integer.MIN_VALUE;
        for (Vector2 vector2 : vector2s) {
            int score = dragonMovementEvaluator.evaluate(vector2);
            if (score > minscore) {
                minscore = score;
                minVec = vector2;
            }
        }

        int distanceDragonPlayer0Before = Vector2.manhattan(tile9.getPosition(), game.getBoard().getDragon().getPosition()); // Yellow
        int distanceDragonPlayer1Before = Vector2.manhattan(tile3.getPosition(), game.getBoard().getDragon().getPosition()); // Purple

        game.getBoard().getDragon().moveTo(minVec);

        int distanceDragonPlayer0After = Vector2.manhattan(tile9.getPosition(), game.getBoard().getDragon().getPosition()); // Yellow
        int distanceDragonPlayer1After = Vector2.manhattan(tile3.getPosition(), game.getBoard().getDragon().getPosition()); // Purple

        assertTrue(distanceDragonPlayer0Before < distanceDragonPlayer0After);
        assertTrue(distanceDragonPlayer1Before > distanceDragonPlayer1After);
    }
}