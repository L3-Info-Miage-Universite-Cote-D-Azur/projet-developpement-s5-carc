package server.network;

import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a TCP client connection manager.
 */
public class ClientConnectionManager {
    private ConcurrentHashMap<Integer, ClientConnection> connections;
    private Thread connectionCheckerThread;
    private int nextConnectionId;

    private boolean running;

    public ClientConnectionManager() {
        connections = new ConcurrentHashMap<>();
        connectionCheckerThread = new Thread(() -> {
            while (running) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                for (ClientConnection connection : connections.values()) {
                    if (!connection.isConnected()) {
                        connection.close();
                    }
                }
            }
        });
    }

    /**
     * Starts the connection manager.
     */
    public void start() {
        if (running) {
            return;
        }

        running = true;
        connectionCheckerThread.start();
    }

    /**
     * Stops the connection manager.
     */
    public void stop() {
        if (!running) {
            return;
        }

        running = false;

        for (ClientConnection connection : connections.values()) {
            connection.close();
        }

        try {
            connectionCheckerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets a connection by its id.
     *
     * @param connectionId The connection id.
     * @return The connection.
     */
    public ClientConnection getConnection(int connectionId) {
        return connections.get(connectionId);
    }

    /**
     * Creates a new connection with the given socket channel.
     *
     * @param channel The socket channel.
     * @return The connection.
     */
    public ClientConnection createConnection(AsynchronousSocketChannel channel) {
        ClientConnection connection = new ClientConnection(channel, nextConnectionId);
        connections.put(nextConnectionId, connection);
        nextConnectionId++;
        connection.startIO();
        return connection;
    }

    /**
     * Removes a connection.
     *
     * @param connection The connection.
     */
    public void removeConnection(ClientConnection connection) {
        connections.remove(connection.getId());
    }

    /**
     * Gets the number of connections.
     *
     * @return The number of connections.
     */
    public int count() {
        return connections.size();
    }
}
