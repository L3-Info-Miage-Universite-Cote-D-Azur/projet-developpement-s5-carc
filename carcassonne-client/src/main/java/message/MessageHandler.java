package message;

import ai.SimpleAI;
import logger.Logger;
import logic.Game;
import logic.command.CommandType;
import logic.command.ICommand;
import logic.command.ICommandExecutorListener;
import logic.config.GameConfig;
import logic.player.Player;
import network.ServerConnection;
import network.message.Message;
import network.message.connection.ServerHelloMessage;
import network.message.game.GameCommandMessage;
import network.message.game.GameCommandRequestMessage;
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
        currentMatchGame.getCommandExecutor().setListener(new ICommandExecutorListener() {
            @Override
            public void onCommandExecuted(ICommand command) {
                connection.send(new GameCommandRequestMessage(command));
            }

            @Override
            public void onCommandFailed(ICommand command, String reason) {
                Logger.warn("Game: command failed: %s", reason);
            }

            @Override
            public void onCommandFailed(ICommand command, String reason, Object... args) {
                Logger.warn("Game: command failed: %s", reason);
            }
        });

        for (int i = 0; i < currentMatchGame.getPlayerCount(); i++) {
            Player player = currentMatchGame.getPlayer(i);

            if (player.getId() == userId) {
                player.setListener(new SimpleAI(player));
                return;
            }
        }

        Logger.warn("Game: own user not found in game!");
    }

    private void onGameCommand(GameCommandMessage message) {
        if (message.getCommand().getType() == CommandType.MASTER_TURN_DATA || currentMatchGame.getTurn().getPlayer().getId() != userId) {
            // Logger.info("Game: command %s received from server!", message.getCommand().getType());
            message.getCommand().execute(currentMatchGame);
        } else {
            // Logger.info("Game: command %s received from server! (callback)", message.getCommand().getType());
        }
    }

    private void onGameResult(GameResultMessage message) {
        Logger.info("Game: result received from server!");
    }
}
