package network.message;

import stream.ByteInputStream;
import stream.ByteOutputStream;

/**
 * Abstract class for all messages used for communication between client and server.
 */
public abstract class Message {
    /**
     * Returns the message type.
     * @return the message type
     */
    public abstract MessageType getType();

    /**
     * Encodes the message attributes to the output stream.
     * @param stream the output stream
     */
    public abstract void encode(ByteOutputStream stream);

    /**
     * Decodes the message attributes from the input stream.
     * @param stream the input stream
     */
    public abstract void decode(ByteInputStream stream);
}
