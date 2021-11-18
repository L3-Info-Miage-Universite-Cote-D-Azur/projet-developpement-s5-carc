package network.message;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MessageTest {
    @Test
    public void testAllMessagesHaveDefaultContrustor() throws NoSuchMethodException {
        for (MessageType type : MessageType.values()) {
            assertNotNull(type.getMessageClass().getDeclaredConstructor());
        }
    }
}
