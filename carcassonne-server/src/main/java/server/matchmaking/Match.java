package server.matchmaking;

import logic.Game;
import logic.GameTurn;
import logic.IGameListener;
import logic.command.EndTurnCommand;
import logic.command.ICommand;
import logic.command.MasterTurnStartedCommand;
import logic.command.PlaceTileDrawnCommand;
import logic.config.GameConfig;
import logic.player.Player;
import logic.tile.ChunkId;
import logic.tile.Tile;
import network.message.Message;
import network.message.game.GameCommandMessage;
import network.message.game.GameDataMessage;
import network.message.game.GameResultMessage;
import server.command.SlaveCommandExecutionNotifier;
import server.logger.Logger;
import server.session.ClientSession;
import stream.ByteOutputStream;

/**
 * Represents a game in the matchmaking system.
 */
public class Match implements IGameListener {
    private final int id;
    private final ClientSession[] sessions;
    private final Game game;

    public Match(int id, ClientSession[] sessions) {
        this.id = id;
        this.sessions = sessions;
        this.game = new Game(GameConfig.loadFromResources());
        game.setListener(this);
        game.getCommandExecutor().setListener(new SlaveCommandExecutionNotifier(this));

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
     * Removes the player from the connected sessions.
     * @param session the session to remove
     */
    public void removePlayer(ClientSession session) {
        for (int i = 0; i < sessions.length; i++) {
            if (sessions[i] == session) {
                sessions[i] = null;
            }
        }

        if (game.isStarted() && !game.isOver()) {
            autoPlayIfCurrentPlayerIsOffline();
        }
    }

    /**
     * Starts the match.
     */
    public void startGame() {
        game.start();
    }

    /**
     * Gets the session by the user id.
     * @param userId
     * @return
     */
    private ClientSession getSessionByUserId(int userId) {
        for (ClientSession session : sessions) {
            if (session != null && session.getUserId() == userId) {
                return session;
            }
        }
        return null;
    }

    /**
     * Sends a message to all connected clients.
     * @param message
     */
    private void sendMessageToConnectedClients(Message message) {
        for (ClientSession session : sessions) {
            if (session != null) {
                session.getConnection().send(message);
            }
        }
    }

    /**
     * Executes a command in the master game and notify the connected clients if successful.
     * @param userId the executor user id
     * @param command the command to execute
     */
    public void executeCommand(int userId, ICommand command) {
        GameTurn turn = game.getTurn();

        if (turn.isOver()) {
            Logger.warn("Player %d tried to execute command %s but the current turn is over.", userId, command);
            return;
        }

        if (turn.getPlayer().getId() != userId) {
            Logger.warn("Player %d tried to execute command %s but it is not his turn.", userId, command.getType());
            return;
        }

        if (!command.canBeExecuted(game)) {
            Logger.warn("Player %d tried to execute command %s but it is not executable.", userId, command.getType());
            return;
        }

        Logger.info("Player %d executed command %s", userId, command.getType());

        game.getCommandExecutor().execute(command);
    }

    /**
     * Notifies the connected clients that the command has been executed.
     * @param command the executed command
     */
    public void notifyCommandExecutionToConnectedClients(ICommand command) {
        sendMessageToConnectedClients(new GameCommandMessage(command));
    }

    /**
     * Auto plays the game if the current player who must play is offline.
     */
    private void autoPlayIfCurrentPlayerIsOffline() {
        GameTurn turn = game.getTurn();
        int userId = turn.getPlayer().getId();

        if (getSessionByUserId(userId) == null) {
            Logger.info("Player %d left the game. Tile to draw will be placed randomly to continue the game.",userId);
            executeCommand(userId, new PlaceTileDrawnCommand(game.getBoard().findFreePlaceForTile(turn.getTileToDraw()).get(0)));
            executeCommand(userId, new EndTurnCommand());
        }
    }

    @Override
    public void onTurnStarted(int id) {
        Logger.info("Match %d: Turn %d started", this.id, id);

        notifyCommandExecutionToConnectedClients(new MasterTurnStartedCommand(game.getTurn().getTileToDraw(), game));
        autoPlayIfCurrentPlayerIsOffline();
    }

    @Override
    public void onTurnEnded(int id) {
        Logger.info("Match %d: Turn %d ended", this.id, id);
    }

    @Override
    public void onTilePlaced(Tile tile) {
        Logger.debug("Match %d: Tile model %s placed at (%d,%d)", id, tile.getConfig().model, tile.getPosition().getX(), tile.getPosition().getY());
    }

    @Override
    public void onMeeplePlaced(Player player, Tile tile, ChunkId chunkId) {
        Logger.debug("Match %d: Meeple placed at tile (%d,%d), chunk %s", id, tile.getPosition().getX(), tile.getPosition().getY(), chunkId);
    }

    @Override
    public void onMeepleRemoved(Player player, Tile tile, ChunkId chunkId) {
        Logger.debug("Match %d: Meeple removed from tile (%d,%d), chunk %s", id, tile.getPosition().getX(), tile.getPosition().getY(), chunkId);
    }

    @Override
    public void onStart() {
        Logger.info("Match %d: Game started", id);

        ByteOutputStream stream = new ByteOutputStream(1000);
        game.encode(stream, false);
        sendMessageToConnectedClients(new GameDataMessage(stream.toByteArray()));
    }

    @Override
    public void onEnd() {
        Logger.info("Match %d: Game ended", id);

        ByteOutputStream stream = new ByteOutputStream(1000);
        game.encode(stream, true);

        destroy();
        sendMessageToConnectedClients(new GameResultMessage(stream.toByteArray()));
    }
}
