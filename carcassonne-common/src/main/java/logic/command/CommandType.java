package logic.command;

/**
 * Enumeration of all possible commands with their type ids.
 */
public enum CommandType {
    PLACE_TILE_DRAWN,
    PLACE_MEEPLE,
    SKIP_MEEPLE_PLACEMENT,
    MASTER_NEXT_TURN_DATA,
    ROTATE_TILE_DRAWN,
    MOVE_DRAGON;
}
