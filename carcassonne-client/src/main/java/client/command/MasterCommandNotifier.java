package client.command;

import client.logger.Logger;
import logic.command.ICommand;
import logic.command.ICommandExecutorListener;
import client.network.ServerConnection;
import network.message.game.GameCommandRequestMessage;

public class MasterCommandNotifier implements ICommandExecutorListener {
    private final ServerConnection connection;

    public MasterCommandNotifier(ServerConnection connection) {
        this.connection = connection;
    }

    /**
     * Notifies the master that a command has been executed.
     * @param command The command that was executed.
     */
    @Override
    public void onCommandExecuted(ICommand command) {
        connection.send(new GameCommandRequestMessage(command));
    }

    /**
     *
     * @param command
     * @param reason The reason of the failure.
     */
    @Override
    public void onCommandFailed(ICommand command, String reason) {
        Logger.warn("Game: command failed: %s", reason);
    }

    @Override
    public void onCommandFailed(ICommand command, String reason, Object... args) {
        Logger.warn("Game: command failed: %s", reason);
    }
}
