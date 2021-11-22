package client.command;

import client.Client;
import client.logger.Logger;
import client.logger.LoggerCategory;
import client.network.ServerConnection;
import logic.command.ICommand;
import logic.command.ICommandExecutorListener;
import network.message.game.GameCommandRequestMessage;

/**
 * Class that notifies the server of a command execution.
 */
public class MasterCommandExecutionNotifier implements ICommandExecutorListener {
    private final Client client;

    public MasterCommandExecutionNotifier(Client client) {
        this.client = client;
    }

    /**
     * Notifies the master that a command has been executed.
     * @param command The command that was executed.
     */
    @Override
    public void onCommandExecuted(ICommand command) {
        client.getServerConnection().send(new GameCommandRequestMessage(command));
    }

    /**
     *
     * @param command
     * @param reason The reason of the failure.
     */
    @Override
    public void onCommandFailed(ICommand command, String reason) {
        Logger.warn(LoggerCategory.GAME, "Game: command failed: %s", reason);
    }

    @Override
    public void onCommandFailed(ICommand command, String reason, Object... args) {
        Logger.warn(LoggerCategory.GAME, "Command failed: %s", reason);
    }
}
