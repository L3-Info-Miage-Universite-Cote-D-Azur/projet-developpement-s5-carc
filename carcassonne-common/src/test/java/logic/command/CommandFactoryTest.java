package logic.command;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CommandFactoryTest {
    @Test
    public void testCreateCommand() {
        for (CommandType type : CommandType.values()) {
            ICommand command = CommandFactory.create(type);
            assertNotNull(command);
            assertEquals(type, command.getType());
        }
    }
}
