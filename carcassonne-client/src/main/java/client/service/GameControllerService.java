package client.service;

import client.Client;
import client.message.IMessageHandler;
import network.message.Message;

/**
 * Services that controls the game.
 * It joins automatically the matchmaking and stops the client when all matches are finished.
 */
public class GameControllerService extends ServiceBase implements IMessageHandler {
    /**
     * Remaining matches to play.
     */
    private int remainingMatches;

    public GameControllerService(Client client) {
        super(client);
    }

    /**
     * Handles the specified message if the handler is interested in it.
     *
     * @param message The message to handle.
     */
    @Override
    public void handleMessage(Message message) {
        switch (message.getType()) {
            case SERVER_HELLO -> onAuthenticated();
            case GAME_RESULT -> onBattleOver();
            default -> throw new IllegalStateException("Unexpected value: " + message.getType());
        }
    }

    /**
     * Called when the client is connected and ready to play.
     */
    private void onAuthenticated() {
        client.getMatchmakingService().joinMatchmaking(client.getConfig().getMatchConfig().getNumPlayers());
    }

    /**
     * Called when the client has finished playing its match.
     */
    private void onBattleOver() {
        remainingMatches--;

        if (remainingMatches == 0) {
            client.stop();
        } else {
            client.getMatchmakingService().joinMatchmaking(client.getConfig().getMatchConfig().getNumPlayers());
        }
    }

    /**
     * Called when the client is connected to the server.
     */
    @Override
    public void onConnect() {
        remainingMatches = client.getConfig().getMatchConfig().getNumMatches();
    }

    /**
     * Called when the client is disconnected from the server.
     */
    @Override
    public void onDisconnect() {
        remainingMatches = 0;
    }
}
