package client.service;

import client.Client;

/**
 * Base class for all services.
 */
public abstract class ServiceBase {
    public final Client client;

    public ServiceBase(Client client) {
        this.client = client;
    }

    /**
     * Gets the client.
     *
     * @return the client
     */
    public final Client getClient() {
        return client;
    }

    /**
     * Called when the client is connected to the server.
     */
    public abstract void onConnect();

    /**
     * Called when the client is disconnected from the server.
     */
    public abstract void onDisconnect();
}
