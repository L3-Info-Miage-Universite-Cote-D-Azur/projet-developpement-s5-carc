package server.matchmaking;

import logic.Game;
import logic.GameTurn;
import logic.IGameListener;
import logic.command.*;
import logic.config.GameConfig;
import logic.player.IPlayerListener;
import logic.player.Player;
import logic.tile.ChunkId;
import logic.tile.Tile;
import network.message.Message;
import network.message.game.GameCommandMessage;
import network.message.game.GameDataMessage;
import network.message.game.GameResultMessage;
import server.logger.Logger;
import server.session.ClientSession;
import stream.ByteOutputStream;

public class Match implements IGameListener, ICommandExecutorListener {
    private final int id;
    private final ClientSession[] sessions;
    private final Game game;

    public Match(int id, ClientSession[] sessions) {
        this.id = id;
        this.sessions = sessions;
        this.game = new Game(GameConfig.loadFromResources());
        game.setListener(this);
        game.setMaster(true);
        game.getCommandExecutor().setListener(this);

        for (ClientSession session : sessions) {
            game.addPlayer(new Player(session.getUserId()));
        }
    }

    public void removePlayer(ClientSession session) {
        for (int i = 0; i < sessions.length; i++) {
            if (sessions[i] == session) {
                sessions[i] = null;
            }
        }

        if (!game.isOver()) {
            autoPlayIfCurrentPlayerIsOffline();
        }
    }

    public void startGame() {
        game.start();
    }

    private ClientSession getSessionByUserId(int userId) {
        for (ClientSession session : sessions) {
            if (session != null && session.getUserId() == userId) {
                return session;
            }
        }
        return null;
    }

    private void sendMessageToConnectedClients(Message message) {
        for (ClientSession session : sessions) {
            if (session != null) {
                session.getConnection().send(message);
            }
        }
    }

    public void executeCommand(int userId, ICommand command) {
        GameTurn turn = game.getTurn();

        if (turn.isOver()) {
            Logger.warn("Player %d tried to execute command %s but the current turn is over.", userId, command);
            return;
        }

        if (turn.getPlayer().getId() != userId) {
            Logger.warn("Player %d tried to execute command %s but it is not his turn", userId, command.getType());
            return;
        }

        if (!command.canBeExecuted(game)) {
            Logger.warn("Player %d tried to execute command %s but it is not allowed", userId, command.getType());
            return;
        }

        Logger.info("Player %d executed command %s", userId, command.getType());

        game.getCommandExecutor().execute(command);
    }

    private void notifyCommandExecutionToConnectedClients(ICommand command) {
        sendMessageToConnectedClients(new GameCommandMessage(command));
    }

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
        sendMessageToConnectedClients(new GameResultMessage());

        for (ClientSession session : sessions) {
            if (session != null) {
                session.setMatch(null);
            }
        }
    }

    @Override
    public void onCommandExecuted(ICommand command) {
        notifyCommandExecutionToConnectedClients(command);
    }

    @Override
    public void onCommandFailed(ICommand command, String reason) {
        Logger.warn("Match %d: Command failed: %s", id, reason);
    }

    @Override
    public void onCommandFailed(ICommand command, String reason, Object... args) {
        Logger.warn("Match %d: Command failed: %s", id, String.format(reason, args));
    }
}