package client.network;

import client.logger.Logger;
import client.logger.LoggerCategory;
import client.message.MessageDispatcher;
import client.network.socket.ITcpClientSocketListener;
import client.network.socket.TcpClientSocket;
import network.Packet;
import network.ResizableByteBuffer;
import network.message.IMessage;
import stream.ByteInputStream;
import stream.ByteOutputStream;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Class that manages the connection to the server.
 */
public class ServerConnection implements ITcpClientSocketListener {
    /**
     * Size of the buffer used to read data from the socket.
     * When the data is read, it'll be stored in the stream receiveStream.
     * If the buffer is full, extra data is handled by the next call to onReceive.
     */
    private static final int READ_BUFFER_SIZE = 1024;

    /**
     * Initial size of the stream used to store data received from socket and to be handled.
     */
    private static final int INITIAL_RECEIVE_STREAM_SIZE = 1024;

    /**
     * Max size of the stream used to store data received from socket and to be handled.
     * If the stream is full, the connection is closed.
     */
    private static final int MAX_RECEIVE_BUFFER_SIZE = 1024 * 1024;

    /**
     * Initial size of the stream used to store data to send to the socket.
     */
    private static final int INITIAL_SEND_STREAM_SIZE = 1024;

    /**
     * Max size of the stream used to store data to send to the socket.
     * If the stream is full, the connection is closed.
     */
    private static final int MAX_SEND_BUFFER_SIZE = 1024 * 1024;

    private final TcpClientSocket clientSocket;
    private final ByteBuffer readBuffer;
    private final ResizableByteBuffer receiveStream;
    private final ResizableByteBuffer sendStream;
    private final MessageDispatcher messageDispatcher;

    public ServerConnection() throws IOException {
        clientSocket = new TcpClientSocket();
        clientSocket.setListener(this);
        readBuffer = ByteBuffer.allocate(READ_BUFFER_SIZE);
        receiveStream = new ResizableByteBuffer(INITIAL_RECEIVE_STREAM_SIZE, MAX_RECEIVE_BUFFER_SIZE);
        sendStream = new ResizableByteBuffer(INITIAL_SEND_STREAM_SIZE, MAX_SEND_BUFFER_SIZE);
        messageDispatcher = new MessageDispatcher();
    }

    /**
     * Connects to the server.
     *
     * @param host The host of the server.
     * @param port The port of the server.
     */
    public void connect(String host, int port) {
        clientSocket.connect(host, port);
    }

    /**
     * Closes the connection.
     */
    public void close() {
        clientSocket.close();
    }

    /**
     * Invoked when the connection is established.
     */
    @Override
    public void onConnected() {
        Logger.info(LoggerCategory.NETWORK, "Connected to the server.");
        clientSocket.read(readBuffer);
    }

    /**
     * Invoked when the connection to the server is failed.
     */
    @Override
    public void onConnectFailed() {
        Logger.error(LoggerCategory.NETWORK, "Connection failed.");
    }

    /**
     * Invoked when the connection to the server is lost.
     */
    @Override
    public void onDisconnected() {
        Logger.error(LoggerCategory.NETWORK, "Connection lost with the server.");
    }

    /**
     * Invoked when data is received from the server.
     *
     * @param length The length of the data received.
     */
    @Override
    public void onReceive(int length) {
        readBuffer.position(length);
        readBuffer.flip();
        receiveStream.put(readBuffer.array(), 0, length);
        readBuffer.clear();

        ByteInputStream stream = new ByteInputStream(receiveStream.getBuffer(), receiveStream.size());
        int bytesRead = 0;

        while (!stream.isAtEnd()) {
            Packet packet = new Packet();
            int read = packet.decode(stream);

            if (read == -1) {
                break;
            } else if (read == -2) {
                Logger.error(LoggerCategory.NETWORK, "Invalid packet received, closing connection...");
                close();
                return;
            } else if (read == -3) {
                Logger.warn(LoggerCategory.NETWORK, "Invalid packet checksum, closing connection...");
                close();
                return;
            }

            Logger.debug(LoggerCategory.NETWORK, "Received message %s", packet.getMessage());

            bytesRead += read;
            messageDispatcher.handle(packet.getMessage());
        }

        receiveStream.remove(bytesRead);
    }

    /**
     * Invoked when the connection was sent data.
     *
     * @param length
     */
    @Override
    public synchronized void onSend(int length) {
        sendStream.remove(length);

        if (sendStream.size() != 0) {
            clientSocket.write(ByteBuffer.wrap(sendStream.getBuffer(), 0, sendStream.size()));
        }
    }

    /**
     * Sends a message to the server.
     *
     * @param message The message to send.
     */
    public synchronized void send(IMessage message) {
        Logger.debug(LoggerCategory.NETWORK, "Sending message %s", message);

        ByteOutputStream stream = new ByteOutputStream(32);
        Packet packet = Packet.create(message);
        packet.encode(stream);

        send(stream.getBytes(), 0, stream.getLength());
    }

    /**
     * Sends the given data to the server.
     *
     * @param buffer The data to send.
     */
    private synchronized void send(byte[] buffer, int offset, int length) {
        if (sendStream.size() == 0) {
            sendStream.put(buffer, offset, length);
            clientSocket.write(ByteBuffer.wrap(sendStream.getBuffer(), 0, sendStream.size()));
        } else {
            sendStream.put(buffer, offset, length);
        }
    }

    public MessageDispatcher getMessageDispatcher() {
        return messageDispatcher;
    }
}
