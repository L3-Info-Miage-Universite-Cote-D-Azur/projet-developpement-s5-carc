package logic.command;

import logic.Game;
import logic.board.GameBoard;
import logic.dragon.Dragon;
import logic.math.Vector2;
import logic.player.Player;
import logic.state.GameStateType;
import logic.state.turn.GameTurnExtraActionState;
import logic.state.turn.GameTurnPlaceTileState;
import logic.tile.Tile;
import logic.tile.TileFlags;
import logic.tile.chunk.Chunk;
import stream.ByteInputStream;
import stream.ByteOutputStream;

import java.util.Vector;

/**
 * Command to move dragon
 */
public class MoveDragonCommand implements ICommand {
    private Vector2 position;

    /**
     * Gets the command type
     *
     * @return the command type
     */
    @Override
    public CommandType getType() {
        return CommandType.MOVE_DRAGON;
    }

    /**
     * Encode the command attributes to the output stream
     *
     * @param stream the output stream
     */
    @Override
    public void encode(ByteOutputStream stream) {
        stream.writeInt(position.getX());
        stream.writeInt(position.getY());
    }

    /**
     * Decode the command attributes from the input stream.
     *
     * @param stream the input stream
     */
    @Override
    public void decode(ByteInputStream stream) {
        position = new Vector2(stream.readInt(), stream.readInt());
    }

    /**
     * Checks if the command is valid and can be executed.
     *
     * @param game
     * @return true if the command is valid
     */
    @Override
    public boolean canBeExecuted(Game game) {
        Dragon dragon = game.getBoard().getDragon();
        if(game.getBoard().hasDragon()){
            game.getCommandExecutor().getListener().onCommandFailed(this, "The dragon not exist yet.");
            return false;
        }
        Vector2 moveStep = position.subtract(dragon.getPosition());
        if(Math.abs(moveStep.getX() | moveStep.getY()) == 1){
            game.getCommandExecutor().getListener().onCommandFailed(this, "Cannot move the dragon so far.");
            return false;
        }

        return true;
    }

    /**
     * Gets the game state required to execute the command.
     *
     * @return the game state
     */
    @Override
    public GameStateType getRequiredState() {
        return GameStateType.TURN_MOVE_DRAGON;
    }

    /**
     * Executes the command.
     *
     * @param game The game context
     * @return True if the tile was placed, false otherwise
     */
    @Override
    public void execute(Game game) {
        Dragon dragon = game.getBoard().getDragon();

        dragon.moveTo(position);
    }
}
