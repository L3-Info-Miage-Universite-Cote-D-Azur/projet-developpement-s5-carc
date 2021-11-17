package logic.command;

import logic.Game;
import logic.GameTurn;
import stream.ByteInputStream;
import stream.ByteOutputStream;

/**
 * Command to end the turn.
 */
public class EndTurnCommand implements ICommand {
    @Override
    public CommandId getId() {
        return CommandId.END_TURN;
    }

    @Override
    public void encode(ByteOutputStream stream) {
    }

    @Override
    public void decode(ByteInputStream stream) {
    }

    /**
     * Executes the command to end the turn.
     * @param game the game context
     * @return true if the turn was ended, false otherwise
     */
    @Override
    public boolean execute(Game game) {
        GameTurn turn = game.getTurn();

        if (turn.isOver()) {
            game.getListener().onCommandFailed("Turn is already over!");
            return false;
        }

        if (!turn.hasPlacedTile()) {
            game.getListener().onCommandFailed("You must place a tile before ending your turn!");
        }

        turn.endTurn();

        return false;
    }
}
