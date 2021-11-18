package server.session;

import server.matchmaking.Match;
import server.matchmaking.Matchmaking;
import server.network.ClientConnection;

/**
 * Represents a client session.
 * It is used to store information about the client connection.
 */
public class ClientSession {
    private final ClientConnection connection;
    private final int userId;

    private Matchmaking currentMatchmaking;
    private Match currentMatch;

    private boolean destroyed;

    public ClientSession(ClientConnection connection, int userId) {
        this.connection = connection;
        this.userId = userId;
    }

    /**
     * Destroys the client session.
     */
    public synchronized void destroy() {
        if (destroyed) {
            return;
        }

        destroyed = true;

        if (currentMatchmaking != null) {
            currentMatchmaking.remove(this);
        }

        if (currentMatch != null) {
            synchronized (currentMatch) {
                currentMatch.removePlayer(this);
            }
        }
    }

    public ClientConnection getConnection() {
        return connection;
    }

    public int getUserId() {
        return userId;
    }

    public Matchmaking getMatchmaking() {
        return currentMatchmaking;
    }

    public synchronized void setMatchmaking(Matchmaking matchmaking) {
        if (destroyed) {
            matchmaking.remove(this);
        } else {
            this.currentMatchmaking = matchmaking;
        }
    }

    public Match getMatch() {
        return currentMatch;
    }

    public synchronized void setMatch(Match currentMatch) {
        if (destroyed) {
            synchronized (currentMatch) {
                currentMatch.removePlayer(this);
            }
        } else {
            this.currentMatch = currentMatch;
        }
    }
}
