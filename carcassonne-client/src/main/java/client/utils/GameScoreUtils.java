package client.utils;

import logic.Game;
import logic.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class that contains methods to print the scores of the players.
 */
public class GameScoreUtils {
    /**
     * Create a string with the scores of the players
     * @param game The game to get the scores from
     * @param cellSize The max size that can be used to center
     * @return
     */
    public static String createScoreTable(Game game, int cellSize) {
        StringBuilder stat = new StringBuilder("| Statistics |\n");

        // Sort the player list by the score
        ArrayList<Player> playersSort = new ArrayList<>(game.getPlayerCount());

        for (int i = 0; i < game.getPlayerCount(); i++) {
            playersSort.add(game.getPlayer(i));
        }

        playersSort.sort(Player::compareTo);
        Collections.reverse(playersSort);

        // Player Name
        List<String> playerName = new ArrayList<>();
        playersSort.forEach(p -> playerName.add("Player " + p.getId()));
        toArrayLine("PLAYER", cellSize, playerName, stat);

        // Total Score
        List<String> playerResult = new ArrayList<>();
        final int[] i = {1};
        playersSort.forEach(p -> {
            playerResult.add(i[0] + "e(" + p.getScore() + ")");
            i[0]++;
        });
        toArrayLine("RESULTS (score)", cellSize, playerResult, stat);

        // Road points
        List<String> playerRoadPoints = new ArrayList<>();
        playersSort.forEach(p -> playerRoadPoints.add(String.valueOf(p.getRoadPoints())));
        toArrayLine("ROAD POINTS", cellSize, playerRoadPoints, stat);

        // Town points
        List<String> playerTownPoints = new ArrayList<>();
        playersSort.forEach(p -> playerTownPoints.add(String.valueOf(p.getTownPoints())));
        toArrayLine("TOWN POINTS", cellSize, playerTownPoints, stat);

        // Abbey points
        List<String> playerAbbeyPoints = new ArrayList<>();
        playersSort.forEach(p -> playerAbbeyPoints.add(String.valueOf(p.getAbbeyPoints())));
        toArrayLine("ABBEY POINTS", cellSize, playerAbbeyPoints, stat);

        // Field points
        List<String> playerFieldPoints = new ArrayList<>();
        playersSort.forEach(p -> playerFieldPoints.add(String.valueOf(p.getFieldPoints())));
        toArrayLine("FIELD POINTS", cellSize, playerFieldPoints, stat);

        // Partisans played
        List<String> playerPartisansPlayed = new ArrayList<>();
        playersSort.forEach(p -> playerPartisansPlayed.add(String.valueOf(p.getPartisansPlayed())));
        toArrayLine("PARTISANS PLAYED", cellSize, playerPartisansPlayed, stat);

        // Partisans remained
        List<String> playerPartisansRemained = new ArrayList<>();
        playersSort.forEach(p -> playerPartisansRemained.add(String.valueOf(p.getPartisansRemained())));
        toArrayLine("PARTISANS REMAINED", cellSize, playerPartisansRemained, stat);

        return stat.toString();
    }

    /**
     * Align a string to the center
     *
     * @param word    The word to align to the center
     * @param maxSize The max size that can be used to center
     * @return The word align to the center
     */
    public static String alignToCenter(String word, int maxSize) {
        StringBuilder wordBuilder = new StringBuilder(word);
        int size = (maxSize - word.length()) / 2;
        for (int j = 0; j < size; j++)
            wordBuilder.insert(0, " ").append(" ");

        if (wordBuilder.toString().length() % 2 != 0)
            wordBuilder.append(" ");

        wordBuilder.append("|");
        return wordBuilder.toString();
    }

    /**
     * Add a line (array) and fill with a key and these values
     *
     * @param lineName      The line name
     * @param cellSize      The max size that can be used to center
     * @param values        The values to fill into the line
     * @param stringBuilder The stringbuilder of the array
     */
    public static void toArrayLine(String lineName, int cellSize, List<String> values, StringBuilder stringBuilder) {
        stringBuilder.append("|").append(alignToCenter(lineName, cellSize));
        for (String value : values)
            stringBuilder.append(alignToCenter(value, cellSize));
        stringBuilder.append("\n");
    }
}
