package network.message.connection;

import network.message.Message;
import network.message.MessageType;
import stream.ByteInputStream;
import stream.ByteOutputStream;

/**
 * Message sent by the server to the client to inform that the connection is ready to be used.
 */
public class ServerHelloMessage extends Message {
    private int userId;

    public ServerHelloMessage() {
    }

    public ServerHelloMessage(int userId) {
        this.userId = userId;
    }

    /**
     * Gets the message type of this message.
     * @return The message type.
     */
    @Override
    public MessageType getType() {
        return MessageType.SERVER_HELLO;
    }

    /**
     * Encodes the message attributes to the output stream.
     * @param stream the output stream
     */
    @Override
    public void encode(ByteOutputStream stream) {
        stream.writeInt(userId);
    }

    /**
     * Decodes the message attributes from the input stream.
     * @param stream the input stream
     */
    @Override
    public void decode(ByteInputStream stream) {
        userId = stream.readInt();
    }

    /**
     * Gets the user id of the user that is connected to the server.
     * @return The user id.
     */
    public int getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "SERVER_HELLO {}";
    }
}
