package logic.command;

import logic.Game;
import logic.state.GameStateType;
import logic.state.turn.GameTurnPlaceMeepleState;
import stream.ByteInputStream;
import stream.ByteOutputStream;

public class SkipMeeplePlacementCommand implements ICommand {
    /**
     * Gets the command type.
     *
     * @return the command type
     */
    @Override
    public CommandType getType() {
        return CommandType.SKIP_MEEPLE_PLACEMENT;
    }

    /**
     * Encodes the command attributes to the output stream.
     *
     * @param stream the output stream
     */
    @Override
    public void encode(ByteOutputStream stream) {

    }

    /**
     * Decodes the command attributes from the input stream.
     *
     * @param stream the input stream
     */
    @Override
    public void decode(ByteInputStream stream) {

    }

    /**
     * Checks if the command is valid and can be executed.
     *
     * @return true if the command is valid
     */
    @Override
    public boolean canBeExecuted(Game game) {
        return true;
    }

    /**
     * Gets the game state required to execute the command.
     *
     * @return the game state
     */
    @Override
    public GameStateType getRequiredState() {
        return GameStateType.TURN_PLACE_MEEPLE;
    }

    /**
     * Executes the command.
     *
     * @param game the game context
     */
    @Override
    public void execute(Game game) {
        GameTurnPlaceMeepleState state = (GameTurnPlaceMeepleState) game.getState();
        state.complete();
    }
}
