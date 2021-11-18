package network.message.matchmaking;

import network.message.Message;
import network.message.MessageType;
import stream.ByteInputStream;
import stream.ByteOutputStream;

/**
 * Message sent by the client to join the matchmaking queue.
 */
public class JoinMatchmakingMessage extends Message {
    /**
     * Returns the message type.
     * @return the message type
     */
    @Override
    public MessageType getType() {
        return MessageType.JOIN_MATCHMAKING;
    }

    /**
     * Encodes the message attributes to the output stream.
     * @param stream the output stream
     */
    @Override
    public void encode(ByteOutputStream stream) {

    }

    /**
     * Decodes the message attributes from the input stream.
     * @param stream the input stream
     */
    @Override
    public void decode(ByteInputStream stream) {

    }
}
