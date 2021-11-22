package logic.state.turn;

import logic.Game;
import logic.state.GameState;
import logic.state.GameStateType;
import stream.ByteInputStream;
import stream.ByteOutputStream;

public class GameTurnWaitingMasterDataState extends GameState {
    public GameTurnWaitingMasterDataState(Game game) {
        super(game);
    }

    @Override
    public void init() {

    }

    @Override
    public void encode(ByteOutputStream stream) {

    }

    @Override
    public void decode(ByteInputStream stream) {

    }

    @Override
    public void complete() {
        game.setState(new GameTurnEndingState(game));
    }

    @Override
    public GameStateType getType() {
        return GameStateType.TURN_WAITING_MASTER_DATA;
    }
}
