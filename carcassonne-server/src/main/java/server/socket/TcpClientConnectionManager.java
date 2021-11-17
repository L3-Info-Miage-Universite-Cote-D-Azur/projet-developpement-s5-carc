package server.socket;

import java.nio.channels.AsynchronousSocketChannel;
import java.util.HashMap;

public class TcpClientConnectionManager {
    private HashMap<Integer, TcpClientConnection> connections;
    private Thread connectionCheckerThread;
    private int nextConnectionId;

    public TcpClientConnectionManager() {
        connections = new HashMap<>();
        connectionCheckerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    for (TcpClientConnection connection : connections.values()) {
                        if (!connection.isConnected()) {
                            connection.close();
                        }
                    }
                }
            }
        });
    }

    public void start() {
        connectionCheckerThread.start();
    }

    public void stop() {
        connectionCheckerThread.interrupt();
    }

    public TcpClientConnection getConnection(int connectionId) {
        return connections.get(connectionId);
    }

    public TcpClientConnection createConnection(AsynchronousSocketChannel channel) {
        TcpClientConnection connection = new TcpClientConnection(channel, nextConnectionId);
        connections.put(nextConnectionId, connection);
        nextConnectionId++;
        return connection;
    }

    public void removeConnection(TcpClientConnection connection) {
        connections.remove(connection.getId());
    }
}
