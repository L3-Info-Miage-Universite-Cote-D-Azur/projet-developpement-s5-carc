package network.message.matchmaking;

import network.message.Message;
import network.message.MessageType;
import stream.ByteInputStream;
import stream.ByteOutputStream;

/**
 * Message sent by the client to leave the matchmaking queue.
 */
public class LeaveMatchmakingMessage extends Message {
    /**
     * Returns the message type.
     *
     * @return the message type
     */
    @Override
    public MessageType getType() {
        return MessageType.LEAVE_MATCHMAKING;
    }

    /**
     * Encodes the message attributes to the output stream.
     *
     * @param stream the output stream
     */
    @Override
    public void encode(ByteOutputStream stream) {
        // TODO
    }

    /**
     * Decodes the message attributes from the input stream.
     *
     * @param stream the input stream
     */
    @Override
    public void decode(ByteInputStream stream) {
        // TODO
    }

    @Override
    public String toString() {
        return "LEAVE_MATCHMAKING {}";
    }
}
