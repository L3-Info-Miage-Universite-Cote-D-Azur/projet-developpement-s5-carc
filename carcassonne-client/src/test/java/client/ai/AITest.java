package client.ai;

import logic.Game;
import logic.command.ICommand;
import logic.config.GameConfig;
import logic.math.Vector2;
import logic.player.Player;
import logic.state.turn.GameTurnPlaceTileState;
import logic.tile.Direction;
import logic.tile.TileFlags;
import logic.tile.TileRotation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reflection.ReflectionUtils;

public class AITest {
    private Game game;

    @BeforeEach
    void setup() {
        game = Mockito.spy(new Game(GameConfig.loadFromResources()) {
            @Override
            public boolean executeCommand(ICommand command) {
                return true;
            }
        });

        game.addPlayer(new Player(1));
        game.addPlayer(new Player(2));
        game.start();
    }

    @Test
    void testWaitingPlaceTile() throws Exception {
        AI ai = Mockito.mock(AI.class);
        ReflectionUtils.setField(ai, "player", game.getPlayer(0));
        Mockito.doCallRealMethod().when(ai).onWaitingPlaceTile();
        Mockito.doCallRealMethod().when(ai).getGame();

        GameTurnPlaceTileState state = (GameTurnPlaceTileState) game.getState();

        Mockito.when(ai.findPositionForTile(state.getTileDrawn())).thenReturn(new TilePosition(new Vector2(0, 0), TileRotation.DOWN));

        ai.onWaitingPlaceTile();

        Mockito.verify(ai, Mockito.times(1)).findPositionForTile(state.getTileDrawn());
        Mockito.verify(game, Mockito.times(2)).executeCommand(Mockito.any());
    }

    @Test
    void testWaitingPlaceMeeple() throws Exception {
        AI ai = Mockito.mock(AI.class);
        ReflectionUtils.setField(ai, "player", game.getPlayer(0));
        Mockito.doCallRealMethod().when(ai).onWaitingMeeplePlacement();
        Mockito.doCallRealMethod().when(ai).getGame();

        GameTurnPlaceTileState state = (GameTurnPlaceTileState) game.getState();

        state.getTileDrawn().setPosition(new Vector2(0, 0));
        state.getTileDrawn().getConfig().getFlags().add(TileFlags.PRINCESS);
        game.getBoard().place(state.getTileDrawn());
        state.complete();

        Mockito.when(ai.findChunkToPlaceMeeple(state.getTileDrawn())).thenReturn(null);
        Mockito.when(ai.findChunkToRemoveMeeple(state.getTileDrawn())).thenReturn(null);
        Mockito.when(ai.findChunkToPlaceFairy()).thenReturn(null);

        ai.onWaitingMeeplePlacement();

        Mockito.verify(ai, Mockito.times(1)).findChunkToPlaceMeeple(state.getTileDrawn());
        Mockito.verify(ai, Mockito.times(1)).findChunkToRemoveMeeple(state.getTileDrawn());
        Mockito.verify(ai, Mockito.times(1)).findChunkToPlaceFairy();
        Mockito.verify(game, Mockito.times(1)).executeCommand(Mockito.any());
    }

    @Test
    void testWaitingDragonMove() throws Exception {
        AI ai = Mockito.mock(AI.class);
        ReflectionUtils.setField(ai, "player", game.getPlayer(0));
        Mockito.doCallRealMethod().when(ai).onWaitingDragonMove();
        Mockito.doCallRealMethod().when(ai).getGame();

        GameTurnPlaceTileState state = (GameTurnPlaceTileState) game.getState();

        state.getTileDrawn().setPosition(new Vector2(0, 0));
        state.getTileDrawn().getConfig().getFlags().add(TileFlags.PRINCESS);
        game.getBoard().place(state.getTileDrawn());
        state.complete();

        Mockito.when(ai.findDirectionForDragon(null)).thenReturn(Direction.LEFT);

        ai.onWaitingDragonMove();

        Mockito.verify(ai, Mockito.times(1)).findDirectionForDragon(null);
        Mockito.verify(game, Mockito.times(1)).executeCommand(Mockito.any());
    }
}
