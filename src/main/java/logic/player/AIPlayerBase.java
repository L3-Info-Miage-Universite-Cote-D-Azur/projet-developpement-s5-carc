package logic.player;

import logic.board.GameBoard;
import logic.math.Vector2;
import logic.tile.TileData;
import logic.tile.TileFactory;

public abstract class AIPlayerBase extends PlayerBase {
    public AIPlayerBase(int id) {
        super(id);
    }

    @Override
    public void onTurn() {
        TileData tilePicked;
        do {
            tilePicked = game.getStack().remove();
        } while (!tryPlaceTile(tilePicked));
    }

    private boolean tryPlaceTile(TileData tileData) {
        GameBoard board = game.getBoard();
        Vector2 position = findFreePositionForTile(tileData);

        if (position == null) {
            return false;
        }

        board.place(TileFactory.create(tileData, position));
        return true;
    }

    public abstract Vector2 findFreePositionForTile(TileData tile);
}
