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
     * Gets the number of players in the queue.
     * @return the number of players in the queue
     */
    public int getPlayersInQueue() {
        return queue.size();
    }

    /**
     * Adds a client to the queue.
     *
     * @param client
     */
    public synchronized void add(ClientSession client) {
        if (queue.indexOf(client) != -1) {
            throw new IllegalArgumentException("Client already in queue");
        }

        queue.add(client);
        client.setMatchmaking(this);
        notifyMatchmakingProgress();
        checkQueue();
    }

    /**
     * Removes a client from the queue.
     *
     * @param client
     */
    public synchronized void remove(ClientSession client) {
        queue.remove(client);
        notifyMatchmakingProgress();
    }

    /**
     * Checks the queue for matches.
     */
    private synchronized void checkQueue() {
        if (queue.size() >= numPlayersRequired) {
            ClientSession[] sessions = new ClientSession[numPlayersRequired];

            for (int i = 0; i < numPlayersRequired; i++) {
                sessions[i] = queue.remove(0);
            }

            createMatch(sessions);
        }
    }

    /**
     * Creates a match with the given client sessions.
     * @param sessions the client sessions
     */
    protected synchronized void createMatch(ClientSession[] sessions) {
        Match match = new Match(++matchIdCounter, sessions);

        for (ClientSession session : sessions) {
            session.setMatch(match);
            session.setMatchmaking(null);
        }

        match.startGame();
    }

    /**
     * Notifies the clients of the matchmaking progress.
     */
    protected synchronized void notifyMatchmakingProgress() {
        for (ClientSession session : queue) {
            session.getConnection().send(new MatchmakingDataMessage(queue.size(), numPlayersRequired));
        }
    }
}
