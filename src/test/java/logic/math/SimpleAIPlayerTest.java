package logic.math;

import logic.Game;
import logic.board.GameBoard;
import logic.config.GameConfig;
import logic.player.SimpleAIPlayer;
import logic.tile.Tile;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


class SimpleAIPlayerTest {
    @Test
    void testOnceTilePlacementPerTurn() {
        for (int i = 1000; i > 0; i--) {
            Game game = createSinglePlayerGameEnv(new GameConfig());

            for (int attemptedTileCount = 0; !game.isFinished(); attemptedTileCount++) {
                assertEquals(game.getBoard().getTileCount(), attemptedTileCount);
                game.update();
            }
        }
    }

    @Test
    void testStartingTilePlacement() {
        Game game = createSinglePlayerGameEnv(new GameConfig());
        game.update();

        Tile startingTile = game.getBoard().getStartingTile();

        assertNotNull(startingTile);
        assertEquals(startingTile.getPosition(), GameBoard.STARTING_TILE_POSITION);
    }

    @Test
    void testTilePlacedToFreePointPlacement() {
        Game game = createSinglePlayerGameEnv(new GameConfig());
        GameBoard board = game.getBoard();

        while (!game.isFinished()) {
            ArrayList<Vector2> freePoints = game.getBoard().findFreePlaceForTile(game.getStack().peek());
            game.update();

            if (board.getTileCount() >= 2) {
                boolean hasTileOnFreePoint = false;

                for (Vector2 previousFreePoint : freePoints) {
                    if (board.hasTileAt(previousFreePoint)) {
                        hasTileOnFreePoint = true;
                        break;
                    }
                }

                assertTrue(hasTileOnFreePoint);
            }
        }
    }

    @Test
    void testTilePlacedToRandomFreePointPlacement() {
        Game game = createSinglePlayerGameEnv(new GameConfig());
        GameBoard board = game.getBoard();

        game.update();

        SimpleAIPlayer player = (SimpleAIPlayer) game.getPlayer(0);

        Tile tile = game.getStack().peek();
        ArrayList<Vector2> freePoints = board.findFreePlaceForTile(tile);
        int[] freePointPickedCount = new int[freePoints.size()];

        int testCount = 100000;

        for (int i = testCount; i > 0; i--) {
            int freePointPickedIndex = freePoints.indexOf(player.findFreePositionForTile(tile));
            assertNotEquals(freePointPickedIndex, -1);
            freePointPickedCount[freePointPickedIndex]++;
        }

        int[] freePointPickedPercentage = new int[freePoints.size()];

        for (int i = 0; i < freePointPickedPercentage.length; i++) {
            freePointPickedPercentage[i] = (int) (100L * freePointPickedCount[i] / testCount);
        }

        for (int percentage : freePointPickedPercentage) {
            int idealPercentage = 100 / freePoints.size();
            int maxPercentage = (int) (idealPercentage * 1.25);
            int minPercentage = (int) (idealPercentage * 0.75);

            assertTrue(percentage >= minPercentage);
            assertTrue(percentage <= maxPercentage);
        }
    }

    private static Game createSinglePlayerGameEnv(GameConfig config) {
        Game game = new Game(config);

        game.createPlayer(new SimpleAIPlayer(1));
        game.createPlayer(new SimpleAIPlayer(2));
        game.start();

        return game;
    }
}
