package client.service;

import client.Client;
import client.ClientTestUtils;
import client.network.ServerConnection;
import network.message.IMessage;
import network.message.matchmaking.JoinMatchmakingMessage;
import network.message.matchmaking.MatchmakingDataMessage;
import network.message.matchmaking.MatchmakingFailedMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reflection.ReflectionUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MatchmakingServiceTest {
    private Client client;
    private MatchmakingService matchmakingService;

    private Object messageSent;

    @BeforeEach
    void setup() throws IOException {
        client = ClientTestUtils.createMockClient(new ServerConnection() {
            @Override
            public synchronized void send(IMessage message) {
                messageSent = message;
            }
        }, 1);
        matchmakingService = client.getMatchmakingService();
    }

    @Test
    void testReset() throws Exception {
        ReflectionUtils.setField(matchmakingService, "playersInQueue", 1);
        ReflectionUtils.setField(matchmakingService, "playersNeeded", 2);

        ReflectionUtils.callMethod(matchmakingService, "reset");

        assertEquals(0, (int) ReflectionUtils.getField(matchmakingService, "playersInQueue"));
        assertEquals(0, (int) ReflectionUtils.getField(matchmakingService, "playersNeeded"));
    }

    @Test
    void testHandleMatchmakingData() throws Exception {
        matchmakingService.handleMessage(new MatchmakingDataMessage(1, 2));

        assertEquals(0, (int) ReflectionUtils.getField(matchmakingService, "playersInQueue"));
        assertEquals(0, (int) ReflectionUtils.getField(matchmakingService, "playersNeeded"));

        ReflectionUtils.setField(matchmakingService, "isInMatchmaking", true);

        matchmakingService.handleMessage(new MatchmakingDataMessage(1, 2));

        assertEquals(1, (int) ReflectionUtils.getField(matchmakingService, "playersInQueue"));
        assertEquals(2, (int) ReflectionUtils.getField(matchmakingService, "playersNeeded"));
    }

    @Test
    void testHandleMatchmakingFailed() throws Exception {
        ReflectionUtils.setField(matchmakingService, "isInMatchmaking", true);
        matchmakingService.handleMessage(new MatchmakingFailedMessage());
        assertEquals(false, (boolean) ReflectionUtils.getField(matchmakingService, "isInMatchmaking"));
    }

    @Test
    void testJoinMatchmaking() throws Exception {
        matchmakingService.joinMatchmaking(10);

        assertEquals(true, (boolean) ReflectionUtils.getField(matchmakingService, "isInMatchmaking"));
        assertEquals(JoinMatchmakingMessage.class, messageSent.getClass());

        assertThrows(IllegalStateException.class, () -> matchmakingService.joinMatchmaking(10));
        assertThrows(IllegalStateException.class, () -> matchmakingService.joinMatchmaking(5));
    }
}
