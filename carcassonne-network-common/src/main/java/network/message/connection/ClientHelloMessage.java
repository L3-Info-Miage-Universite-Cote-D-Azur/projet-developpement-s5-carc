package network.message.connection;

import network.message.Message;
import network.message.MessageId;
import stream.ByteInputStream;
import stream.ByteOutputStream;

public class ClientHelloMessage extends Message {
    @Override
    public MessageId getId() {
        return MessageId.CLIENT_HELLO;
    }

    @Override
    public void encode(ByteOutputStream stream) {

    }

    @Override
    public void decode(ByteInputStream stream) {

    }
}