package network.message.matchmaking;

import network.message.Message;
import network.message.MessageId;
import stream.ByteInputStream;
import stream.ByteOutputStream;

public class MatchmakingDataMessage extends Message {
    private int currentPlayerCount;
    private int maxPlayerCount;

    public MatchmakingDataMessage() {

    }

    public MatchmakingDataMessage(int currentPlayerCount, int maxPlayerCount) {
        this.currentPlayerCount = currentPlayerCount;
        this.maxPlayerCount = maxPlayerCount;
    }


    @Override
    public MessageId getId() {
        return MessageId.MATCHMAKING_DATA;
    }

    @Override
    public void encode(ByteOutputStream stream) {
        stream.writeInt(currentPlayerCount);
        stream.writeInt(maxPlayerCount);
    }

    @Override
    public void decode(ByteInputStream stream) {
        currentPlayerCount = stream.readInt();
        maxPlayerCount = stream.readInt();
    }
}
