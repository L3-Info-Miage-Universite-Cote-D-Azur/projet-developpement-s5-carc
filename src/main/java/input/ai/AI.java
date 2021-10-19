package input.ai;

import input.PlayerInput;
import logic.Game;
import logic.board.GameBoard;
import logic.tile.TileData;

public abstract class AI implements PlayerInput {
    private Game game;

    @Override
    public void onTurn() {
        TileData tilePicked = pickFromStack();

        placeTile(tilePicked, game.getGameBoard());
    }

    @Override
    public void setGame(Game game) {
        this.game = game;
    }

    public abstract void placeTile(TileData data, GameBoard board);

    public TileData pickFromStack() {
        return game.getStack().pick();
    }
}
