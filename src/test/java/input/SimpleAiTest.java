package input;

import input.ai.SimpleAI;
import logic.Game;
import logic.board.GameBoard;
import logic.config.GameConfig;
import logic.player.PlayerInfo;
import logic.tile.TileData;
import logic.tile.TileType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class SimpleAiTest {
    @Test
    void testPlaceTile() {
        Game game = new Game(new GameConfig());
        SimpleAI simpleAI = new SimpleAI();
        PlayerInfo playerInfo = new PlayerInfo(10);
        game.createPlayer(playerInfo, simpleAI);

        TileType tileType = TileType.START;
        TileData tileData = new TileData(tileType);

        assertTrue(simpleAI.placeTile(tileData));
        assertTrue(game.getBoard().hasTileAt(GameBoard.STARTING_TILE_POSITION));

        TileType tileType0 = TileType.ROAD;
        TileData tileData0 = new TileData(tileType0);
        assertTrue(simpleAI.placeTile(tileData0));
        assertNotEquals(tileType0 ,game.getBoard().getTileAt(GameBoard.STARTING_TILE_POSITION).getType());

    }
}
