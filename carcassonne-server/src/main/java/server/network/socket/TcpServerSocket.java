package server.network.socket;

import server.network.ClientConnectionManager;
import server.network.socket.handler.TcpAcceptHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;

/**
 * Represents a TCP server socket.
 */
public class TcpServerSocket {
    private final AsynchronousServerSocketChannel serverSocketChannel;
    private final ClientConnectionManager connectionManager;

    public TcpServerSocket(String host, int port, ClientConnectionManager connectionManager) throws IOException {
        serverSocketChannel = AsynchronousServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(host, port));
        this.connectionManager = connectionManager;
    }

    /**
     * Starts the server socket to listen for incoming connections.
     */
    public void start() {
        serverSocketChannel.accept(null, new TcpAcceptHandler(serverSocketChannel, connectionManager));
    }

    /**
     * Closes the server socket.
     */
    public void stop() {
        try {
            serverSocketChannel.close();
        } catch (IOException e) {
            // Ignore
        }
    }
}
