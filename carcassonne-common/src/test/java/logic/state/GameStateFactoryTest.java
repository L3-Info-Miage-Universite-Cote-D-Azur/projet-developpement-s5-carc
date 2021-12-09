package logic.state;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GameStateFactoryTest {
    @Test
    void testCreateFromType() {
        for (GameStateType type : GameStateType.values()) {
            GameState gameState = GameStateFactory.createByType(type, null);

            assertNotNull(gameState);
            assertEquals(type, gameState.getType());
        }
    }
}
