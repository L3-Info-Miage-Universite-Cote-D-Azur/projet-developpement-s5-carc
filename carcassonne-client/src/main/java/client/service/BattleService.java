package client.service;

import client.Client;
import client.ai.HeuristicAI;
import client.listener.ClientGameListener;
import client.logger.Logger;
import client.logger.LoggerCategory;
import client.message.IMessageHandler;
import logic.Game;
import logic.config.GameConfig;
import logic.player.Player;
import logic.state.GameState;
import logic.tile.Tile;
import network.message.IMessage;
import network.message.game.GameCommandMessage;
import network.message.game.GameDataMessage;
import network.message.game.GameMasterNextTurnDataMessage;
import network.message.game.GameResultMessage;
import stream.ByteInputStream;

import java.util.ArrayList;

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
    public void handleMessage(IMessage message) {
        switch (message.getType()) {
            case GAME_DATA -> onGameData((GameDataMessage) message);
            case GAME_COMMAND -> onGameCommand((GameCommandMessage) message);
            case GAME_MASTER_NEXT_TURN_DATA -> onGameMasterNextTurnData((GameMasterNextTurnDataMessage) message);
            case GAME_RESULT -> onGameResult((GameResultMessage) message);
            case CLIENT_HELLO, SERVER_HELLO, JOIN_MATCHMAKING, MATCHMAKING_DATA, LEAVE_MATCHMAKING, MATCHMAKING_LEFT, MATCHMAKING_FAILED, GAME_COMMAND_REQUEST -> {
                // ignored
            }
            default -> {
                // Do nothing
            }
        }
    }

    /**
     * Handles a game data message.
     *
     * @param message The message to handle.
     */
    private void onGameData(GameDataMessage message) {
        Logger.info(LoggerCategory.SERVICE, "Battle game data received. Waiting my turn...");

        /* Load the snapshot of the game data. */
        gameView = new Game(client.getGameConfig());
        gameView.decode(new ByteInputStream(message.getData(), message.getData().length), false);

        /* Attach a listener to the game view */
        gameView.setListener(new ClientGameListener(client, gameView));

        /* Attach your AI as listener of our player. */
        Player ownPlayer = gameView.getPlayerById(client.getAuthenticationService().getUserId());

        if (ownPlayer != null) {
            ownPlayer.setListener(new HeuristicAI(ownPlayer));
        } else {
            Logger.warn(LoggerCategory.SERVICE, "Own player not found!");
        }

        /* As we restore the game view from the server snapshot, we need to call the player listeners if the current state needs it. */
        GameState currentState = gameView.getState();

        if (currentState != null) {
            switch (currentState.getType()) {
                case TURN_PLACE_TILE -> gameView.getTurnExecutor().getListener().onWaitingPlaceTile();
                case TURN_PLACE_MEEPLE -> gameView.getTurnExecutor().getListener().onWaitingMeeplePlacement();
                case TURN_MOVE_DRAGON -> gameView.getTurnExecutor().getListener().onWaitingDragonMove();
                case START, TURN_INIT, TURN_WAITING_MASTER_DATA, TURN_ENDING, OVER -> {
                    // ignored
                }
                default -> {
                    // Do nothing
                }
            }
        }
    }

    /**
     * Handles a game command message.
     * Command musts be executed if it's not our turn (because we already executed the command locally) or if it's a master command.
     *
     * @param message The message to handle.
     */
    private void onGameCommand(GameCommandMessage message) {
        if (gameView.getTurnExecutor().getId() != client.getAuthenticationService().getUserId()) {
            Logger.debug(LoggerCategory.SERVICE, "Server command %s received and executed.", message.getCommand().getType());
            message.getCommand().execute(gameView);
        }
    }

    /**
     * Handles a game master next turn data message.
     *
     * @param message The message to handle.
     */
    private void onGameMasterNextTurnData(GameMasterNextTurnDataMessage message) {
        ArrayList<Tile> tiles = new ArrayList<>();
        tiles.add(gameView.getConfig().getTile(message.getTileConfigIndex()).createTile(gameView));
        gameView.getStack().fill(tiles);
        gameView.getState().complete();
    }

    /**
     * Handles a game result message.
     *
     * @param message The message to handle.
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
        // ignored
    }

    /**
     * Called when the client is disconnected from the server.
     */
    @Override
    public void onDisconnect() {
        gameView = null;
    }

    /**
     * Gets the game view.
     *
     * @return The game view.
     */
    public Game getGameView() {
        return gameView;
    }
}
