package client.config;

import excel.ExcelNode;

/**
 * Represents the stats' configuration.
 */
public class StatsConfig {
    private final boolean createBoardView;
    private final boolean createGlobalStatistics;

    public StatsConfig(ExcelNode node) {
        this.createBoardView = Boolean.parseBoolean(node.getRow("CreateBoardView").getValue("Value"));
        this.createGlobalStatistics = Boolean.parseBoolean(node.getRow("CreateGlobalStatistics").getValue("Value"));
    }

    /**
     * Returns whether the board view should be created.
     * @return whether the board view should be created
     */
    public boolean isCreateBoardView() {
        return createBoardView;
    }

    /**
     * Returns whether the global statistics should be created.
     * @return whether the global statistics should be created
     */
    public boolean isCreateGlobalStatistics() {
        return createGlobalStatistics;
    }
}
