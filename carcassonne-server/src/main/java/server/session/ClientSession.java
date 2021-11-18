package server.session;

import server.matchmaking.Match;
import server.matchmaking.Matchmaking;
import server.network.ClientConnection;

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

    public void destroy() {
        synchronized (this) {
            destroyed = true;

            if (currentMatchmaking != null) {
                currentMatchmaking.remove(this);
            }

            if (currentMatch != null) {
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

    public void setMatchmaking(Matchmaking matchmaking) {
        this.currentMatchmaking = matchmaking;
    }

    public Match getMatch() {
        return currentMatch;
    }

    public void setMatch(Match currentMatch) {
        this.currentMatch = currentMatch;
    }
}
