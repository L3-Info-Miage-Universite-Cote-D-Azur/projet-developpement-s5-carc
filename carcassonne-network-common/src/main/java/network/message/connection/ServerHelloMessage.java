package network.message.connection;

import network.message.Message;
import network.message.MessageId;
import stream.ByteInputStream;
import stream.ByteOutputStream;

public class ServerHelloMessage extends Message {
    @Override
    public MessageId getId() {
        return MessageId.SERVER_HELLO;
    }

    @Override
    public void encode(ByteOutputStream stream) {
    }

    @Override
    public void decode(ByteInputStream stream) {
    }
}
