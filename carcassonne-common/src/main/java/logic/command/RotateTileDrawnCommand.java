package logic.command;

import logic.Game;
import logic.state.GameStateType;
import logic.state.turn.GameTurnPlaceTileState;
import logic.tile.TileRotation;
import stream.ByteInputStream;
import stream.ByteOutputStream;

/**
 * Commands that rotate the tile drawn by the player.
 */
public class RotateTileDrawnCommand implements ICommand {
    private TileRotation rotation;

    public RotateTileDrawnCommand() {
    }

    public RotateTileDrawnCommand(TileRotation rotation) {
        this.rotation = rotation;
    }

    /**
     * Gets the command type.
     * @return the command type
     */
    @Override
    public CommandType getType() {
        return CommandType.ROTATE_TILE_DRAWN;
    }
    /**
     * Encodes the command attributes to the output stream.
     * @param stream the output stream
     */
    @Override
    public void encode(ByteOutputStream stream) {
        stream.writeInt(rotation.ordinal());
    }

    /**
     * Decodes the command attributes from the input stream.
     * @param stream the input stream
     */
    @Override
    public void decode(ByteInputStream stream) {
        rotation = TileRotation.values()[stream.readInt()];
    }

    /**
     * Checks if the command is valid and can be executed.
     * @return true if the command is valid
     */
    @Override
    public boolean canBeExecuted(Game game) {
        return rotation != null;
    }
    /**
     * Gets the game state required to execute the command.
     * @return the game state
     */
    @Override
    public GameStateType getRequiredState() {
        return GameStateType.TURN_PLACE_TILE;
    }
    /**
     * Executes the command.
     * @param game the game context
     */
    @Override
    public void execute(Game game) {
        GameTurnPlaceTileState placeTileState = (GameTurnPlaceTileState) game.getState();
        placeTileState.getTileDrawn().setRotation(rotation);
    }
}
