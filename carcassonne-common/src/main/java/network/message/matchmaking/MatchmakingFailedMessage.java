package network.message.matchmaking;

import network.message.Message;
import network.message.MessageType;
import stream.ByteInputStream;
import stream.ByteOutputStream;

/**
 * Message sent when a matchmaking request failed.
 */
public class MatchmakingFailedMessage extends Message {
    /**
     * Gets the message type of this message.
     *
     * @return The message type.
     */
    @Override
    public MessageType getType() {
        return MessageType.MATCHMAKING_FAILED;
    }

    /**
     * Encodes the message into a byte stream.
     *
     * @param stream the output stream
     */
    @Override
    public void encode(ByteOutputStream stream) {
        // TODO
    }

    /**
     * Decodes the message from a byte stream.
     *
     * @param stream the input stream
     */
    @Override
    public void decode(ByteInputStream stream) {
        //TODO
    }
}
