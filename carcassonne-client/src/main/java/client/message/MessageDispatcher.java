package client.message;

import network.message.IMessage;

import java.util.ArrayList;

/**
 * Handles messages received from the server.
 */
public class MessageDispatcher {
    private final ArrayList<IMessageHandler> handlers;

    public MessageDispatcher() {
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
     *
     * @param message The message to handle.
     */
    public void handle(IMessage message) {
        for (IMessageHandler handler : handlers) {
            handler.handleMessage(message);
        }
    }
}
