package logic.player;

import logger.Logger;
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
        Vector2 position = findPositionForTile(tile);

        if (position == null) {
            Logger.info("[AI] No point available to place tile %s", tile);
            return false;
        }

        Logger.info(String.format("[AI] Player %d places the tile %s to %s", getId(), tile, position));

        tile.setPosition(position);
        board.place(tile);

        return true;
    }

    public abstract Vector2 findPositionForTile(Tile tile);
}
