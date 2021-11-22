package client.message;

import client.ai.SimpleAI;
import client.command.MasterCommandExecutionNotifier;
import client.listener.GameLogger;
import client.logger.Logger;
import client.network.ServerConnection;
import logic.Game;
import logic.command.CommandType;
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
public class MessageDispatcher {
    private final ServerConnection connection;
    private final ArrayList<IMessageHandler> handlers;

    public MessageDispatcher(ServerConnection connection) {
        this.connection = connection;
        this.handlers = new ArrayList<>();
    }

    /**
     * Adds a message handler to the list of handlers.
     */
    public void addHandler(IMessageHandler handler) {
        handlers.add(handler);
    }

    /**
     * Handles a message received from the server.
     * @param message The message to handle.
     */
    public void handle(Message message) {
        for (IMessageHandler handler : handlers) {
            handler.handleMessage(message);
        }
    }
}
