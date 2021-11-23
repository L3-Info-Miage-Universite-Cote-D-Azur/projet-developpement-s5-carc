package client.service;

import client.Client;
import client.logger.Logger;
import client.logger.LoggerCategory;
import client.message.IMessageHandler;
import network.message.Message;
import network.message.game.GameDataMessage;
import network.message.matchmaking.JoinMatchmakingMessage;
import network.message.matchmaking.MatchmakingDataMessage;
import network.message.matchmaking.MatchmakingFailedMessage;

public class MatchmakingService extends ServiceBase implements IMessageHandler {
    /**
     * Indicates if the client is currently in a matchmaking process.
     */
    private boolean isInMatchmaking;

    /**
     * Number of players that are currently in your matchmaking queue.
     */
    private int playersInQueue;

    /**
     * Number of players that are required to start a match.
     */
    private int playersNeeded;

    public MatchmakingService(Client client) {
        super(client);
    }

    /**
     * Resets the matchmaking data.
     */
    private void reset() {
        isInMatchmaking = false;
        playersInQueue = 0;
        playersNeeded = 0;
    }

    /**
     * Handles the specified message if the handler is interested in it.
     * @param message The message to handle.
     */
    @Override
    public void handleMessage(Message message) {
        switch (message.getType()) {
            case MATCHMAKING_DATA -> onMatchmakingData((MatchmakingDataMessage) message);
            case MATCHMAKING_FAILED -> onMatchmakingFailed((MatchmakingFailedMessage) message);
            case GAME_DATA -> onMatchmakingCompleted((GameDataMessage) message);
        }
    }

    /**
     * Handles the matchmaking data message.
     * @param message
     */
    private void onMatchmakingData(MatchmakingDataMessage message) {
        if (isInMatchmaking) {
            playersInQueue = message.getNumPlayers();
            playersNeeded = message.getRequiredPlayers();

            Logger.info(LoggerCategory.SERVICE, "Matchmaking progress: %d/%d", playersInQueue, playersNeeded);
        }
    }

    /**
     * Handles the matchmaking failed message.
     * @param message
     */
    private void onMatchmakingFailed(MatchmakingFailedMessage message) {
        Logger.error(LoggerCategory.SERVICE, "Matchmaking failed!");
        reset();
    }

    /**
     * Handles the matchmaking completed message.
     */
    private void onMatchmakingCompleted(GameDataMessage message) {
        reset();
    }

    /**
     * Called when the client is connected to the server.
     */
    @Override
    public void onConnect() {

    }

    /**
     * Called when the client is disconnected from the server.
     */
    @Override
    public void onDisconnect() {
        reset();
    }

    /**
     * Joins the matchmaking queue.
     * @param matchSize The number of players that are required to start a match.
     */
    public void joinMatchmaking(int matchSize) {
        if (isInMatchmaking) {
            throw new IllegalStateException("Already in matchmaking.");
        }

        Logger.info(LoggerCategory.SERVICE, "Join the matchmaking for %d players battle.", matchSize);

        isInMatchmaking = true;
        client.getServerConnection().send(new JoinMatchmakingMessage(matchSize));
    }
}
