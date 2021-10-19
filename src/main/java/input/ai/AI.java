package input.ai;

import input.PlayerInput;
import logic.Game;
import logic.player.Player;
import logic.tile.TileData;

public abstract class AI implements PlayerInput {
    protected Player player;

    @Override
    public void onTurn() {
        TileData tilePicked;
        do {
            tilePicked = pickFromStack();
        } while (!placeTile(tilePicked));
    }

    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }

    public abstract boolean placeTile(TileData data);

    public TileData pickFromStack() {
        return player.getGame().getStack().pick();
    }
}
