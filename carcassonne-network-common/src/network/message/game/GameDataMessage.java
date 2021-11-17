package network.message.game;

import network.message.Message;
import network.message.MessageId;
import stream.ByteInputStream;
import stream.ByteOutputStream;

public class GameDataMessage extends Message {
    private byte[] data;

    public GameDataMessage() {
    }

    public GameDataMessage(byte[] data) {
        this.data = data;
    }

    @Override
    public MessageId getId() {
        return null;
    }

    @Override
    public void encode(ByteOutputStream stream) {
        stream.writeBytes(data);
    }

    @Override
    public void decode(ByteInputStream stream) {
        data = stream.readBytes();
    }

    public byte[] getData() {
        return data;
    }
}
