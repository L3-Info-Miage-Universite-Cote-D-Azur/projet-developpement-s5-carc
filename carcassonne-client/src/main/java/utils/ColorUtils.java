package utils;

public interface ColorUtils {
    static final String ANSI_RESET = "\u001B[0m";
    static final String ANSI_BLACK = "\u001B[30m";
    static final String ANSI_RED = "\u001B[31m";
    static final String ANSI_GREEN = "\u001B[32m";
    static final String ANSI_YELLOW = "\u001B[33m";
    static final String ANSI_BLUE = "\u001B[34m";
    static final String ANSI_PURPLE = "\u001B[35m";
    static final String ANSI_CYAN = "\u001B[36m";
    static final String ANSI_WHITE = "\u001B[37m";
    static final String BLACK_BACKGROUND = "\033[40m";  // BLACK
    static final String RED_BACKGROUND = "\033[41m";    // RED
    static final String GREEN_BACKGROUND = "\033[42m";  // GREEN
    static final String YELLOW_BACKGROUND = "\033[43m"; // YELLOW
    static final String BLUE_BACKGROUND = "\033[44m";   // BLUE
    static final String PURPLE_BACKGROUND = "\033[45m"; // PURPLE
    static final String CYAN_BACKGROUND = "\033[46m";   // CYAN
    static final String WHITE_BACKGROUND = "\033[47m";  // WHITE
}
