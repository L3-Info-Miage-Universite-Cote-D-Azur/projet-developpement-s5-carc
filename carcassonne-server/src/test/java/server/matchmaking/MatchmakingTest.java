package server.matchmaking;

import network.message.Message;
import org.junit.jupiter.api.Test;
import server.network.ClientConnection;
import server.session.ClientSession;

import static org.junit.jupiter.api.Assertions.*;

public class MatchmakingTest {
    @Test
    void testAddRemove() {
        final Integer[] numMatchmakingProgressSent = {0};

        Matchmaking matchmaking = new Matchmaking(5) {
            @Override
            protected synchronized void notifyMatchmakingProgress() {
                numMatchmakingProgressSent[0]++;
            }
        };

        ClientSession session1 = new ClientSession(createFakeClientConnection(1), 1);
        ClientSession session2 = new ClientSession(createFakeClientConnection(2), 2);

        matchmaking.add(session1);
        matchmaking.add(session2);
        assertEquals(2, matchmaking.getPlayersInQueue());
        assertEquals(2, numMatchmakingProgressSent[0]);

        matchmaking.remove(session1);
        assertEquals(1, matchmaking.getPlayersInQueue());
        assertEquals(3, numMatchmakingProgressSent[0]);

        matchmaking.remove(session2);
        assertEquals(0, matchmaking.getPlayersInQueue());
        assertEquals(4, numMatchmakingProgressSent[0]);
    }

    @Test
    void testCreateMatch() {
        final Integer[] numMatchmakingProgressSent = {0};
        final Boolean[] matchCreated = {false};

        ClientSession session1 = new ClientSession(createFakeClientConnection(1), 1);
        ClientSession session2 = new ClientSession(createFakeClientConnection(2), 2);

        Matchmaking matchmaking = new Matchmaking(2) {
            @Override
            protected synchronized void createMatch(ClientSession[] sessions) {
                assertEquals(session1, sessions[0]);
                assertEquals(session2, sessions[1]);
                matchCreated[0] = true;
            }

            @Override
            protected synchronized void notifyMatchmakingProgress() {
                numMatchmakingProgressSent[0]++;
            }
        };

        matchmaking.add(session1);
        matchmaking.add(session2);

        assertTrue(matchCreated[0]);
        assertEquals(0, matchmaking.getPlayersInQueue());
        assertEquals(2, numMatchmakingProgressSent[0]);
    }

    private static ClientConnection createFakeClientConnection(int id) {
        return new ClientConnection(null, id) {
            @Override
            public synchronized void send(Message message) {
            }
        };
    }
}
