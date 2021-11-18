package client.command;

import client.logger.Logger;
import client.network.ServerConnection;
import logic.command.ICommand;
import logic.command.ICommandExecutorListener;
import network.message.game.GameCommandRequestMessage;

/**
 * Class that notifies the server of a command execution.
 */
public class MasterCommandExecutionNotifier implements ICommandExecutorListener {
    private final ServerConnection connection;

    public MasterCommandExecutionNotifier(ServerConnection connection) {
        this.connection = connection;
    }

    /**
     * Notifies the master that a command has been executed.
     * @param command The command that was executed.
     */
    @Override
    public void onCommandExecuted(ICommand command) {
        Logger.debug("Game: command executed: %s", command.getClass().getSimpleName());
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
