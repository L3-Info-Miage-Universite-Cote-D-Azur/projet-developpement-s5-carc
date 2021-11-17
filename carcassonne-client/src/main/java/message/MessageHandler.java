package message;

import logger.Logger;
import network.ServerConnection;
import network.message.Message;
import network.message.connection.ServerHelloMessage;

public class MessageHandler {
    private final ServerConnection connection;

    public MessageHandler(ServerConnection connection) {
        this.connection = connection;
    }

    public void handle(Message message) {
        switch (message.getId()) {
            case SERVER_HELLO -> onServerHello((ServerHelloMessage) message);
            default -> Logger.warn("Unknown message received: %s", message.getId());
        }
    }

    private void onServerHello(ServerHelloMessage message) {
        System.out.println("ServerHelloMessage received");
    }
}
