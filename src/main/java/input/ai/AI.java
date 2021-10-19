package input.ai;

import input.PlayerInput;
import logic.Game;
import logic.tile.TileData;

public abstract class AI implements PlayerInput {
    protected Game game;

    @Override
    public void onTurn() {
        TileData tilePicked;
        do {
            tilePicked = pickFromStack();
        } while (!placeTile(tilePicked));
    }

    @Override
    public void setGame(Game game) {
        this.game = game;
    }

    public abstract boolean placeTile(TileData data);

    public TileData pickFromStack() {
        return game.getStack().pick();
    }
}
