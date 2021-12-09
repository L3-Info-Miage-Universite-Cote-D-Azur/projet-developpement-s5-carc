package client.service;

import client.Client;
import client.ClientTestUtils;
import client.ai.heuristic.HeuristicAI;
import logic.Game;
import logic.IGameListener;
import logic.board.GameBoard;
import logic.command.ICommand;
import logic.command.PlaceTileDrawnCommand;
import logic.command.SkipMeeplePlacementCommand;
import logic.dragon.Dragon;
import logic.dragon.Fairy;
import logic.meeple.Meeple;
import logic.player.IPlayerListener;
import logic.player.Player;
import logic.state.GameState;
import logic.state.turn.GameTurnPlaceTileState;
import logic.tile.Tile;
import logic.tile.chunk.Chunk;
import network.message.game.GameCommandMessage;
import network.message.game.GameDataMessage;
import network.message.game.GameMasterNextTurnDataMessage;
import network.message.game.GameResultMessage;
import org.junit.jupiter.api.Test;
import reflection.ReflectionUtils;
import stream.ByteOutputStream;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BattleServiceTest {
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

        game.setListener(new IGameListener() {
            @Override
            public void onTurnStarted(int turn, Tile tileDrawn) {

            }

            @Override
            public void onTurnEnded(int turn) {

            }

            @Override
            public void onGameStarted() {

            }

            @Override
            public void onGameEnded() {

            }

            @Override
            public void onStateChanged(GameState state) {

            }

            @Override
            public void onTilePlaced(Tile tile) {

            }

            @Override
            public void onTileRotated(Tile tile) {

            }

            @Override
            public void onMeeplePlaced(Chunk chunk, Meeple meeple) {

            }

            @Override
            public void onMeepleRemoved(Chunk chunk, Meeple meeple) {

            }

            @Override
            public void onFairySpawned(Fairy fairy) {

            }

            @Override
            public void onFairyDeath(Fairy fairy) {

            }

            @Override
            public void onDragonSpawned(Dragon dragon) {

            }

            @Override
            public void onDragonDeath(Dragon dragon) {

            }

            @Override
            public void onDragonMove(Dragon dragon) {

            }

            @Override
            public void onCommandExecuted(ICommand command) {
                battleService.handleMessage(new GameCommandMessage(command));
            }

            @Override
            public void onCommandFailed(ICommand command, int errorCode) {

            }
        });

        game.executeCommand(new PlaceTileDrawnCommand(GameBoard.STARTING_TILE_POSITION));
        game.executeCommand(new SkipMeeplePlacementCommand());

        battleService.handleMessage(new GameMasterNextTurnDataMessage(game.getConfig().getTileIndex(((GameTurnPlaceTileState) game.getState()).getTileDrawn().getConfig())));

        assertEquals(2, gameView.getTurnCount());
        assertEquals(gameView.getPlayer(1), gameView.getTurnExecutor());
    }

    @Test
    void testGameResult() throws Exception {
        Client client = ClientTestUtils.createMockClient(null, 1);
        BattleService battleService = client.getBattleService();

        Map<Class<? extends ServiceBase>, ServiceBase> services = (Map<Class<? extends ServiceBase>, ServiceBase>) ReflectionUtils.getField(client, "services");

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
