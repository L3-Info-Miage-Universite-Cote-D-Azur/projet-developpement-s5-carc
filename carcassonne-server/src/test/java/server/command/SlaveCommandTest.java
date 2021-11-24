package server.command;

import logic.Game;
import logic.config.GameConfig;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import server.matchmaking.Match;
import server.session.ClientSession;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled
public class SlaveCommandTest {
    private static final GameConfig config = GameConfig.loadFromResources();

    @Disabled //Doesn't work
     void TestCommand() {
        Game game = TestUtils.initGameEnv(2, true, true);
        sessions = new ClientSession[];
        match = new Match(8,);
        assertEquals(match, new SlaveCommandExecutionNotifier(match));
    }
}
