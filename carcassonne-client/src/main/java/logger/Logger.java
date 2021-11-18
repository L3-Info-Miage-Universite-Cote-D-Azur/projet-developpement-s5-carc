package logger;

import config.LoggerConfig;

public class Logger {
    private static LoggerConfig config = LoggerConfig.getDefaultConfig();
    private static String ANSI_PREFIX = "\u001B[0";

    /**
     * Logs a message to the console with the info color.
     * @param message The message to log.
     * @param args The arguments to format the message with.
     */
    public static void info(String message, Object... args) {
        info(String.format(message, args));
    }

    /**
     * Logs a message to the console with the info color.
     * @param message The message to log.
     */
    public static void info(String message) {
        print(message, config.getInfoColor());
    }

    /**
     * Logs a message to the console with the warning color.
     * @param message The message to log.
     * @param args The arguments to format the message with.
     */
    public static void warn(String message, Object... args) {
        warn(String.format(message, args));
    }

    /**
     * Logs a message to the console with the warning color.
     * @param message The message to log.
     */
    public static void warn(String message) {
        print(message, config.getWarningColor());
    }

    /**
     * Logs a message to the console with the error color.
     * @param message The message to log.
     * @param args The arguments to format the message with.
     */
    public static void error(String message, Object... args) {
        error(String.format(message, args));
    }

    /**
     * Logs a message to the console with the error color.
     * @param message The message to log.
     */
    public static void error(String message) {
        print(message, config.getErrorColor());
    }

    /**
     * Prints a message to the console with the given color.
     * @param message The message to print.
     * @param ansiColorCode The color to print the message in.
     */
    private static void print(String message, String ansiColorCode) {
        if (config.isEnabled()) {
            synchronized (System.out) {
                System.out.print("\u001B[");
                System.out.print(ansiColorCode);
                System.out.print(message);
                System.out.print(ANSI_PREFIX);
                System.out.println("0m");
            }
        }
    }

    /**
     * Sets the configuration for the logger.
     * @param config The configuration to use.
     */
    public static void setConfig(LoggerConfig config) {
        Logger.config = config;
    }
}
