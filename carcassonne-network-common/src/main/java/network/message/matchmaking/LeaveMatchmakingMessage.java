package network.message.matchmaking;

import network.message.Message;
import network.message.MessageId;
import stream.ByteInputStream;
import stream.ByteOutputStream;

public class LeaveMatchmakingMessage extends Message {
    @Override
    public MessageId getId() {
        return MessageId.LEAVE_MATCHMAKING;
    }

    @Override
    public void encode(ByteOutputStream stream) {
    }

    @Override
    public void decode(ByteInputStream stream) {
    }
}
