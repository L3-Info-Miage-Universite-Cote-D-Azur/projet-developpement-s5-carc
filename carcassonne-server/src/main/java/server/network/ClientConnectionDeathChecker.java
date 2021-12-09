package server.network;

import java.util.Map;
import java.util.TimerTask;

/**
 * Task that checks if the client connections are still alive.
 */
public class ClientConnectionDeathChecker extends TimerTask {
    private Map<Integer, ClientConnection> connections;

    public ClientConnectionDeathChecker(Map<Integer, ClientConnection> connections) {
        this.connections = connections;
    }

    /**
     * Routines that checks if the client connections are still alive.
     */
    @Override
    public void run() {
        for (ClientConnection connection : connections.values()) {
            if (!connection.isConnected()) {
                connection.close();
            }
        }
    }
}
