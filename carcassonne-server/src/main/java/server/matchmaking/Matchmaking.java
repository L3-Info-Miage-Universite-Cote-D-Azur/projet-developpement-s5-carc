package server.matchmaking;

import network.message.matchmaking.MatchmakingDataMessage;
import server.session.ClientSession;

import java.util.ArrayList;

/**
 * Represents a matchmaking queue.
 */
public class Matchmaking {
    private ArrayList<ClientSession> queue;
    private int numPlayersRequired;
    private int matchIdCounter;

    public Matchmaking(int numPlayersRequired) {
        this.numPlayersRequired = numPlayersRequired;
        queue = new ArrayList<>();
    }

    /**
     * Adds a client to the queue.
     * @param client
     */
    public void add(ClientSession client) {
        synchronized (this) {
            if (queue.indexOf(client) != -1) {
                throw new IllegalArgumentException("Client already in queue");
            }

            queue.add(client);
            client.setMatchmaking(this);
            notifyMatchmakingProgress();
            checkQueue();
        }
    }

    /**
     * Removes a client from the queue.
     * @param client
     */
    public void remove(ClientSession client) {
        synchronized (this) {
            queue.remove(client);
            notifyMatchmakingProgress();
        }
    }

    /**
     * Checks the queue for matches.
     */
    private void checkQueue() {
        synchronized (this) {
            if (queue.size() >= numPlayersRequired) {
                ClientSession[] sessions = new ClientSession[numPlayersRequired];

                for (int i = 0; i < numPlayersRequired; i++) {
                    sessions[i] = queue.remove(0);
                }

                Match match = new Match(matchIdCounter++, sessions);

                for (ClientSession session : sessions) {
                    session.setMatch(match);
                    session.setMatchmaking(null);
                }

                match.startGame();
            }
        }
    }

    /**
     * Notifies the clients of the matchmaking progress.
     */
    private void notifyMatchmakingProgress() {
        synchronized (this) {
            for (ClientSession session : queue) {
                session.getConnection().send(new MatchmakingDataMessage(queue.size(), numPlayersRequired));
            }
        }
    }
}
