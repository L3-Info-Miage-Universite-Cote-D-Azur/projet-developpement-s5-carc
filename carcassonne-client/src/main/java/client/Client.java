package client;

import client.config.ClientConfig;
import client.logger.Logger;
import client.message.IMessageHandler;
import client.network.ServerConnection;
import client.service.*;
import logic.config.GameConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that represents the client.
 */
public class Client {
    private final ClientConfig config;
    private final GameConfig gameConfig;
    private final ServerConnection serverConnection;
    private final Map<Class<? extends ServiceBase>, ServiceBase> services;

    public Client(ClientConfig config, GameConfig gameConfig) throws IOException {
        this.config = config;
        this.gameConfig = gameConfig;

        Logger.setConfig(config.getLoggerConfig());

        this.serverConnection = new ServerConnection() {
            @Override
            public void onConnected() {
                super.onConnected();

                for (ServiceBase service : services.values()) {
                    service.onConnect();
                }
            }

            @Override
            public void onDisconnected() {
                super.onDisconnected();

                for (ServiceBase service : services.values()) {
                    service.onDisconnect();
                }
            }
        };
        this.services = new HashMap<>();

        this.recordService(new AuthenticationService(this));
        this.recordService(new MatchmakingService(this));
        this.recordService(new BattleService(this));
        this.recordService(new GameStatisticsService(this));

        this.recordService(new GameControllerService(this, 2, 1));

        this.serverConnection.connect(config.getServerHost(), config.getServerPort());
    }

    /**
     * Records the specified service and registers it to the message dispatcher if it implements the IMessageHandler interface.
     * @param service the service
     */
    private void recordService(ServiceBase service) {
        this.services.put(service.getClass(), service);

        if (service instanceof IMessageHandler messageHandler) {
            this.serverConnection.getMessageDispatcher().addHandler(messageHandler);
        }
    }

    /**
     * Gets the server connection.
     * @return the server connection
     */
    public final ServerConnection getServerConnection() {
        return serverConnection;
    }

    /**
     * Gets the game config.
     * @return the game config
     */
    public final GameConfig getGameConfig() {
        return gameConfig;
    }

    /**
     * Gets the authentication service.
     * @return the authentication service
     */
    public final AuthenticationService getAuthenticationService() {
        return (AuthenticationService) services.get(AuthenticationService.class);
    }

    /**
     * Gets the matchmaking service.
     * @return the matchmaking service
     */
    public MatchmakingService getMatchmakingService() {
        return (MatchmakingService) services.get(MatchmakingService.class);
    }

    /**
     * Gets the battle service.
     * @return the battle service
     */
    public BattleService getBattleService() {
        return (BattleService) services.get(BattleService.class);
    }

    /**
     * Gets the game statistics service.
     * @return the game statistics service
     */
    public GameStatisticsService getGameStatisticsService() {
        return (GameStatisticsService) services.get(GameStatisticsService.class);
    }

    /**
     * Gets the service of the specified type.
     * @param serviceClass the service class
     * @return the service
     */
    public ServiceBase getService(Class<? extends ServiceBase> serviceClass) {
        return services.get(serviceClass);
    }

    /**
     * Closes the client.
     */
    public void stop() {
        serverConnection.close();

        for (ServiceBase service : services.values()) {
            service.onDisconnect();
        }

        synchronized (this) {
            this.notifyAll();
        }
    }
}
