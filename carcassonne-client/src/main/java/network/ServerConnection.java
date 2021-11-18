package network;

import logger.Logger;
import message.MessageHandler;
import network.message.Message;
import network.message.connection.ClientHelloMessage;
import network.socket.ITcpClientSocketListener;
import network.socket.TcpClientSocket;
import stream.ByteInputStream;
import stream.ByteOutputStream;

import java.io.IOException;
import java.nio.ByteBuffer;

public class ServerConnection implements ITcpClientSocketListener {
    /**
     * Size of the buffer used to read data from the socket.
     * When the data is read, it'll be stored in the stream receiveStream.
     * If the buffer is full, extra data is handled by the next call to onReceive.
     */
    private static int READ_BUFFER_SIZE = 1024;

    /**
     * Initial size of the stream used to store data received from socket and to be handled.
     */
    private static int INITIAL_RECEIVE_STREAM_SIZE = 1024;

    /**
     * Max size of the stream used to store data received from socket and to be handled.
     * If the stream is full, the connection is closed.
     */
    private static int MAX_RECEIVE_BUFFER_SIZE = 1024 * 1024;

    private final TcpClientSocket clientSocket;
    private final ByteBuffer readBuffer;
    private ResizableByteBuffer receiveStream;
    private final MessageHandler messageHandler;

    public ServerConnection() throws IOException {
        clientSocket = new TcpClientSocket();
        clientSocket.setListener(this);
        readBuffer = ByteBuffer.allocate(READ_BUFFER_SIZE);
        receiveStream = new ResizableByteBuffer(INITIAL_RECEIVE_STREAM_SIZE, MAX_RECEIVE_BUFFER_SIZE);
        messageHandler = new MessageHandler(this);
    }

    /**
     * Connects to the server.
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
        Logger.info("Network: Connected to the server.");
        clientSocket.read(readBuffer);
        send(new ClientHelloMessage());
    }

    /**
     * Invoked when the connection to the server is failed.
     */
    @Override
    public void onConnectFailed() {
        Logger.warn("Connection failed");
    }

    /**
     * Invoked when the connection to the server is lost.
     */
    @Override
    public void onDisconnected() {
        Logger.warn("Disconnected from the server");
    }

    /**
     * Invoked when data is received from the server.
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
                Logger.warn("Network: Invalid packet received, closing connection...");
                close();
                return;
            } else if (read == -3) {
                Logger.warn("Network: Invalid packet checksum, closing connection...");
                close();
                return;
            }

            Logger.info("Network: Received message " + packet.getMessageType());

            bytesRead += read;
            messageHandler.handle(packet.getMessage());
        }

        receiveStream.remove(bytesRead);
    }

    /**
     * Sends a message to the server.
     * @param message The message to send.
     */
    public void send(Message message) {
        Logger.info("Network: Sending message " + message.getType());

        ByteOutputStream stream = new ByteOutputStream(32);
        Packet packet = Packet.create(message);
        packet.encode(stream);
        clientSocket.write(ByteBuffer.wrap(stream.getBytes(), 0, stream.getLength()));
    }
}
