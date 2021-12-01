package server;

import logic.config.GameConfig;
import server.matchmaking.Matchmaking;
import server.network.ClientConnectionManager;
import server.network.socket.TcpServerSocket;

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

    public Server(String host, int port) throws Exception {
        if (instance != null) {
            throw new Exception("Server already running");
        }

        instance = this;
        connectionManager = new ClientConnectionManager();
        serverSocket = new TcpServerSocket(host, port, connectionManager);
        gameConfig = GameConfig.loadFromResources();
        matchmaking = new HashMap<>();
    }

    /**
     * Destroys the server.
     */
    public void destroy() {
        this.stop();

        if (instance == this) {
            instance = null;
        }
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
     * @return
     */
    public TcpServerSocket getServerSocket() {
        return serverSocket;
    }

    /**
     * Returns the server's client connection manager.
     *
     * @return
     */
    public ClientConnectionManager getConnectionManager() {
        return connectionManager;
    }

    /**
     * Returns the server's game config.
     *
     * @return
     */
    public GameConfig getGameConfig() {
        return gameConfig;
    }

    /**
     * Returns the server's matchmaking.
     *
     * @return
     */
    public Matchmaking getMatchmaking(int matchCapacity) {
        if (matchCapacity < gameConfig.minPlayers)
            return null;
        if (matchCapacity > gameConfig.maxPlayers)
            return null;

        if (!matchmaking.containsKey(matchCapacity)) {
            matchmaking.put(matchCapacity, new Matchmaking(matchCapacity));
        }

        return matchmaking.get(matchCapacity);
    }
}
