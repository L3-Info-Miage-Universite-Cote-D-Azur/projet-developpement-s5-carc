package network;

import network.message.Message;
import network.message.MessageFactory;
import network.message.MessageType;
import network.util.Crc32;
import stream.ByteInputStream;
import stream.ByteOutputStream;

/**
 * Packet class. Contains the message header (type, length crc) and the encoded message data.
 */
public class Packet {
    /**
     * The packet header length.
     * If the incoming packet is smaller than this, we assume that all the data is not yet received.
     */
    public static int HEADER_SIZE = 20;
    /**
     * Magic number used to verify the packet transmission.
     */
    private static int HEADER_MAGIC = 0xA0B0C0D0;
    /**
     * Magic number used to verify the packet transmission.
     */
    private static int TRAILER_MAGIC = 0xF0C0B0A0;
    /**
     * The message type.
     */
    private MessageType type;

    /**
     * The crc32 checksum of the message data.
     * We use this to verify the integrity of the message.
     */
    private int checksum;

    /**
     * The message length.
     */
    private int messageLength;

    /**
     * The message data.
     */
    private byte[] messageData;

    public Packet() {
    }

    public Packet(MessageType type, int checksum, int messageLength, byte[] messageData) {
        this.type = type;
        this.checksum = checksum;
        this.messageLength = messageLength;
        this.messageData = messageData;
    }

    /**
     * Creates a packet from the given message.
     *
     * @param message the message to create the packet from
     * @return the packet
     */
    public static Packet create(Message message) {
        ByteOutputStream stream = new ByteOutputStream(20);
        message.encode(stream);
        return new Packet(message.getType(), Crc32.getCrc(stream.getBytes(), 0, stream.getLength()), stream.getLength(), stream.getBytes());
    }

    /**
     * Decodes the packet from the given stream
     *
     * @param stream the stream to read from
     * @return the number of bytes read.
     * >= 0 if the packet was successfully decoded.
     * -1 if not enough data was available.
     * -2 if the packet was invalid.
     * -3 if the checksum did not match.
     */
    public int decode(ByteInputStream stream) {
        if (stream.getBytesLeft() < HEADER_SIZE) {
            return -1;
        }

        if (stream.readInt() != HEADER_MAGIC) {
            return -2;
        }

        type = MessageType.getByType(stream.readInt());
        checksum = stream.readInt();
        messageLength = stream.readInt();

        if (messageLength < 0 || messageLength > 65535) {
            return -2;
        }

        if (stream.getBytesLeft() < (HEADER_SIZE - 16) + messageLength) {
            return -1;
        }

        messageData = stream.readBytes(messageLength);

        if (stream.readInt() != TRAILER_MAGIC) {
            return -2;
        }

        if (Crc32.getCrc(messageData, 0, messageLength) != checksum) {
            return -3;
        }

        return HEADER_SIZE + messageLength;
    }

    /**
     * Encodes the packet to the given stream
     *
     * @param stream the stream to write to
     * @return the number of bytes written.
     */
    public void encode(ByteOutputStream stream) {
        stream.writeInt(HEADER_MAGIC);
        stream.writeInt(type.getValue());
        stream.writeInt(checksum);
        stream.writeInt(messageLength);
        stream.writeBytesWithoutLength(messageData, 0, messageLength);
        stream.writeInt(TRAILER_MAGIC);
    }

    /**
     * Returns the message type.
     *
     * @return the message type
     */
    public MessageType getMessageType() {
        return type;
    }

    /**
     * Creates and returns the message from the packet.
     *
     * @return the message
     */
    public Message getMessage() {
        Message message = MessageFactory.create(type);
        message.decode(new ByteInputStream(messageData, messageLength));
        return message;
    }

    /**
     * Gets the checksum of the message data.
     *
     * @return the checksum
     */
    public int getChecksum() {
        return checksum;
    }

    /**
     * Gets the message length.
     *
     * @return the message length
     */
    public int getMessageLength() {
        return messageLength;
    }

    /**
     * Gets the message data.
     *
     * @return the message data
     */
    public byte[] getMessageData() {
        return messageData;
    }
}
