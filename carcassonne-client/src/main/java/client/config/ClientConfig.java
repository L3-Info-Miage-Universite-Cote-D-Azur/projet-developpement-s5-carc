package client.config;

import excel.ExcelNode;

import java.net.URISyntaxException;
import java.nio.file.Path;

public class ClientConfig {
    private final String serverHost;
    private final int serverPort;
    private final LoggerConfig loggerConfig;

    private ClientConfig(ExcelNode node) {
        ExcelNode serverNode = node.getChild("Server");

        if (serverNode == null) {
            throw new IllegalArgumentException("Server node not found");
        }

        ExcelNode loggerNode = node.getChild("Logger");

        if (loggerNode == null) {
            throw new IllegalArgumentException("Logger node not found");
        }

        this.serverHost = serverNode.getRow("Host").getValue("Value");
        this.serverPort = Integer.parseInt(serverNode.getRow("Port").getValue("Value"));

        this.loggerConfig = new LoggerConfig(loggerNode);
    }

    /**
     * Returns the hostname of the server.
     * @return the hostname of the server
     */
    public String getServerHost() {
        return serverHost;
    }

    /**
     * Returns the port of the server.
     * @return the port of the server
     */
    public int getServerPort() {
        return serverPort;
    }

    /**
     * Returns the logger configuration.
     * @return the logger configuration
     */
    public LoggerConfig getLoggerConfig() {
        return loggerConfig;
    }

    public static ClientConfig loadFromResources() {
        try {
            ExcelNode rootNode = ExcelNode.load(Path.of(LoggerConfig.class.getResource("config.txt").toURI()));
            return new ClientConfig(rootNode);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
}
