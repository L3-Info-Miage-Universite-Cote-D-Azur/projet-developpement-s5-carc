package client.message;

import client.ServerSideGameMain;
import client.ai.SimpleAI;
import client.command.MasterCommandExecutionNotifier;
import client.listener.GameLogger;
import client.logger.Logger;
import client.network.ServerConnection;
import logic.Game;
import logic.command.CommandType;
import logic.config.GameConfig;
import logic.player.Player;
import network.message.Message;
import network.message.connection.ServerHelloMessage;
import network.message.game.GameCommandMessage;
import network.message.game.GameDataMessage;
import network.message.game.GameResultMessage;
import network.message.matchmaking.JoinMatchmakingMessage;
import network.message.matchmaking.MatchmakingDataMessage;
import stream.ByteInputStream;

/**
 * Handles messages received from the server.
 */
public class MessageHandler {
    private final ServerConnection connection;

    private int userId;
    private Game currentMatchGame;

    public MessageHandler(ServerConnection connection) {
        this.connection = connection;
    }

    /**
     * Handles a message received from the server.
     * @param message The message to handle.
     */
    public void handle(Message message) {
        switch (message.getType()) {
            case SERVER_HELLO -> onServerHello((ServerHelloMessage) message);
            case MATCHMAKING_DATA -> onMatchmakingData((MatchmakingDataMessage) message);
            case GAME_DATA -> onGameData((GameDataMessage) message);
            case GAME_COMMAND -> onGameCommand((GameCommandMessage) message);
            case GAME_RESULT -> onGameResult((GameResultMessage) message);
            default -> Logger.warn("Unknown message received: %s", message.getType());
        }
    }

    /**
     * Handles a server hello message.
     * @param message The message to handle.
     */
    private void onServerHello(ServerHelloMessage message) {
        Logger.info("Network: ServerHelloMessage received. UID: %d", message.getUserId());

        userId = message.getUserId();
        connection.send(new JoinMatchmakingMessage());
    }

    /**
     * Handles a matchmaking data message.
     * @param message The message to handle.
     */
    private void onMatchmakingData(MatchmakingDataMessage message) {
        Logger.info("Matchmaking: %d/%d", message.getNumPlayers(), message.getRequiredPlayers());
    }

    /**
     * Handles a game data message.
     * @param message
     */
    private void onGameData(GameDataMessage message) {
        Logger.info("Game: data from server received!", message.getData());

        currentMatchGame = new Game(GameConfig.loadFromResources());
        currentMatchGame.decode(new ByteInputStream(message.getData(), message.getData().length), false);
        currentMatchGame.setListener(new GameLogger());
        currentMatchGame.getCommandExecutor().setListener(new MasterCommandExecutionNotifier(connection));

        Player ownPlayer = currentMatchGame.getPlayerById(userId);

        if (ownPlayer != null) {
            ownPlayer.setListener(new SimpleAI(ownPlayer));
        } else {
            Logger.warn("Game: own player not found!");
        }
    }

    /**
     * Handles a game command message.
     * Command musts be executed if it's not our turn or if it's a master command.
     * @param message
     */
    private void onGameCommand(GameCommandMessage message) {
        if (currentMatchGame == null) {
            return;
        }

        if (message.getCommand().getType() == CommandType.MASTER_TURN_DATA || currentMatchGame.getTurn().getPlayer().getId() != userId) {
            message.getCommand().execute(currentMatchGame);
        }
    }

    /**
     * Handles a game result message.
     * @param message
     */
    private void onGameResult(GameResultMessage message) {
        Logger.info("Game: result received from server!");
        currentMatchGame = null;

        Game masterGame = new Game(GameConfig.loadFromResources());
        masterGame.decode(new ByteInputStream(message.getData(), message.getData().length), true);

        ServerSideGameMain.onMatchOver(userId, masterGame);

    }

    public int getUserId() {
        return userId;
    }

    public boolean isInMatch() {
        return currentMatchGame != null;
    }
}
