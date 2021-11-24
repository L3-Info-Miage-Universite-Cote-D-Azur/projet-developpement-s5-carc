package logic.command;

import logic.Game;

/**
 * A class that allows to centralise the execution of commands.
 */
public class CommandExecutor {
    private Game game;
    private ICommandExecutorListener listener;

    public CommandExecutor(Game game) {
        this.game = game;
        this.listener = new ICommandExecutorListener() {
            @Override
            public void onCommandExecuted(ICommand command) {

            }

            @Override
            public void onCommandFailed(ICommand command, String reason) {

            }

            @Override
            public void onCommandFailed(ICommand command, String reason, Object... args) {

            }
        };
    }

    /**
     * Gets the listener of the command executor.
     * @return the listener of the command executor.
     */
    public ICommandExecutorListener getListener() {
        return listener;
    }

    /**
     * Sets the listener of the command executor.
     * @param listener the listener of the command executor.
     */
    public void setListener(ICommandExecutorListener listener) {
        this.listener = listener;
    }

    /**
     * Executes the given command and notifies the listener if the command has been executed or not.
     * @param command the command to execute.
     * @return true if the command was executed, false otherwise.
     */
    public boolean execute(ICommand command) {
        if (command.getRequiredState() == game.getState().getType() && command.canBeExecuted(game)) {
            listener.onCommandExecuted(command);
            command.execute(game);
            return true;
        }

        return false;
    }
}
