package logic.player;

import logger.Logger;
import logic.board.GameBoard;
import logic.command.PlaceTileCommand;
import logic.math.Vector2;
import logic.tile.Tile;

public abstract class AIPlayerBase extends PlayerBase {
    public AIPlayerBase(int id) {
        super(id);
    }

    @Override
    public void onTurn() {
        Tile tilePicked;
        Vector2 tilePosition;
        do {
            tilePicked = game.getStack().remove();
            tilePosition = findPositionForTile(tilePicked);
        } while (tilePosition == null || !new PlaceTileCommand(tilePicked, tilePosition).execute(game));
    }

    public abstract Vector2 findPositionForTile(Tile tile);
}
