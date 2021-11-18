package server.matchmaking;

import network.message.matchmaking.MatchmakingDataMessage;
import server.session.ClientSession;

import java.util.ArrayList;

public class Matchmaking {
    private ArrayList<ClientSession> queue;
    private int numPlayersRequired;
    private int matchIdCounter;

    public Matchmaking(int numPlayersRequired) {
        this.numPlayersRequired = numPlayersRequired;
        queue = new ArrayList<>();
    }

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

    public void remove(ClientSession client) {
        synchronized (this) {
            queue.remove(client);
            notifyMatchmakingProgress();
        }
    }

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

    private void notifyMatchmakingProgress() {
        synchronized (this) {
            for (ClientSession session : queue) {
                session.getConnection().send(new MatchmakingDataMessage(queue.size(), numPlayersRequired));
            }
        }
    }
}
