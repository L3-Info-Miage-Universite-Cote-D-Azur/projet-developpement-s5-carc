package network.message;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {
    @Test
    public void testAllMessagesHaveDefaultConstructor() throws NoSuchMethodException {
        for (MessageType type : MessageType.values()) {
            assertNotNull(type.getMessageClass().getDeclaredConstructor());
        }
    }

    @Test
    public void testFactoryReturnsCorrectMessage() {
        for (MessageType type : MessageType.values()) {
            Message message = MessageFactory.create(type);
            assertNotNull(message);
            assertEquals(type, message.getType());
            assertEquals(type.getMessageClass(), message.getClass());
        }
    }
}