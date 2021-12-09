package network.message;

/**
 * The MessageFactory class is used to create messages.
 */
public class MessageFactory {
    private MessageFactory() {
        // ignored
    }

    /**
     * Creates a message instance from the given message type.
     *
     * @param type The message type.
     * @return The message instance.
     */
    public static IMessage create(MessageType type) {
        try {
            return type.getMessageClass().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
