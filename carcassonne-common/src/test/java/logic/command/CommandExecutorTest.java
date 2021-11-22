package logic.command;

import logic.Game;
import logic.TestUtils;
import logic.state.GameStateType;
import org.junit.jupiter.api.Test;
import stream.ByteInputStream;
import stream.ByteOutputStream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommandExecutorTest {
    @Test
    void testExecuteCommand() {
        CommandExecutor executor = TestUtils.initGameEnv(2, false, false).getCommandExecutor();

        assertTrue(executor.execute(new FakeSuccessfulCommand()));
        assertFalse(executor.execute(new FakeFailingCommand()));
    }

    @Test
    void testListenerCallback() {
        CommandExecutor executor = TestUtils.initGameEnv(2, false, false).getCommandExecutor();

        final boolean[] commandExecuted = {false};
        final boolean[] commandFailed = {false};

        executor.setListener(new ICommandExecutorListener() {
            @Override
            public void onCommandExecuted(ICommand command) {
                commandExecuted[0] = true;
            }

            @Override
            public void onCommandFailed(ICommand command, String reason) {
                commandFailed[0] = true;
            }

            @Override
            public void onCommandFailed(ICommand command, String reason, Object... args) {
                commandFailed[0] = true;
            }
        });

        executor.execute(new FakeSuccessfulCommand());

        assertTrue(commandExecuted[0]);
        assertFalse(commandFailed[0]);
        commandExecuted[0] = false;

        executor.execute(new FakeFailingCommand());

        assertFalse(commandExecuted[0]);
        assertTrue(commandFailed[0]);
        commandFailed[0] = false;
    }

    private class FakeSuccessfulCommand implements ICommand {
        @Override
        public CommandType getType() {
            return null;
        }

        @Override
        public void encode(ByteOutputStream stream) {

        }

        @Override
        public void decode(ByteInputStream stream) {

        }

        @Override
        public boolean canBeExecuted(Game game) {
            return true;
        }

        @Override
        public GameStateType getRequiredState() {
            return null;
        }

        @Override
        public void execute(Game game) {

        }
    }

    private class FakeFailingCommand implements ICommand {
        @Override
        public CommandType getType() {
            return null;
        }

        @Override
        public void encode(ByteOutputStream stream) {

        }

        @Override
        public void decode(ByteInputStream stream) {

        }

        @Override
        public boolean canBeExecuted(Game game) {
            game.getCommandExecutor().getListener().onCommandFailed(this, "rip");
            return false;
        }

        @Override
        public GameStateType getRequiredState() {
            return null;
        }

        @Override
        public void execute(Game game) {

        }
    }
}
