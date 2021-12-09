package client.service;

import client.Client;
import client.config.ClientConfig;
import client.network.ServerConnection;
import logic.config.GameConfig;
import network.message.IMessage;
import network.message.MessageType;
import network.message.connection.ServerHelloMessage;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticationServiceTest {
    @Test
    void testAuthenticateSendClientHello() throws IOException {
        final boolean[] called = {false};

        Client client = new Client(ClientConfig.loadFromResources(), GameConfig.loadFromResources()) {
            @Override
            public ServerConnection getServerConnection() {
                try {
                    return new ServerConnection() {
                        @Override
                        public synchronized void send(IMessage message) {
                            if (message.getType() == MessageType.CLIENT_HELLO) {
                                called[0] = true;
                            }
                        }
                    };
                } catch (IOException e) {
                    return null;
                }
            }
        };
        AuthenticationService authenticationService = client.getAuthenticationService();
        authenticationService.onConnect();

        assertTrue(called[0]);
    }

    @Test
    void testAuthenticatedAfterServerHello() {
        AuthenticationService authenticationService = new AuthenticationService(null);
        assertFalse(authenticationService.isAuthenticated());
        authenticationService.handleMessage(new ServerHelloMessage());
        assertTrue(authenticationService.isAuthenticated());

        assertThrows(IllegalStateException.class, () -> authenticationService.handleMessage(new ServerHelloMessage()));
    }
}
