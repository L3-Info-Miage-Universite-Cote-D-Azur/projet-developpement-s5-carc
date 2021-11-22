package client.logger;

import client.config.LoggerConfig;

public class Logger {
    private static LoggerConfig config = LoggerConfig.getDefaultConfig();
    private static String ANSI_PREFIX = "\u001B[0";

    /**
     * Logs a message to the console with the debug color.
     * @param message The message to log.
     * @param args The arguments to format the message with.
     */
    public static void debug(LoggerCategory category, String message, Object... args) {
        debug(category, String.format(message, args));
    }

    /**
     * Logs a message to the console with the debug color.
     * @param message The message to log.
     */
    public static void debug(LoggerCategory category, String message) {
        print(category, message, config.getDebugColor(), LogLevel.DEBUG);
    }

    /**
     * Logs a message to the console with the info color.
     * @param message The message to log.
     * @param args The arguments to format the message with.
     */
    public static void info(LoggerCategory category, String message, Object... args) {
        info(category, String.format(message, args));
    }

    /**
     * Logs a message to the console with the info color.
     * @param message The message to log.
     */
    public static void info(LoggerCategory category, String message) {
        print(category, message, config.getInfoColor(), LogLevel.INFO);
    }

    /**
     * Logs a message to the console with the warning color.
     * @param message The message to log.
     * @param args The arguments to format the message with.
     */
    public static void warn(LoggerCategory category, String message, Object... args) {
        warn(category, String.format(message, args));
    }

    /**
     * Logs a message to the console with the warning color.
     * @param message The message to log.
     */
    public static void warn(LoggerCategory category, String message) {
        print(category, message, config.getWarningColor(), LogLevel.WARN);
    }

    /**
     * Logs a message to the console with the error color.
     * @param message The message to log.
     * @param args The arguments to format the message with.
     */
    public static void error(LoggerCategory category, String message, Object... args) {
        error(category, String.format(message, args));
    }

    /**
     * Logs a message to the console with the error color.
     * @param message The message to log.
     */
    public static void error(LoggerCategory category, String message) {
        print(category, message, config.getErrorColor(), LogLevel.ERROR);
    }

    /**
     * Prints a message to the console with the given color.
     * @param message The message to print.
     * @param ansiColorCode The color to print the message in.
     */
    private static void print(LoggerCategory category, String message, String ansiColorCode, LogLevel level) {
        if (level.ordinal() >= config.getLevel().ordinal()) {
            synchronized (System.out) {
                System.out.print("\u001B[");
                System.out.print(ansiColorCode);
                System.out.print(category);
                System.out.print(": ");
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
