package server;

import logic.config.GameConfig;
import server.socket.TcpClientConnectionManager;
import server.socket.TcpServerSocket;



public class Server {
    private static Server instance;

    private final TcpServerSocket serverSocket;
    private final TcpClientConnectionManager connectionManager;

    private final GameConfig gameConfig;

    public Server(String host, int port) throws Exception {
        if (instance != null) {
            throw new Exception("Server already running");
        }

        instance = this;
        serverSocket = new TcpServerSocket(host, port);
        connectionManager = new TcpClientConnectionManager();
        gameConfig = GameConfig.loadFromResources("config");
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

    public TcpClientConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public static Server getInstance() {
        return instance;
    }
}
