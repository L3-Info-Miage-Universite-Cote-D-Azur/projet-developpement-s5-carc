package logic.command;

/**
 * Enumeration of all possible commands with their type ids.
 */
public enum CommandType {
    PLACE_TILE_DRAWN(1, PlaceTileDrawnCommand.class),
    PLACE_MEEPLE(2, PlaceMeepleCommand.class),
    REMOVE_MEEPLE(3, RemoveMeepleCommand.class),
    END_TURN(4, EndTurnCommand.class),
    MASTER_NEXT_TURN_DATA(5, MasterNextTurnDataCommand.class);

    private final int id;
    private final Class<? extends ICommand> commandClass;

    CommandType(int id, Class<? extends ICommand> commandClass) {
        this.id = id;
        this.commandClass = commandClass;
    }

    public int getValue() {
        return id;
    }

    public Class<? extends ICommand> getCommandClass() {
        return commandClass;
    }

    public static CommandType getCommandType(int id) {
        for (CommandType commandId : CommandType.values()) {
            if (commandId.getValue() == id) {
                return commandId;
            }
        }
        return null;
    }
}
