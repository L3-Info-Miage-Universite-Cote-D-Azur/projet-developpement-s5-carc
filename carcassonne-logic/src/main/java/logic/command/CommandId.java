package logic.command;

public enum CommandId {
    PLACE_TILE_DRAWN(1, PlaceTileDrawnCommand.class),
    PLACE_MEEPLE(2, PlaceMeepleCommand.class),
    REMOVE_MEEPLE(3, RemoveMeepleCommand.class),
    END_TURN(4, EndTurnCommand.class);

    private final int id;
    private final Class<? extends ICommand> commandClass;

    CommandId(int id, Class<? extends ICommand> commandClass) {
        this.id = id;
        this.commandClass = commandClass;
    }

    public int getId() {
        return id;
    }

    public Class<? extends ICommand> getCommandClass() {
        return commandClass;
    }

    public static CommandId getCommandId(int id) {
        for (CommandId commandId : CommandId.values()) {
            if (commandId.getId() == id) {
                return commandId;
            }
        }
        return null;
    }
}
