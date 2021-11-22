package logic.state;

import logic.Game;
import logic.state.turn.GameTurnEndingState;
import logic.state.turn.GameTurnExtraActionState;
import logic.state.turn.GameTurnInitState;
import logic.state.turn.GameTurnPlaceTileState;

public class GameStateFactory {
    public static GameState createByType(GameStateType type, Game game) {
        return switch (type) {
            case START -> new GameStartState(game);
            case TURN_INIT -> new GameTurnInitState(game);
            case TURN_PLACE_TILE -> new GameTurnPlaceTileState(game);
            case TURN_EXTRA_ACTION -> new GameTurnExtraActionState(game);
            case TURN_ENDING -> new GameTurnEndingState(game);
            case OVER -> new GameOverState(game);
            default -> throw new IllegalArgumentException("Unknown game state type: " + type);
        };
    }
}
