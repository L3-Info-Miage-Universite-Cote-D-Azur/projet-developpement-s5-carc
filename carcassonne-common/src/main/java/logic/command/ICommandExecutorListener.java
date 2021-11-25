package logic.command;

public interface ICommandExecutorListener {
    /**
     * Called when a command execution was successful.
     *
     * @param command The command that was executed.
     */
    void onCommandExecuted(ICommand command);

    /**
     * Called when a command execution was failed.
     *
     * @param reason The reason of the failure.
     */
    void onCommandFailed(ICommand command, String reason);

    /**
     * Called when a command execution was failed.
     *
     * @param reason The reason of the failure.
     * @param args   The arguments of the failure.
     */
    void onCommandFailed(ICommand command, String reason, Object... args);
}
