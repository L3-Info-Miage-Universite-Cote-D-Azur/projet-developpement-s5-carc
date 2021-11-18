package server.message;

import network.message.Message;
import network.message.connection.ClientHelloMessage;
import network.message.connection.ServerHelloMessage;
import network.message.game.GameCommandRequestMessage;
import network.message.matchmaking.JoinMatchmakingMessage;
import network.message.matchmaking.LeaveMatchmakingMessage;
import server.Server;
import server.logger.Logger;
import server.matchmaking.Match;
import server.matchmaking.Matchmaking;
import server.network.ClientConnection;
import server.session.ClientSession;

/**
 * Handles messages received from the server.
 */
public class MessageHandler {
    private static int FAKE_USER_ID_COUNTER = 0;

    private final ClientConnection client;

    public MessageHandler(ClientConnection client) {
        this.client = client;
    }

    /**
     * Handles a message received from the server.
     * @param message The message to handle.
     */
    public void handle(Message message) {
        switch (message.getType()) {
            case CLIENT_HELLO -> onClientHello((ClientHelloMessage) message);
            case JOIN_MATCHMAKING -> onJoinMatchmaking((JoinMatchmakingMessage) message);
            case LEAVE_MATCHMAKING -> onLeaveMatchmaking((LeaveMatchmakingMessage) message);
            case GAME_COMMAND_REQUEST -> onGameCommandRequest((GameCommandRequestMessage) message);
            default -> Logger.warn("Received unknown message type: " + message.getType());
        }
    }

    /**
     * Handles a client hello message.
     * @param message The client hello message.
     */
    private void onClientHello(ClientHelloMessage message) {
        if (client.getSession() != null) {
            Logger.warn("Client already has a session.");
            return;
        }

        int userId = ++FAKE_USER_ID_COUNTER;
        client.setSession(new ClientSession(client, userId));
        client.send(new ServerHelloMessage(userId));
    }

    /**
     * Handles a join matchmaking message.
     * @param message The join matchmaking message.
     */
    private void onJoinMatchmaking(JoinMatchmakingMessage message) {
        ClientSession session = client.getSession();

        if (session == null) {
            Logger.warn("Client has no session.");
            return;
        }

        if (session.getMatch() != null) {
            Logger.warn("Client has a match.");
            return;
        }

        if (session.getMatchmaking() != null) {
            Logger.warn("Client already has a matchmaking.");
            return;
        }

        Server.getInstance().getMatchmaking().add(session);
    }

    /**
     * Handles a leave matchmaking message.
     * @param message The leave matchmaking message.
     */
    private void onLeaveMatchmaking(LeaveMatchmakingMessage message) {
        ClientSession session = client.getSession();

        if (session == null) {
            Logger.warn("Client has no session.");
            return;
        }

        if (session.getMatchmaking() == null) {
            Logger.warn("Client has no matchmaking.");
            return;
        }

        Server.getInstance().getMatchmaking().remove(session);
    }

    /**
     * Handles a game command request message.
     * @param message The game command request message.
     */
    private void onGameCommandRequest(GameCommandRequestMessage message) {
        ClientSession session = client.getSession();

        if (session == null) {
            Logger.warn("Client has no session.");
            return;
        }

        Match match = session.getMatch();

        if (match == null) {
            Logger.warn("Client has no match.");
            return;
        }

        match.executeCommand(session.getUserId(), message.getCommand());
    }
}
