package client.service;

import client.Client;
import client.ClientTestUtils;
import network.message.connection.ServerHelloMessage;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reflection.ReflectionUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameControllerServiceTest {
    @Test
    void testStartMatchmakingWhenAuthenticated() throws IOException {
        final boolean[] called = {false};

        Client client = Mockito.spy(ClientTestUtils.createMockClient(null, null));
        GameControllerService gameControllerService = new GameControllerService(client);
        Mockito.when(client.getMatchmakingService()).thenReturn(new MatchmakingService(client) {
            @Override
            public void joinMatchmaking(int matchSize) {
                called[0] = true;
            }
        });

        gameControllerService.handleMessage(new ServerHelloMessage(1));

        assertTrue(called[0]);
    }

    @Test
    void testBattleOverRestartMatchmakingOrStopClient() throws Exception {
        final boolean[] called = {false};

        Client client = Mockito.spy(ClientTestUtils.createMockClient(null, null));
        GameControllerService gameControllerService = new GameControllerService(client);
        ReflectionUtils.setField(gameControllerService, "remainingMatches", 500);
        Mockito.when(client.getMatchmakingService()).thenReturn(new MatchmakingService(client) {
            @Override
            public void joinMatchmaking(int matchSize) {
                called[0] = true;
            }
        });

        int remainingMatches = (int) ReflectionUtils.getField(gameControllerService, "remainingMatches");

        for (int i = remainingMatches; i >= 2; i--) {
            called[0] = false;
            ReflectionUtils.callMethod(gameControllerService, "onBattleOver");
            assertTrue(called[0]);
        }

        called[0] = false;
        ReflectionUtils.callMethod(gameControllerService, "onBattleOver");
        assertFalse(called[0]);

        Mockito.verify(client).stop();
    }


}
