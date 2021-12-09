package server.matchmaking;

import logic.Game;
import logic.command.ICommand;
import logic.config.GameConfig;
import logic.player.Player;
import logic.tile.Tile;
import network.message.IMessage;
import network.message.game.GameCommandMessage;
import network.message.game.GameDataMessage;
import network.message.game.GameMasterNextTurnDataMessage;
import network.message.game.GameResultMessage;
import server.listener.MatchGameListener;
import server.logger.Logger;
import server.player.OfflinePlayerAI;
import server.session.ClientSession;
import stream.ByteOutputStream;

import java.util.Objects;

/**
 * Represents a game in the matchmaking system.
 */
public class Match {
    private final int id;
    private final ClientSession[] sessions;
    private final Game game;

    public Match(int id, ClientSession[] sessions) {
        this.id = id;
        this.sessions = sessions;
        this.game = new Game(Objects.requireNonNull(GameConfig.loadFromResources()));

        game.setListener(new MatchGameListener(this));

        for (ClientSession session : sessions) {
            game.addPlayer(new Player(session.getUserId()));
        }
    }

    /**
     * Destroys the match.
     */
    public void destroy() {
        for (ClientSession session : sessions) {
            if (session != null) {
                session.setMatch(null);
            }
        }
    }

    /**
     * Gets the match id.
     *
     * @return the match id
     */
    public int getId() {
        return id;
    }

    /**
     * Called when a player has been disconnected.
     *
     * @param session the player session
     */
    public void onPlayerDisconnected(ClientSession session) {
        for (int i = 0; i < sessions.length; i++) {
            if (sessions[i] == session) {
                sessions[i] = null;
            }
        }

        if (!game.isOver()) {
            Logger.info("Match %d: Player %d disconnected.", id, session.getUserId());

            Player player = game.getPlayerById(session.getUserId());
            player.setListener(new OfflinePlayerAI(game));

            if (game.isStarted() && game.getTurnExecutor() == player) {
                switch (game.getState().getType()) {
                    case TURN_PLACE_TILE -> player.getListener().onWaitingPlaceTile();
                    case TURN_PLACE_MEEPLE -> player.getListener().onWaitingMeeplePlacement();
                    case TURN_MOVE_DRAGON -> player.getListener().onWaitingDragonMove();
                    default -> { /* do nothing */ }
                }
            }
        }
    }

    /**
     * Sends a message to all connected clients.
     *
     * @param message
     */
    protected void sendMessageToConnectedClients(IMessage message) {
        for (ClientSession session : sessions) {
            if (session != null) {
                session.getConnection().send(message);
            }
        }
    }

    /**
     * Starts the match.
     */
    public void start() {
        game.start();
    }

    /**
     * Executes a command in the master game and notify the connected clients if successful.
     *
     * @param userId  the executor user id
     * @param command the command to execute
     */
    public void executeCommand(int userId, ICommand command) {
        Player commandExecutor = game.getPlayerById(userId);
        Player turnExecutor = game.getTurnExecutor();

        if (commandExecutor != turnExecutor) {
            Logger.warn("Player %d tried to execute command %s but it is not his turn.", userId, command.getType());
            return;
        }

        if (game.getState().getType() != command.getRequiredState()) {
            Logger.warn("Player %d tried to execute command %s but the game state is %s.", userId, command.getType(), game.getState().getType());
            return;
        }

        if (command.canBeExecuted(game) != ICommand.ERROR_SUCCESS) {
            Logger.warn("Player %d tried to execute command %s but it is not executable.", userId, command.getType());
            return;
        }

        Logger.debug("Player %d executed command %s", userId, command.getType());

        game.executeCommand(command);
    }

    /**
     * Creates a snapshot of the game instance.
     *
     * @return the snapshot
     */
    private byte[] createSnapshot(boolean masterData) {
        ByteOutputStream stream = new ByteOutputStream(1024);
        game.encode(stream, masterData);
        return stream.toByteArray();
    }

    /**
     * Called when a command has been executed successfully.
     * It notifies the connected clients that the command has been executed.
     *
     * @param command the executed command.
     */
    public void onCommandExecuted(ICommand command) {
        sendMessageToConnectedClients(new GameCommandMessage(command));
    }

    /**
     * Called when the game has been started.
     * It sends to the connected clients the game snapshot.
     */
    public void onGameStarted() {
        sendMessageToConnectedClients(new GameDataMessage(createSnapshot(false)));
    }

    /**
     * Called when the game has been ended.
     * It sends to the connected clients the game result and destroys the references to this match.
     */
    public void onGameEnded() {
        destroy();
        sendMessageToConnectedClients(new GameResultMessage(createSnapshot(true)));
    }

    /**
     * Called when a turn is started.
     *
     * @param tileDrawn the drawn tile
     */
    public void onTurnStarted(Tile tileDrawn) {
        sendMessageToConnectedClients(new GameMasterNextTurnDataMessage(game.getConfig().getTiles().indexOf(tileDrawn.getConfig())));
    }
}
