package server.message;

import network.message.Message;
import network.message.connection.ClientHelloMessage;
import network.message.game.GameCommandRequestMessage;
import network.message.matchmaking.JoinMatchmakingMessage;
import network.message.matchmaking.LeaveMatchmakingMessage;
import server.socket.TcpClientConnection;

public class MessageHandler {
    private final TcpClientConnection client;

    public MessageHandler(TcpClientConnection client) {
        this.client = client;
    }

    public void handle(Message message) {
        switch (message.getId()) {
            case CLIENT_HELLO -> onClientHello((ClientHelloMessage) message);
            case JOIN_MATCHMAKING -> onJoinMatchmaking((JoinMatchmakingMessage) message);
            case LEAVE_MATCHMAKING -> onLeaveMatchmaking((LeaveMatchmakingMessage) message);
            case GAME_COMMAND_REQUEST -> onGameCommandRequest((GameCommandRequestMessage) message);
        }
    }

    private void onClientHello(ClientHelloMessage message) {
        System.out.println("Client connected: " + client.getRemoteAddress());
    }

    private void onJoinMatchmaking(JoinMatchmakingMessage message) {
        System.out.println("Client joined matchmaking: " + client.getRemoteAddress());
    }

    private void onLeaveMatchmaking(LeaveMatchmakingMessage message) {
        System.out.println("Client left matchmaking: " + client.getRemoteAddress());
    }

    private void onGameCommandRequest(GameCommandRequestMessage message) {
        System.out.println("Client sent game command: " + message.getId() + " " + client.getRemoteAddress());
    }
}
