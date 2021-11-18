package client.command;

import client.logger.Logger;
import logic.command.ICommand;
import logic.command.ICommandExecutorListener;

public class CommandLogger implements ICommandExecutorListener {
    /**
     * Logs the command execution.
     * @param command The command that was executed.
     */
    @Override
    public void onCommandExecuted(ICommand command) {

    }

    /**
     * Invoked when a command cannot be executed
     * @param command The command that was executed.
     * @param reason The reason of the failure.
     */
    @Override
    public void onCommandFailed(ICommand command, String reason) {
        Logger.warn("Game: command failed: %s", reason);
    }

    /**
     * Invoked when a command cannot be executed
     * @param command The command that was executed.
     * @param reason The reason of the failure.
     */
    @Override
    public void onCommandFailed(ICommand command, String reason, Object... args) {
        Logger.warn("Game: command failed: %s", reason);
    }
}
