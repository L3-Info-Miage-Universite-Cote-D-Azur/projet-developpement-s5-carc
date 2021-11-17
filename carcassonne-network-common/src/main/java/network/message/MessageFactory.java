package network.message;

public class MessageFactory {
    public static Message create(MessageId id) {
        try {
            return id.getMessageClass().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
