package server.message;

import network.message.IMessage;
import network.message.connection.ClientHelloMessage;
import network.message.connection.ServerHelloMessage;
import network.message.game.GameCommandRequestMessage;
import network.message.matchmaking.JoinMatchmakingMessage;
import network.message.matchmaking.LeaveMatchmakingMessage;
import network.message.matchmaking.MatchmakingFailedMessage;
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
    private static int fakeUserIdCounter = 0;

    private final ClientConnection client;

    public MessageHandler(ClientConnection client) {
        this.client = client;
    }

    /**
     * Handles a message received from the server.
     *
     * @param message The message to handle.
     */
    public void handle(IMessage message) {
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
     *
     * @param ignoredMessage The client hello message.
     */
    private void onClientHello(ClientHelloMessage ignoredMessage) {
        if (client.getSession() != null) {
            Logger.warn("Client already has a session.");
            return;
        }

        int userId;

        synchronized (MessageHandler.class) {
            userId = ++fakeUserIdCounter;
        }

        client.setSession(new ClientSession(client, userId));
        client.send(new ServerHelloMessage(userId));
    }

    /**
     * Handles a join matchmaking message.
     *
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

        Matchmaking matchmaking = Server.getInstance().getMatchmaking(message.getMatchCapacity());

        if (matchmaking == null) {
            Logger.warn("No matchmaking found for capacity: " + message.getMatchCapacity());
            client.send(new MatchmakingFailedMessage());
            return;
        }

        matchmaking.add(session);
    }

    /**
     * Handles a leave matchmaking message.
     *
     * @param ignoredMessage The leave matchmaking message.
     */
    private void onLeaveMatchmaking(LeaveMatchmakingMessage ignoredMessage) {
        ClientSession session = client.getSession();

        if (session == null) {
            Logger.warn("Client has no session.");
            return;
        }

        Matchmaking matchmaking = session.getMatchmaking();

        if (matchmaking == null) {
            Logger.warn("Client has no matchmaking.");
            return;
        }

        matchmaking.remove(session);
    }

    /**
     * Handles a game command request message.
     *
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

        synchronized (match) {
            match.executeCommand(session.getUserId(), message.getCommand());
        }
    }
}
