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

    public void start() {
        connectionManager.start();
        serverSocket.start();
    }

    public void stop() {
        connectionManager.stop();
        serverSocket.stop();
    }

    public TcpServerSocket getServerSocket() {
        return serverSocket;
    }

    public ClientConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public GameConfig getGameConfig() {
        return gameConfig;
    }

    public Matchmaking getMatchmaking() {
        return matchmaking;
    }

    public static Server getInstance() {
        return instance;
    }
}
