package logic.command;

/**
 * Class that creates commands from the given command type.
 */
public class CommandFactory {
    /**
     * Creates a command from the given command type.
     *
     * @param type the command type
     * @return the command
     */
    public static ICommand create(CommandType type) {
        try {
            return type.getCommandClass().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
