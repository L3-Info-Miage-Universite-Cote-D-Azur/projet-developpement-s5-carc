package server.matchmaking;

import org.junit.jupiter.api.Test;
import server.session.ClientSession;

import static org.junit.jupiter.api.Assertions.*;

public class MatchTest {
    @Test
    void destroyTest(){
        Match match = new Match(1, new ClientSession[0]);
        Match matchNull = new Match(1, new ClientSession[0]);
        matchNull.destroy();
        assertNotEquals(match, matchNull);
    }

    @Test
    void testGetId() {
        Match match = new Match(89, new ClientSession[0]);
        assertEquals(89,match.getId());
    }

    @Test
    void removePlayerTest() {

    }
}
