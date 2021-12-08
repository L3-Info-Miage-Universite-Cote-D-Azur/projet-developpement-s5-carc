package client.config;

import excel.ExcelNode;

import java.net.URISyntaxException;
import java.nio.file.Path;

public class ClientConfig {
    private final String serverHost;
    private final int serverPort;
    private final LoggerConfig loggerConfig;
    private final MatchConfig matchConfig;

    private ClientConfig(ExcelNode node) {
        ExcelNode serverNode = node.getChild("Server");

        if (serverNode == null) {
            throw new IllegalArgumentException("Server node not found");
        }

        ExcelNode loggerNode = node.getChild("Logger");

        if (loggerNode == null) {
            throw new IllegalArgumentException("Logger node not found");
        }

        ExcelNode matchNode = node.getChild("Match");

        if (matchNode == null) {
            throw new IllegalArgumentException("Match node not found");
        }

        this.serverHost = serverNode.getRow("Host").getValue("Value");
        this.serverPort = Integer.parseInt(serverNode.getRow("Port").getValue("Value"));

        this.loggerConfig = new LoggerConfig(loggerNode);
        this.matchConfig = new MatchConfig(matchNode);
    }

    /**
     * Loads the client configuration from the resources.
     *
     * @return the client configuration
     */
    public static ClientConfig loadFromResources() {
        try {
            String resourcePath = Path.of(ClientConfig.class.getResource(".").toURI()).toString();
            ExcelNode rootNode = ExcelNode.load(Path.of(resourcePath, "config.txt"));
            return new ClientConfig(rootNode);
        } catch (URISyntaxException e) {
            return null;
        }
    }

    /**
     * Returns the hostname of the server.
     *
     * @return the hostname of the server
     */
    public String getServerHost() {
        return serverHost;
    }

    /**
     * Returns the port of the server.
     *
     * @return the port of the server
     */
    public int getServerPort() {
        return serverPort;
    }

    /**
     * Returns the logger configuration.
     *
     * @return the logger configuration
     */
    public LoggerConfig getLoggerConfig() {
        return loggerConfig;
    }

    /**
     * Returns the match configuration.
     *
     * @return the match configuration
     */
    public MatchConfig getMatchConfig() {
        return matchConfig;
    }
}
