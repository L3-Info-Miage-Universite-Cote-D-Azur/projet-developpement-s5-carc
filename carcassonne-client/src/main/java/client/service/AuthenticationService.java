package client.service;

import client.Client;
import client.logger.Logger;
import client.logger.LoggerCategory;
import client.message.IMessageHandler;
import network.message.Message;
import network.message.connection.ClientHelloMessage;
import network.message.connection.ServerHelloMessage;

/**
 * Service that manages the authentication process with the server.
 */
public class AuthenticationService extends ServiceBase implements IMessageHandler {
    private boolean authenticated;
    private int userId;

    public AuthenticationService(Client client) {
        super(client);
    }

    /**
     * Handles the specified message if the handler is interested in it.
     * @param message The message to handle.
     */
    @Override
    public void handleMessage(Message message) {
        switch (message.getType()) {
            case SERVER_HELLO -> onServerHello((ServerHelloMessage) message);
        }
    }

    /**
     * Handles the server hello message from the server.
     * @param message The server hello message.
     */
    private void onServerHello(ServerHelloMessage message) {
        if (authenticated) {
            throw new IllegalStateException("Already authenticated");
        }

        authenticated = true;
        userId = message.getUserId();

        Logger.debug(LoggerCategory.SERVICE, "Client logged. UID:%d", userId);
    }

    /**
     * Authenticates the client with the server.
     */
    public void authenticate() {
        this.client.getServerConnection().send(new ClientHelloMessage());
    }

    /**
     * Called when the client is connected to the server.
     */
    @Override
    public void onConnect() {
        authenticate();
    }

    /**
     * Called when the client is disconnected from the server.
     * Resets the authentication data.
     */
    @Override
    public void onDisconnect() {
        authenticated = false;
        userId = 0;
    }

    /**
     * Gets whether the client is authenticated.
     * @return Whether the client is authenticated.
     */
    public boolean isAuthenticated() {
        return authenticated;
    }

    /**
     * Gets the user id.
     * @return The user id.
     */
    public int getUserId() {
        return userId;
    }
}
