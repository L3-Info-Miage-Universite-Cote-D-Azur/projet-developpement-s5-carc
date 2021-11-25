package server;

import server.logger.Logger;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        Map<String, String> config = loadConfigurationFromArguments(args);

        String listenIp = config.getOrDefault("listen-ip", "127.0.0.1");
        int listenPort = Integer.parseInt(config.getOrDefault("listen-port", "8080"));

        Server server = new Server(listenIp, listenPort);

        server.start();

        Logger.info("Server started on %s:%d!", listenIp, listenPort);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Logger.info("Stopping the server...");

            try {
                server.stop();
                Logger.info("Server stopped");
            } catch (Exception e) {
                Logger.error("Failed to stop the server: %s", e);
            }
        }));
    }


    private static Map<String, String> loadConfigurationFromArguments(String[] args) {
        if (args.length % 2 != 0) {
            throw new IllegalArgumentException("Invalid number of arguments");
        }

        HashMap<String, String> config = new HashMap<>();

        for (int i = 0; i < args.length; i += 2) {
            String arg = args[i];
            String value = args[i + 1];

            if (config.containsKey(arg)) {
                throw new IllegalArgumentException("Duplicate argument: " + arg);
            }

            config.put(arg, value);
        }

        return config;
    }
}
