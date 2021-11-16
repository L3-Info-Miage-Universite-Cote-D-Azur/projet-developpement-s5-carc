package logic.player;

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
            if (game.getStack().isEmpty()) {
                break;
            }

            tilePicked = game.getStack().remove();
            tilePosition = findPositionForTile(tilePicked);
        } while (tilePosition == null || !new PlaceTileCommand(tilePicked, tilePosition).execute(game));

        placeMeepleIfNeeded();
    }

    public abstract Vector2 findPositionForTile(Tile tile);
    public abstract void placeMeepleIfNeeded();
}
