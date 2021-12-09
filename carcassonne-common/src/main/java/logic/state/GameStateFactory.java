package logic.state;

import logic.Game;
import logic.state.turn.*;

public class GameStateFactory {
    private GameStateFactory() {
        // ignored
    }

    public static GameState createByType(GameStateType type, Game game) {
        return switch (type) {
            case START -> new GameStartState(game);
            case TURN_INIT -> new GameTurnInitState(game);
            case TURN_PLACE_TILE -> new GameTurnPlaceTileState(game);
            case TURN_PLACE_MEEPLE -> new GameTurnPlaceMeepleState(game);
            case TURN_MOVE_DRAGON -> new GameTurnMoveDragonState(game);
            case TURN_WAITING_MASTER_DATA -> new GameWaitingMasterDataState(game);
            case TURN_ENDING -> new GameTurnEndingState(game);
            case OVER -> new GameOverState(game);
            default -> throw new IllegalArgumentException("Unknown game state type: " + type);
        };
    }
}
