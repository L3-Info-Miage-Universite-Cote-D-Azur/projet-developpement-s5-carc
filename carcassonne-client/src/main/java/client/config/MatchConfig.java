package client.config;

import excel.ExcelNode;

/**
 * Represents the match configuration.
 */
public class MatchConfig {
    private final int numMatches;
    private final int numPlayers;

    public MatchConfig(int numMatches, int numPlayers) {
        this.numMatches = numMatches;
        this.numPlayers = numPlayers;
    }

    public MatchConfig(ExcelNode node) {
        this.numMatches = Integer.parseInt(node.getRow("NumMatches").getValue("Value"));
        this.numPlayers = Integer.parseInt(node.getRow("NumPlayers").getValue("Value"));
    }

    /**
     * Gets the number of matches to play.
     * @return number of matches
     */
    public int getNumMatches() {
        return numMatches;
    }

    /**
     * Gets the number of players in each match.
     * @return number of players
     */
    public int getNumPlayers() {
        return numPlayers;
    }
}
