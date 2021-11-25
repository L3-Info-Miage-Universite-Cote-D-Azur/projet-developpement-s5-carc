package server.logger;

/**
 * Logger class that logs messages to the console with different levels of importance.
 */
public class Logger {
    private static final LogLevel DEFAULT_LEVEL = LogLevel.DEBUG;

    /**
     * Logs a message to the console with the DEBUG level.
     *
     * @param message the message to log
     */
    public static void debug(String message) {
        print(message, "\u001B[36m", LogLevel.DEBUG);
    }

    /**
     * Logs a message to the console with the DEBUG level.
     *
     * @param message the message to log
     */
    public static void debug(String message, Object... args) {
        debug(String.format(message, args));
    }

    /**
     * Logs a message to the console with the INFO level.
     *
     * @param message the message to log
     */
    public static void info(String message) {
        print(message, "\u001B[32m", LogLevel.INFO);
    }

    /**
     * Logs a message to the console with the INFO level.
     *
     * @param message the message to log
     */
    public static void info(String message, Object... args) {
        info(String.format(message, args));
    }

    /**
     * Logs a message to the console with the WARNING level.
     *
     * @param message the message to log
     */
    public static void warn(String message) {
        print(message, "\u001B[33m", LogLevel.WARN);
    }

    /**
     * Logs a message to the console with the WARNING level.
     *
     * @param message the message to log
     */
    public static void warn(String message, Object... args) {
        warn(String.format(message, args));
    }

    /**
     * Logs a message to the console with the ERROR level.
     *
     * @param message the message to log
     */
    public static void error(String message) {
        print(message, "\u001B[31m", LogLevel.ERROR);
    }

    /**
     * Logs a message to the console with the ERROR level.
     *
     * @param message the message to log
     */
    public static void error(String message, Object... args) {
        error(String.format(message, args));
    }

    /**
     * Logs a message to the console with the FATAL level.
     *
     * @param message the message to log
     */
    public static void fatal(String message) {
        print(message, "\u001B[31m", LogLevel.FATAL);
    }

    /**
     * Logs a message to the console with the FATAL level.
     *
     * @param message the message to log
     */
    public static void fatal(String message, Object... args) {
        fatal(String.format(message, args));
    }

    /**
     * Logs a message to the console with the DEBUG level.
     *
     * @param message the message to log
     */
    public static void print(String message, String color, LogLevel level) {
        if (level.ordinal() >= DEFAULT_LEVEL.ordinal()) {
            System.out.println(color + message + "\u001B[0m");
        }
    }
}
