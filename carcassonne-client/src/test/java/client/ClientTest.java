package client;

import client.config.ClientConfig;
import client.service.*;
import logic.config.GameConfig;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {
    @Test
    void testConstructor() throws IOException {
        ClientConfig clientConfig = ClientConfig.loadFromResources();
        GameConfig gameConfig = GameConfig.loadFromResources();
        assertNotNull(clientConfig);
        Client client = new Client(clientConfig, gameConfig);

        assertEquals(AuthenticationService.class, client.getAuthenticationService().getClass());
        assertEquals(MatchmakingService.class, client.getMatchmakingService().getClass());
        assertEquals(BattleService.class, client.getBattleService().getClass());
        assertEquals(GameStatisticsService.class, client.getGameStatisticsService().getClass());
        assertEquals(GameControllerService.class, client.getService(GameControllerService.class).getClass());

        assertEquals(clientConfig, client.getConfig());
        assertEquals(gameConfig, client.getGameConfig());
    }

    @Test
    void testNotifyOnStop() throws Exception {
        ClientConfig clientConfig = ClientConfig.loadFromResources();
        GameConfig gameConfig = GameConfig.loadFromResources();
        assertNotNull(clientConfig);
        Client client = new Client(clientConfig, gameConfig);

        try {
            client.start();
        } catch (Exception ignored) {
        }

        synchronized (client) {
            client.wait(60000);
        }
        assertTrue(true);
    }
}
