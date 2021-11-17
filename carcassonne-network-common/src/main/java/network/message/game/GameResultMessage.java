package network.message.game;

import network.message.Message;
import network.message.MessageId;
import stream.ByteInputStream;
import stream.ByteOutputStream;

public class GameResultMessage extends Message {
    @Override
    public MessageId getId() {
        return MessageId.GAME_RESULT;
    }

    @Override
    public void encode(ByteOutputStream stream) {

    }

    @Override
    public void decode(ByteInputStream stream) {

    }
}
