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
     * Sizes the match to play.
     */
    private final int matchSizeToPlay;

    /**
     * Numbers of matches to play.
     */
    private final int numberOfMatches;

    /**
     * Remaining matches to play.
     */
    private int remainingMatches;

    public GameControllerService(Client client, int matchSize, int numberOfMatches) {
        super(client);
        this.matchSizeToPlay = matchSize;
        this.numberOfMatches = numberOfMatches;
    }

    /**
     * Handles the specified message if the handler is interested in it.
     * @param message The message to handle.
     */
    @Override
    public void handleMessage(Message message) {
        switch (message.getType()) {
            case SERVER_HELLO -> onAuthenticated();
            case GAME_RESULT -> onBattleOver();
        }
    }

    /**
     * Called when the client is connected and ready to play.
     */
    private void onAuthenticated() {
        client.getMatchmakingService().joinMatchmaking(matchSizeToPlay);
    }

    /**
     * Called when the client has finished playing its match.
     */
    private void onBattleOver() {
        remainingMatches--;

        if (remainingMatches == 0) {
            client.stop();
        } else {
            client.getMatchmakingService().joinMatchmaking(matchSizeToPlay);
        }
    }

    /**
     * Called when the client is connected to the server.
     */
    @Override
    public void onConnect() {
        remainingMatches = numberOfMatches;
    }

    /**
     * Called when the client is disconnected from the server.
     */
    @Override
    public void onDisconnect() {
        remainingMatches = 0;
    }
}
