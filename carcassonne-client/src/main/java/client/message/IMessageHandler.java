package client.message;

import network.message.IMessage;

/**
 * Interface for message handlers.
 */
public interface IMessageHandler {
    /**
     * Handles the specified message if the handler is interested in it.
     *
     * @param message The message to handle.
     */
    void handleMessage(IMessage message);
}
