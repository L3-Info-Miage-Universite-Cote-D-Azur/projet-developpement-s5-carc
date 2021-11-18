package server.command;

import logic.command.ICommand;
import logic.command.ICommandExecutorListener;
import server.matchmaking.Match;

/**
 * Class that notifies the slaves of the execution of a command.
 */
public class SlaveCommandExecutionNotifier implements ICommandExecutorListener {
    private final Match match;

    public SlaveCommandExecutionNotifier(Match match) {
        this.match = match;
    }

    @Override
    public void onCommandExecuted(ICommand command) {
        match.notifyCommandExecutionToConnectedClients(command);
    }

    @Override
    public void onCommandFailed(ICommand command, String reason) {

    }

    @Override
    public void onCommandFailed(ICommand command, String reason, Object... args) {

    }
}
