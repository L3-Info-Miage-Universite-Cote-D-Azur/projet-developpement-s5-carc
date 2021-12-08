package client.message;

import network.message.Message;
import network.message.MessageType;
import network.message.connection.ClientHelloMessage;
import org.junit.jupiter.api.Test;

public class MessageDispatcherTest {
    @Test
    void testDispatchToHandler() {
        MessageDispatcher dispatcher = new MessageDispatcher();

        final boolean[] handled = {false,false,false};

        dispatcher.addHandler(new IMessageHandler() {
            @Override
            public void handleMessage(Message message) {
                handled[0] = message.getType() == MessageType.CLIENT_HELLO;
            }
        });
        dispatcher.addHandler(new IMessageHandler() {
            @Override
            public void handleMessage(Message message) {
                handled[1] = message.getType() == MessageType.CLIENT_HELLO;
            }
        });
        dispatcher.addHandler(new IMessageHandler() {
            @Override
            public void handleMessage(Message message) {
                handled[2] = message.getType() == MessageType.CLIENT_HELLO;
            }
        });

        dispatcher.handle(new ClientHelloMessage());
    }
}
