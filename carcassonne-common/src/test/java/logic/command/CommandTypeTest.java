package logic.command;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommandTypeTest {
    @Test
    public void testCommandByTypeNumber() {
        for (CommandType type : CommandType.values()) {
            assertEquals(type, CommandType.getCommandType(type.getValue()));
        }
    }
}
