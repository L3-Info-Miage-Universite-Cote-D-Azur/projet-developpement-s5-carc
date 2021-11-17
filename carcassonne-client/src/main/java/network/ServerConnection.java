package network;

import logger.Logger;
import message.MessageHandler;
import network.socket.ITcpClientSocketListener;
import network.socket.TcpClientSocket;
import stream.ByteInputStream;

import java.io.IOException;
import java.nio.ByteBuffer;

public class ServerConnection implements ITcpClientSocketListener {
    private static int READ_BUFFER_SIZE = 1024;
    private static int WRITE_BUFFER_SIZE = 1024;

    private final TcpClientSocket clientSocket;
    private final ByteBuffer readBuffer;
    private final ByteBuffer writeBuffer;
    private final MessageHandler messageHandler;

    public ServerConnection() throws IOException {
        clientSocket = new TcpClientSocket();
        clientSocket.setListener(this);
        readBuffer = ByteBuffer.allocate(READ_BUFFER_SIZE);
        writeBuffer = ByteBuffer.allocate(WRITE_BUFFER_SIZE);
        messageHandler = new MessageHandler(this);
    }

    public void close() {
        clientSocket.close();
    }

    @Override
    public void onConnected() {
        Logger.info("Connected to the server");
    }

    @Override
    public void onConnectFailed() {
        Logger.warn("Connection failed");
    }

    @Override
    public void onDisconnected() {
        Logger.warn("Disconnected from the server");
    }

    @Override
    public void onReceive(int length) {
        readBuffer.position(length);
        readBuffer.flip();

        ByteInputStream stream = new ByteInputStream(readBuffer.array(), length);

        while (!stream.isAtEnd()) {
            Packet packet = new Packet();
            int read = packet.decode(stream);

            if (read == -1) {
                break;
            } else if (read == -2) {
                Logger.warn("Checksum mismatch");
                close();
                return;
            } else if (read == -3) {
                close();
                return;
            }

            messageHandler.handle(packet.getMessage());
        }
    }
}
