package server;

import logic.config.GameConfig;
import server.matchmaking.Matchmaking;
import server.network.ClientConnectionManager;
import server.network.socket.TcpServerSocket;

/**
 * The server class.
 */
public class Server {
    private static Server instance;

    private final TcpServerSocket serverSocket;
    private final ClientConnectionManager connectionManager;
    private final Matchmaking matchmaking;
    private final GameConfig gameConfig;

    public Server(String host, int port) throws Exception {
        if (instance != null) {
            throw new Exception("Server already running");
        }

        instance = this;
        serverSocket = new TcpServerSocket(host, port);
        connectionManager = new ClientConnectionManager();
        gameConfig = GameConfig.loadFromResources();
        matchmaking = new Matchmaking(gameConfig.maxPlayers);
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
     * @return
     */
    public TcpServerSocket getServerSocket() {
        return serverSocket;
    }

    /**
     * Returns the server's client connection manager.
     * @return
     */
    public ClientConnectionManager getConnectionManager() {
        return connectionManager;
    }

    /**
     * Returns the server's game config.
     * @return
     */
    public GameConfig getGameConfig() {
        return gameConfig;
    }

    /**
     * Returns the server's matchmaking.
     * @return
     */
    public Matchmaking getMatchmaking() {
        return matchmaking;
    }

    /**
     * Returns the server's instance.
     * @return
     */
    public static Server getInstance() {
        return instance;
    }
}
