package client;

import client.config.ClientConfig;
import client.network.ServerConnection;
import logic.config.GameConfig;
import network.message.connection.ServerHelloMessage;

import java.io.IOException;

public class ClientTestUtils {
    public static Client createMockClient(ServerConnection customServerConnection, Integer customUserId) throws IOException {
        Client client = new Client(ClientConfig.loadFromResources(), GameConfig.loadFromResources()) {
            @Override
            public ServerConnection getServerConnection() {
                if (customServerConnection != null) {
                    return customServerConnection;
                }
                return super.getServerConnection();
            }
        };

        if (customUserId != null) {
            client.getAuthenticationService().handleMessage(new ServerHelloMessage(customUserId));
        }

        return client;
    }
}
