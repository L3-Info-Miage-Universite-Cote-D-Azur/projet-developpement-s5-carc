package server.network;

import server.logger.Logger;

import java.nio.channels.AsynchronousSocketChannel;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a TCP client connection manager.
 */
public class ClientConnectionManager {
    private final ConcurrentHashMap<Integer, ClientConnection> connections;
    private final Timer connectionChecker;
    private int nextConnectionId;

    private boolean running;

    public ClientConnectionManager() {
        connections = new ConcurrentHashMap<>();
        connectionChecker = new Timer();
    }

    /**
     * Starts the connection manager.
     */
    public void start() {
        if (running) {
            return;
        }

        running = true;
        connectionChecker.schedule(new ClientConnectionDeathChecker(connections), 0, 500);
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

        connectionChecker.cancel();
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

        Logger.debug("Connection %d created. IP:%s", connection.getId(), connection.getRemoteAddress());

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
