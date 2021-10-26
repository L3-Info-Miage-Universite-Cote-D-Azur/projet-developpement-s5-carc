package logic.player;

import logic.board.GameBoard;
import logic.math.Vector2;
import logic.tile.Tile;

public abstract class AIPlayerBase extends PlayerBase {
    public AIPlayerBase(int id) {
        super(id);
    }

    @Override
    public void onTurn() {
        Tile tilePicked;
        do {
            tilePicked = game.getStack().remove();
        } while (!tryPlaceTile(tilePicked));
    }

    private boolean tryPlaceTile(Tile tile) {
        GameBoard board = game.getBoard();
        Vector2 position = findFreePositionForTile(tile);

        if (position == null) {
            return false;
        }

        tile.setPosition(position);
        board.place(tile);
        return true;
    }

    public abstract Vector2 findFreePositionForTile(Tile tile);
}
