package client.message;

import client.SlaveGameMain;
import client.ai.SimpleAI;
import client.command.MasterCommandExecutionNotifier;
import client.config.ServerConfig;
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
import network.message.matchmaking.MatchmakingFailedMessage;
import stream.ByteInputStream;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles messages received from the server.
 */
public class MessageHandler {
    private final ServerConnection connection;

    private int userId;
    private Game currentMatchGame;
    private ArrayList<Game> matchHistory = new ArrayList<>();

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
            case MATCHMAKING_FAILED -> onMatchmakingFailed((MatchmakingFailedMessage) message);
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
        Logger.debug("Network: ServerHelloMessage received. UID: %d", message.getUserId());

        userId = message.getUserId();
        connection.send(new JoinMatchmakingMessage(ServerConfig.MATCHMAKING_MATCH_CAPACITY));
    }

    /**
     * Handles a matchmaking data message.
     * @param message The message to handle.
     */
    private void onMatchmakingData(MatchmakingDataMessage message) {
        Logger.debug("Matchmaking: %d/%d", message.getNumPlayers(), message.getRequiredPlayers());
    }

    private void onMatchmakingFailed(MatchmakingFailedMessage message) {
        Logger.warn("Matchmaking: failed!");
    }

    /**
     * Handles a game data message.
     * @param message
     */
    private void onGameData(GameDataMessage message) {
        Logger.debug("Game: data from server received!", message.getData());

        currentMatchGame = new Game(ServerConfig.GAME_CONFIG);
        currentMatchGame.decode(new ByteInputStream(message.getData(), message.getData().length), false);

        if (userId == 0) {
            currentMatchGame.setListener(new GameLogger());
        }

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
            Logger.debug("Game: execute command %s", message.getCommand().getType());
            message.getCommand().execute(currentMatchGame);
        }
    }

    /**
     * Handles a game result message.
     * @param message
     */
    private void onGameResult(GameResultMessage message) {
        Logger.debug("Game: result received from server!");
        currentMatchGame = null;

        Game masterGame = new Game(ServerConfig.GAME_CONFIG);
        masterGame.decode(new ByteInputStream(message.getData(), message.getData().length), true);
        matchHistory.add(masterGame);

        if (matchHistory.size() == ServerConfig.NUM_MATCHES) {
            Logger.info("Game: finished %d matches!", matchHistory.size());
            SlaveGameMain.onAllMatchOver();
            connection.close();
        } else {
            Logger.info("Game: match ended. Rejoin the matchmaking...");
            connection.send(new JoinMatchmakingMessage(ServerConfig.MATCHMAKING_MATCH_CAPACITY));
        }
    }

    /**
     * Gets the client's user id.
     * @return
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Gets whether the client is in match.
     * @return
     */
    public boolean isInMatch() {
        return currentMatchGame != null;
    }

    /**
     * Gets the match history.
     * @return
     */
    public List<Game> getMatchHistory() {
        return matchHistory;
    }
}
