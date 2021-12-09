package network.message;

import stream.ByteInputStream;
import stream.ByteOutputStream;

/**
 * Abstract class for all messages used for communication between client and server.
 */
public interface IMessage {
    /**
     * Returns the message type.
     *
     * @return the message type
     */
    MessageType getType();

    /**
     * Encodes the message attributes to the output stream.
     *
     * @param stream the output stream
     */
    void encode(ByteOutputStream stream);

    /**
     * Decodes the message attributes from the input stream.
     *
     * @param stream the input stream
     */
    void decode(ByteInputStream stream);
}
