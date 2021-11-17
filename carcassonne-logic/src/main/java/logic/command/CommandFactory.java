package logic.command;

public class CommandFactory {
    public static ICommand create(CommandId commandId) {
        try {
            return commandId.getCommandClass().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
