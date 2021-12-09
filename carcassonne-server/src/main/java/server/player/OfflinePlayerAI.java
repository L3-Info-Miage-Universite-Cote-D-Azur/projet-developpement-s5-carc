package server.player;

import logic.Game;
import logic.command.MoveDragonCommand;
import logic.command.PlaceTileDrawnCommand;
import logic.command.RotateTileDrawnCommand;
import logic.command.SkipMeeplePlacementCommand;
import logic.dragon.Dragon;
import logic.math.Vector2;
import logic.player.IPlayerListener;
import logic.state.turn.GameTurnPlaceTileState;
import logic.tile.Direction;
import logic.tile.Tile;
import logic.tile.TileRotation;

import java.util.List;

public class OfflinePlayerAI implements IPlayerListener {
    private final Game game;

    public OfflinePlayerAI(Game game) {
        this.game = game;
    }

    @Override
    public void onWaitingPlaceTile() {
        GameTurnPlaceTileState state = (GameTurnPlaceTileState) game.getState();
        Tile tileDrawn = state.getTileDrawn();

        List<Vector2> freeTiles = null;

        for (TileRotation rotation : TileRotation.values()) {
            if (rotation != tileDrawn.getRotation()) {
                game.executeCommand(new RotateTileDrawnCommand(rotation));
            }

            freeTiles = game.getBoard().findFreePlacesForTile(tileDrawn);

            if (!freeTiles.isEmpty()) {
                break;
            }
        }

        assert freeTiles != null;
        game.executeCommand(new PlaceTileDrawnCommand(freeTiles.get(0)));
    }

    @Override
    public void onWaitingMeeplePlacement() {
        game.executeCommand(new SkipMeeplePlacementCommand());
    }

    @Override
    public void onWaitingDragonMove() {
        Dragon dragon = game.getBoard().getDragon();

        for (Direction direction : Direction.values()) {
            Vector2 position = dragon.getPosition().add(direction.value());

            if (dragon.canMoveTo(position)) {
                game.executeCommand(new MoveDragonCommand(direction));
                break;
            }
        }
    }
}
