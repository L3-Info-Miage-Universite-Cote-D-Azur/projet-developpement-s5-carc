package logic.state.turn;

import logic.Game;
import logic.state.GameState;
import logic.state.GameStateType;
import logic.tile.Tile;
import stream.ByteInputStream;
import stream.ByteOutputStream;
import stream.ByteStreamHelper;

/**
 * Represents the state of a game turn when a player is placing a tile.
 */
public class GameTurnPlaceTileState extends GameState {
    private Tile tileDraw;

    public GameTurnPlaceTileState(Game game) {
        super(game);
    }

    public GameTurnPlaceTileState(Game game, Tile tileDraw) {
        super(game);
        this.tileDraw = tileDraw;
    }

    @Override
    public void init() {
        game.getTurnExecutor().getListener().onWaitingPlaceTile();
    }

    @Override
    public void encode(ByteOutputStream stream) {
        ByteStreamHelper.encodeTile(stream, tileDraw, game);
    }

    @Override
    public void decode(ByteInputStream stream) {
        tileDraw = ByteStreamHelper.decodeTile(stream, game);
    }

    /**
     * Completes the state by starting the {@link GameTurnExtraActionState}.
     */
    @Override
    public void complete() {
        game.setState(new GameTurnExtraActionState(game));
    }

    /**
     * Returns the type of this state.
     * @return The type of this state.
     */
    @Override
    public GameStateType getType() {
        return GameStateType.TURN_PLACE_TILE;
    }

    /**
     * Returns the tile that is being drawn.
     * @return The tile that is being drawn.
     */
    public Tile getTileDrawn() {
        return tileDraw;
    }
}
