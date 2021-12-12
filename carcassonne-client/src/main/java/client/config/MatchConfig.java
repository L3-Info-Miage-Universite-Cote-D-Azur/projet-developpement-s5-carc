package client.config;

import excel.ExcelNode;

/**
 * Represents the match configuration.
 * It's used to know which match the client wants to play.
 */
public class MatchConfig {
    /**
     * Number of matches to play.
     */
    private final int numMatches;

    /**
     * Number of players per match.
     */
    private final int numPlayers;

    public MatchConfig(ExcelNode node) {
        this.numMatches = Integer.parseInt(node.getRow("NumMatches").getValue("Value"));
        this.numPlayers = Integer.parseInt(node.getRow("NumPlayers").getValue("Value"));
    }

    /**
     * Gets the number of matches to play.
     *
     * @return number of matches
     */
    public int getNumMatches() {
        return numMatches;
    }

    /**
     * Gets the number of players in each match.
     *
     * @return number of players
     */
    public int getNumPlayers() {
        return numPlayers;
    }
}
