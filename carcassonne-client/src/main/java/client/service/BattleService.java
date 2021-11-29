package client.service;

import client.Client;
import client.ai.SimpleAI;
import client.ai.heuristic.HeuristicAI;
import client.command.MasterCommandExecutionNotifier;
import client.listener.GameLogger;
import client.logger.Logger;
import client.logger.LoggerCategory;
import client.message.IMessageHandler;
import logic.Game;
import logic.command.CommandType;
import logic.config.GameConfig;
import logic.player.Player;
import logic.state.GameState;
import network.message.Message;
import network.message.game.GameCommandMessage;
import network.message.game.GameDataMessage;
import network.message.game.GameResultMessage;
import stream.ByteInputStream;

/**
 * Services that manages the game battle.
 */
public class BattleService extends ServiceBase implements IMessageHandler {
    /**
     * Current battle game instance.
     * It contains all the information about the current battle without the stack.
     * Used to know the current state of the battle and by the client to know what to do.
     * Initialized by the {@link GameDataMessage}.
     * Updated by the {@link GameCommandMessage}.
     * Disposed by the {@link GameResultMessage} or if the connection is lost.
     */
    private Game gameView;

    public BattleService(Client client) {
        super(client);
    }

    /**
     * Handles the specified message if the handler is interested in it.
     *
     * @param message The message to handle.
     */
    @Override
    public void handleMessage(Message message) {
        switch (message.getType()) {
            case GAME_DATA -> onGameData((GameDataMessage) message);
            case GAME_COMMAND -> onGameCommand((GameCommandMessage) message);
            case GAME_RESULT -> onGameResult((GameResultMessage) message);
        }
    }

    /**
     * Handles a game data message.
     *
     * @param message
     */
    private void onGameData(GameDataMessage message) {
        Logger.info(LoggerCategory.SERVICE, "Battle game data received. Waiting my turn...");

        /* Load the snapshot of the game data. */
        gameView = new Game(client.getGameConfig());
        gameView.decode(new ByteInputStream(message.getData(), message.getData().length), false);

        /* Attach a listener to the game view command executor so the server is notified that we are trying to execute a command. */
        gameView.getCommandExecutor().setListener(new MasterCommandExecutionNotifier(client));

        /* Attach a listener to the game view so we logs the game events. */
        gameView.setListener(new GameLogger(gameView));

        /* Attach your AI as listener of our player. */
        Player ownPlayer = gameView.getPlayerById(client.getAuthenticationService().getUserId());

        if (ownPlayer != null) {
            ownPlayer.setListener(new HeuristicAI(ownPlayer));
        } else {
            Logger.warn(LoggerCategory.SERVICE, "Own player not found!");
        }

        /* As we restore the game view from the server snapshot, we need to call the player listeners if the current state needs it. */
        GameState currentState = gameView.getState();

        switch (currentState.getType()) {
            case TURN_PLACE_TILE -> gameView.getTurnExecutor().getListener().onWaitingPlaceTile();
            case TURN_EXTRA_ACTION -> gameView.getTurnExecutor().getListener().onWaitingExtraAction();
        }
    }

    /**
     * Handles a game command message.
     * Command musts be executed if it's not our turn (because we already executed the command locally) or if it's a master command.
     *
     * @param message
     */
    private void onGameCommand(GameCommandMessage message) {
        if (message.getCommand().getType() == CommandType.MASTER_NEXT_TURN_DATA || gameView.getTurnExecutor().getId() != client.getAuthenticationService().getUserId()) {
            Logger.debug(LoggerCategory.SERVICE, "Server command %s received and executed.", message.getCommand().getType());
            message.getCommand().execute(gameView);
        }
    }

    /**
     * Handles a game result message.
     *
     * @param message
     */
    private void onGameResult(GameResultMessage message) {
        Logger.info(LoggerCategory.SERVICE, "Battle game result received.");

        gameView = null;

        /* Load the full game data. */
        Game masterGame = new Game(GameConfig.loadFromResources());
        masterGame.decode(new ByteInputStream(message.getData(), message.getData().length), true);

        /* Record the game result. */
        client.getGameStatisticsService().onBattleOver(masterGame);
    }

    /**
     * Called when the client is connected to the server.
     */
    @Override
    public void onConnect() {

    }

    /**
     * Called when the client is disconnected from the server.
     */
    @Override
    public void onDisconnect() {
        gameView = null;
    }
}
