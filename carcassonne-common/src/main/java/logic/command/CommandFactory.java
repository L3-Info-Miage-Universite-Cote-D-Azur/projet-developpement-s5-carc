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
        return switch (type) {
            case PLACE_TILE_DRAWN -> new PlaceTileDrawnCommand();
            case PLACE_MEEPLE -> new PlaceMeepleCommand();
            case SKIP_MEEPLE_PLACEMENT -> new SkipMeeplePlacementCommand();
            case ROTATE_TILE_DRAWN -> new RotateTileDrawnCommand();
            case MOVE_DRAGON -> new MoveDragonCommand();
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }
}
