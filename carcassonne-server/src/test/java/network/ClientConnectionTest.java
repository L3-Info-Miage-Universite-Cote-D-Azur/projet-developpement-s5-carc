package network;

import logic.Game;
import logic.player.Player;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import server.network.ClientConnection;
import server.session.ClientSession;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientConnectionTest {

    @Disabled
    void testGetId() {
        ClientConnection client = new ClientConnection(0,1);
        assertEquals(1, ClientConnection.getId());
    }
    @Disabled
    void testGetSession() {
        ClientSession client = new ClientSession(0,1);
        assertEquals(1, ClientSession.getSession);
    }

    @Disabled
    void testSetSession() {
        ClientSession client = new ClientSession(ClientSession,1);
        ClientSession session = new ClientSession(client);
        ClientSession.setSession(session);
        assertEquals(session, ClientSession.getSession());
    }
}
