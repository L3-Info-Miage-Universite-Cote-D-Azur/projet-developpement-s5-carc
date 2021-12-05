package server.matchmaking;

import network.message.Message;
import network.message.MessageType;
import org.junit.jupiter.api.Test;
import server.session.ClientSession;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MatchTest {
    @Test
    void testSendGameDataWhenStarting() {
        ClientSession[] players = new ClientSession[] {
                new ClientSession(null, 1),
                new ClientSession(null, 2),
        };

        final Boolean[] completed = new Boolean[] {false};

        Match match = new Match(1, players) {
            @Override
            protected void sendMessageToConnectedClients(Message message) {
                if (message.getType() == MessageType.GAME_DATA) {
                    completed[0] = true;
                } else {
                    throw new RuntimeException("Unexpected message type");
                }
            }
        };

        match.start();

        assertTrue(completed[0]);
    }

    @Test
    void testSendGameMasterNextTurnDataWhenNewTurnStarted() {
        ClientSession[] players = new ClientSession[] {
                new ClientSession(null, 1),
                new ClientSession(null, 2),
        };

        final Boolean[] completed = new Boolean[] {false};

        Match match = new Match(1, players) {
            @Override
            protected void sendMessageToConnectedClients(Message message) {
                if (message.getType() == MessageType.GAME_MASTER_NEXT_TURN_DATA) {
                    completed[0] = true;
                }
            }
        };

        match.start();
        match.onPlayerDisconnected(players[0]);

        assertTrue(completed[0]);
    }

    @Test
    void testSendGameResultWhenMatchOver() {
        ClientSession[] players = new ClientSession[] {
                new ClientSession(null, 1),
                new ClientSession(null, 2),
        };

        final Boolean[] completed = new Boolean[] {false};

        Match match = new Match(1, players) {
            @Override
            protected void sendMessageToConnectedClients(Message message) {
                if (message.getType() == MessageType.GAME_RESULT) {
                    completed[0] = true;
                }
            }
        };

        match.start();
        match.onGameOver();

        assertTrue(completed[0]);
    }

    @Test
    void testAutoCompleteWhenPlayersOffline() {
        ClientSession[] players = new ClientSession[] {
            new ClientSession(null, 1),
            new ClientSession(null, 2),
        };

        final Boolean[] completed = new Boolean[] {false};

        Match match = new Match(1, players) {
            @Override
            public void destroy() {
                completed[0] = true;
            }
        };

        match.onPlayerDisconnected(players[0]);
        match.onPlayerDisconnected(players[1]);

        match.start();

        assertTrue(completed[0]);
    }
}
