package network.message.matchmaking;

import network.message.IMessage;
import network.message.MessageType;
import stream.ByteInputStream;
import stream.ByteOutputStream;

/**
 * Message sent by the server to the client when the client leaves the matchmaking queue.
 */
public class MatchmakingLeftMessage implements IMessage {
    /**
     * Returns the message type.
     *
     * @return the message type
     */
    @Override
    public MessageType getType() {
        return MessageType.MATCHMAKING_LEFT;
    }

    /**
     * Encodes the message attributes to the output stream.
     *
     * @param stream the output stream
     */
    @Override
    public void encode(ByteOutputStream stream) {
        // Nothing to encode.
    }

    /**
     * Decodes the message attributes from the input stream.
     *
     * @param stream the input stream
     */
    @Override
    public void decode(ByteInputStream stream) {
        // Nothing to decode.
    }

    @Override
    public String toString() {
        return "MATCHMAKING_LEFT {}";
    }
}
