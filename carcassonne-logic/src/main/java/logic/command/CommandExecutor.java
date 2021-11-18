package logic.command;

import logic.Game;

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

    public ICommandExecutorListener getListener() {
        return listener;
    }

    public void setListener(ICommandExecutorListener listener) {
        this.listener = listener;
    }

    public boolean execute(ICommand command) {
        if (command.canBeExecuted(game)) {
            command.execute(game);
            listener.onCommandExecuted(command);
            return true;
        }

        return false;
    }
}
