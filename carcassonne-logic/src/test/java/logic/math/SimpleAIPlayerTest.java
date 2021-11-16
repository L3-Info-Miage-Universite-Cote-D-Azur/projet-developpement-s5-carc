package logic.math;

import logic.Game;
import logic.board.GameBoard;
import logic.player.SimpleAIPlayer;

import logic.tile.Tile;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


class SimpleAIPlayerTest {
    private static final GameConfig config = GameConfig.loadFromJSON("{\"MIN_PLAYERS\":2,\"MAX_PLAYERS\":5,\"TILES\":[{\"center\":{\"type\":\"ROAD\",\"relations\":[\"LEFT\",\"RIGHT\"]},\"top\":{\"type\":\"TOWN_WALL\",\"relations\":[]},\"bot\":{\"type\":\"FIELD\",\"relations\":[]},\"left\":{\"type\":\"ROAD\",\"relations\":[\"CENTER\"]},\"right\":{\"type\":\"ROAD\",\"relations\":[\"CENTER\"]},\"details\":{\"model\":\"D\",\"count\":1,\"expansion\":\"default\",\"flags\":[\"STARTING\"]}},{\"center\":{\"type\":\"ROAD\",\"relations\":[\"LEFT\",\"RIGHT\"]},\"top\":{\"type\":\"TOWN_WALL\",\"relations\":[]},\"bot\":{\"type\":\"FIELD\",\"relations\":[]},\"left\":{\"type\":\"ROAD\",\"relations\":[\"CENTER\"]},\"right\":{\"type\":\"ROAD\",\"relations\":[\"CENTER\"]},\"details\":{\"model\":\"D\",\"count\":3,\"expansion\":\"default\",\"flags\":[]}}]}");

    @Test
    void testOnceTilePlacementPerTurn() {
        for (int i = 1000; i > 0; i--) {
            Game game = createSinglePlayerGameEnv();

            for (int attemptedTileCount = 0; !game.isFinished(); attemptedTileCount++) {
                assertEquals(game.getBoard().getTileCount(), attemptedTileCount);
                game.update();
            }
        }
    }

    @Test
    void testStartingTilePlacement() {
        Game game = createSinglePlayerGameEnv();
        game.update();

        Tile startingTile = game.getBoard().getStartingTile();

        assertNotNull(startingTile);
        assertEquals(startingTile.getPosition(), GameBoard.STARTING_TILE_POSITION);
    }

    @Test
    void testTilePlacedToFreePointPlacement() {
        Game game = createSinglePlayerGameEnv();
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
        Game game = createSinglePlayerGameEnv();
        GameBoard board = game.getBoard();

        game.update();

        SimpleAIPlayer player = (SimpleAIPlayer) game.getPlayer(0);

        Tile tile = game.getStack().peek();
        ArrayList<Vector2> freePoints = board.findFreePlaceForTile(tile);
        int[] freePointPickedCount = new int[freePoints.size()];

        int testCount = 100000;

        for (int i = testCount; i > 0; i--) {
            int freePointPickedIndex = freePoints.indexOf(player.findPositionForTile(tile));
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

    private static Game createSinglePlayerGameEnv() {
        Game game = new Game(config);

        game.addPlayer(new SimpleAIPlayer(1));
        game.addPlayer(new SimpleAIPlayer(2));
        game.start();

        return game;
    }
}
