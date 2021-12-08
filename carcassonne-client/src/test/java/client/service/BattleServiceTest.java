package client.service;

import client.Client;
import client.ClientTestUtils;
import client.ai.heuristic.HeuristicAI;
import client.config.ClientConfig;
import logic.Game;
import logic.board.GameBoard;
import logic.command.ICommand;
import logic.command.ICommandExecutorListener;
import logic.command.PlaceTileDrawnCommand;
import logic.command.SkipMeeplePlacementCommand;
import logic.config.GameConfig;
import logic.player.IPlayerListener;
import logic.player.Player;
import logic.state.turn.GameTurnPlaceTileState;
import logic.tile.Tile;
import network.message.game.GameCommandMessage;
import network.message.game.GameDataMessage;
import network.message.game.GameMasterNextTurnDataMessage;
import network.message.game.GameResultMessage;
import org.junit.jupiter.api.Test;
import stream.ByteOutputStream;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class BattleServiceTest {
    @Test
    void testGameDataInitGameViewAndAttachAI() throws IOException {
        Client client = ClientTestUtils.createMockClient(null, 1);
        BattleService battleService = client.getBattleService();

        Game game = createGame(client);

        ByteOutputStream stream = new ByteOutputStream(1000);
        game.encode(stream, false);

        battleService.handleMessage(new GameDataMessage(stream.toByteArray()));

        Game gameView = battleService.getGameView();

        assertFalse(gameView.isMaster());

        assertEquals(game.getPlayer(0).getId(), gameView.getPlayer(0).getId());
        assertEquals(game.getPlayer(1).getId(), gameView.getPlayer(1).getId());
        assertEquals(game.getPlayer(2).getId(), gameView.getPlayer(2).getId());
        assertEquals(HeuristicAI.class, gameView.getPlayerById(1).getListener().getClass());
    }

    @Test
    void testGameCommandExecuteCommandOnGameView() throws IOException {
        Client client = ClientTestUtils.createMockClient(null, 1);
        BattleService battleService = client.getBattleService();

        Game game = createGame(client);

        ByteOutputStream stream = new ByteOutputStream(1000);
        game.encode(stream, false);

        battleService.handleMessage(new GameDataMessage(stream.toByteArray()));
        battleService.handleMessage(new GameCommandMessage(new PlaceTileDrawnCommand(GameBoard.STARTING_TILE_POSITION)));
        battleService.handleMessage(new GameCommandMessage(new SkipMeeplePlacementCommand()));

        Game gameView = battleService.getGameView();

        assertEquals(1, gameView.getBoard().getTileCount());
    }

    @Test
    void testGameNextTurnData() throws IOException {
        Client client = ClientTestUtils.createMockClient(null, 1);
        BattleService battleService = client.getBattleService();

        Game game = createGame(client);

        ByteOutputStream stream = new ByteOutputStream(1000);
        game.encode(stream, false);

        battleService.handleMessage(new GameDataMessage(stream.toByteArray()));

        Game gameView = battleService.getGameView();
        gameView.getPlayerById(1).setListener(new IPlayerListener() {
            @Override
            public void onWaitingPlaceTile() {

            }

            @Override
            public void onWaitingMeeplePlacement() {

            }

            @Override
            public void onWaitingDragonMove() {

            }
        });

        game.getCommandExecutor().setListener(new ICommandExecutorListener() {
            @Override
            public void onCommandExecuted(ICommand command) {
                battleService.handleMessage(new GameCommandMessage(command));
            }

            @Override
            public void onCommandFailed(ICommand command, String reason) {

            }

            @Override
            public void onCommandFailed(ICommand command, String reason, Object... args) {

            }
        });

        game.getCommandExecutor().execute(new PlaceTileDrawnCommand(GameBoard.STARTING_TILE_POSITION));
        game.getCommandExecutor().execute(new SkipMeeplePlacementCommand());

        battleService.handleMessage(new GameMasterNextTurnDataMessage(game.getConfig().tiles.indexOf(((GameTurnPlaceTileState )game.getState()).getTileDrawn().getConfig())));

        assertEquals(2, gameView.getTurnCount());
        assertEquals(gameView.getPlayer(1), gameView.getTurnExecutor());
    }

    @Test
    void testGameResult() throws IOException, NoSuchFieldException, IllegalAccessException {
        Client client = ClientTestUtils.createMockClient(null, 1);
        BattleService battleService = client.getBattleService();

        Field servicesField = Client.class.getDeclaredField("services");
        servicesField.setAccessible(true);
        Map<Class<? extends ServiceBase>, ServiceBase> services = (Map<Class<? extends ServiceBase>, ServiceBase>) servicesField.get(client);

        final boolean[] battleOverCalled = {false};
        services.put(GameStatisticsService.class, new GameStatisticsService(client) {
            @Override
            public void onBattleOver(Game game) {
                battleOverCalled[0] = true;
            }
        });

        Game game = createGame(client);

        ByteOutputStream gameSlaveStream = new ByteOutputStream(1000);
        game.encode(gameSlaveStream, false);

        battleService.handleMessage(new GameDataMessage(gameSlaveStream.toByteArray()));

        ByteOutputStream gameMasterStream = new ByteOutputStream(1000);
        game.encode(gameMasterStream, true);

        battleService.handleMessage(new GameResultMessage(gameMasterStream.toByteArray()));

        assertEquals(null, battleService.getGameView());
        assertTrue(battleOverCalled[0]);
    }

    private static Game createGame(Client client) {
        Game game = new Game(client.getGameConfig());

        game.addPlayer(new Player(3));
        game.addPlayer(new Player(1));
        game.addPlayer(new Player(2));
        game.start();

        ByteOutputStream stream = new ByteOutputStream(1000);
        game.encode(stream, false);

        return game;
    }
}
