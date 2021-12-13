package server;

import logic.config.GameConfig;
import server.matchmaking.Matchmaking;
import server.network.ClientConnectionManager;
import server.network.socket.TcpServerSocket;

import java.io.IOException;
import java.util.HashMap;

/**
 * The server class.
 */
public class Server {
    private static Server instance;

    private final TcpServerSocket serverSocket;
    private final ClientConnectionManager connectionManager;
    private final HashMap<Integer, Matchmaking> matchmaking;
    private final GameConfig gameConfig;

    public Server(String host, int port) throws IOException {
        synchronized (Server.class) {
            if (instance != null) {
                throw new IllegalStateException("Server already running");
            }

            instance = this;
        }

        connectionManager = new ClientConnectionManager();
        serverSocket = new TcpServerSocket(host, port, connectionManager);
        gameConfig = GameConfig.loadFromResources();
        matchmaking = new HashMap<>();
    }

    /**
     * Returns the server's instance.
     *
     * @return the server's instance
     */
    public static Server getInstance() {
        return instance;
    }

    /**
     * Destroys the server.
     */
    public void destroy() {
        this.stop();

        synchronized (Server.class) {
            if (instance == this) {
                instance = null;
            }
        }
    }

    /**
     * Starts the server.
     */
    public void start() {
        connectionManager.start();
        serverSocket.start();
    }

    /**
     * Stops the server.
     */
    public void stop() {
        connectionManager.stop();
        serverSocket.stop();
    }

    /**
     * Returns the server's TCP server socket.
     *
     * @return the server's TCP server socket
     */
    public TcpServerSocket getServerSocket() {
        return serverSocket;
    }

    /**
     * Returns the server's client connection manager.
     *
     * @return the server's client connection manager
     */
    public ClientConnectionManager getConnectionManager() {
        return connectionManager;
    }

    /**
     * Returns the server's game config.
     *
     * @return the server's game config
     */
    public GameConfig getGameConfig() {
        return gameConfig;
    }

    /**
     * Returns the server's matchmaking.
     *
     * @return the server's matchmaking
     */
    public Matchmaking getMatchmaking(int matchCapacity) {
        if (matchCapacity < gameConfig.getMinPlayers())
            return null;
        if (matchCapacity > gameConfig.getMaxPlayers())
            return null;

        return matchmaking.computeIfAbsent(matchCapacity, Matchmaking::new);
    }
}
