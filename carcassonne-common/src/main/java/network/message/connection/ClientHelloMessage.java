package network.message.connection;

import network.message.Message;
import network.message.MessageType;
import stream.ByteInputStream;
import stream.ByteOutputStream;

/**
 * Message sent by the client to the server to indicate that it is ready to.
 */
public class ClientHelloMessage extends Message {
    /**
     * Returns the message type.
     *
     * @return the message type
     */
    @Override
    public MessageType getType() {
        return MessageType.CLIENT_HELLO;
    }

    /**
     * Encodes the message attributes to the output stream.
     *
     * @param stream the output stream
     */
    @Override
    public void encode(ByteOutputStream stream) {

    }

    /**
     * Decodes the message attributes from the input stream.
     *
     * @param stream the input stream
     */
    @Override
    public void decode(ByteInputStream stream) {

    }

    @Override
    public String toString() {
        return "CLIENT_HELLO {}";
    }
}
