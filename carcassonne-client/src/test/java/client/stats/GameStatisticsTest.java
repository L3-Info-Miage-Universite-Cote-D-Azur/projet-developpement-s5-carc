package client.stats;

import excel.ExcelNode;
import logic.Game;
import logic.config.GameConfig;
import logic.player.Player;
import logic.state.GameOverState;
import logic.tile.chunk.ChunkType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reflection.ReflectionUtils;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GameStatisticsTest {
    @Test
    void appendTest() {
        GameStatistics gameStatistics = new GameStatistics();

        Game game1 = new Game(GameConfig.loadFromResources());
        Game game2 = new Game(GameConfig.loadFromResources());

        game1.setState(new GameOverState(game1));
        game2.setState(new GameOverState(game2));

        Random random = new Random();

        ArrayList<Player> players = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Player player = new Player(i + 1);

            player.addScore(random.nextInt(100), ChunkType.ROAD);
            player.addScore(random.nextInt(100), ChunkType.TOWN);
            player.addScore(random.nextInt(100), ChunkType.ABBEY);
            player.addScore(random.nextInt(100), ChunkType.FIELD);

            if (i % 2 == 0) {
                game1.addPlayer(player);
            } else {
                game2.addPlayer(player);
            }

            players.add(player);
        }

        gameStatistics.append(game1);
        gameStatistics.append(game1);
        gameStatistics.append(game2);
        gameStatistics.append(game2);

        Player winnerPlayer = null;

        for (Player player : players) {
            GameStatisticsPlayer statisticsPlayer = gameStatistics.getPlayer(player.getId());

            assertNotNull(statisticsPlayer);
            assertEquals(player.getScore() * 2, statisticsPlayer.getTotalScore());

            if (winnerPlayer == null) {
                winnerPlayer = player;
            } else if (winnerPlayer.getScore() < player.getScore()) {
                winnerPlayer = player;
            }
        }

        assertEquals(winnerPlayer.getId(), gameStatistics.getWinner().getId());
    }

    @Test
    void createDetailsExcelTest() throws Exception {
        GameStatistics gameStatistics = new GameStatistics();

        ArrayList<GameStatisticsPlayer> players = (ArrayList<GameStatisticsPlayer>) ReflectionUtils.getField(gameStatistics, "players");

        Player player1 = new Player(1);
        Player player2 = new Player(2);

        player1.addScore(10, ChunkType.ROAD);
        player1.addScore(15, ChunkType.TOWN);
        player1.addScore(20, ChunkType.ABBEY);
        player1.addScore(25, ChunkType.FIELD);

        player2.addScore(30, ChunkType.ROAD);
        player2.addScore(35, ChunkType.TOWN);
        player2.addScore(40, ChunkType.ABBEY);
        player2.addScore(45, ChunkType.FIELD);

        players.add(new GameStatisticsPlayer(player1));
        players.add(new GameStatisticsPlayer(player2));

        ExcelNode node = gameStatistics.createDetailsExcel();

        assertEquals("Name\tPlayer ID 1\tPlayer ID 2\n" +
                "POSITION\t1\t2\n" +
                "RESULTS (score)\t70\t150\n" +
                "ROAD POINTS\t10\t30\n" +
                "TOWN POINTS\t15\t35\n" +
                "ABBEY POINTS\t20\t40\n" +
                "FIELD POINTS\t25\t45\n" +
                "PARTISANS PLAYED\t0\t0\n" +
                "PARTISANS REMAINED\t0\t0\n", node.toString());
    }
}
