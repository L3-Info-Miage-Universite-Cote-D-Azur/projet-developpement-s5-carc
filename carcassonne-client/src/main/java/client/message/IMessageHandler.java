package client.message;

import network.message.Message;

/**
 * Interface for message handlers.
 */
public interface IMessageHandler {
    /**
     * Handles the specified message if the handler is interested in it.
     * @param message The message to handle.
     */
    void handleMessage(Message message);
}
