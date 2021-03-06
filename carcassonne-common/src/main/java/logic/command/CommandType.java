package logic.command;

/**
 * Enumeration of all possible commands with their type ids.
 */
public enum CommandType {
    PLACE_TILE_DRAWN,
    PLACE_MEEPLE,
    PLACE_FAIRY,
    REMOVE_MEEPLE,
    SKIP_MEEPLE_PLACEMENT,
    ROTATE_TILE_DRAWN,
    MOVE_DRAGON;
}
