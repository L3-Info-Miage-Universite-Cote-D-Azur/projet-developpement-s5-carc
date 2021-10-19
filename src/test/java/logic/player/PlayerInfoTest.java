package logic.player;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerInfoTest {
    @Test
    void testPlayerInfo() {
        int id = 568;
        PlayerInfo playerInfo = new PlayerInfo(id);

        assertEquals(id, playerInfo.getId());
    }
}
