package network;

import network.message.Message;
import network.message.MessageFactory;
import network.message.MessageId;
import network.util.Crc32;
import stream.ByteInputStream;
import stream.ByteOutputStream;

public class Packet {
    public static int HEADER_SIZE = 12;
    public static int MAX_PACKET_SIZE = 65535;

    private MessageId id;
    private int checksum;
    private int messageLength;
    private byte[] messageData;

    public Packet() {
    }

    public Packet(MessageId id, int checksum, int messageLength, byte[] messageData) {
        this.id = id;
        this.checksum = checksum;
        this.messageLength = messageLength;
        this.messageData = messageData;
    }

    public int decode(ByteInputStream buffer) {
        if (buffer.getBytesLeft() < HEADER_SIZE) {
            return -1;
        }

        id = MessageId.getById(buffer.readInt());
        checksum = buffer.readInt();
        messageLength = buffer.readInt();

        if (messageLength > MAX_PACKET_SIZE) {
            return -2;
        }

        messageData = buffer.readBytes(messageLength);

        if (Crc32.getCrc(messageData) != checksum) {
            return -3;
        }

        return HEADER_SIZE + messageLength;
    }

    public void encode(ByteOutputStream buffer) {
        buffer.writeInt(id.getId());
        buffer.writeInt(checksum);
        buffer.writeInt(messageLength);
        buffer.writeBytes(messageData);
    }

    public Message getMessage() {
        Message message = MessageFactory.create(id);
        message.decode(new ByteInputStream(messageData, messageLength));
        return message;
    }

    public static Packet create(Message message) {
        ByteOutputStream stream = new ByteOutputStream(20);
        message.encode(stream);
        return new Packet(message.getId(), 0, stream.getLength(), stream.getBytes());
    }
}
