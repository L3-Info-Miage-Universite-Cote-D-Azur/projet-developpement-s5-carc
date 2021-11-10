package Utils;

import java.util.List;

/**
 * The output utils
 */
public interface OutputUtils {
    /**
     * Align a string to the center
     *
     * @param word    The word to align to the center
     * @param maxSize The max size that can be used to center
     * @return The word align to the center
     */
    static String alignToCenter(String word, int maxSize) {
        StringBuilder wordBuilder = new StringBuilder(word);
        int size = (maxSize - word.length()) / 2;
        for (int j = 0; j < size; j++)
            wordBuilder.insert(0, " ").append(" ");

        if (wordBuilder.toString().length() % 2 != 0)
            wordBuilder.append(" ");

        wordBuilder.append("|");
        return wordBuilder.toString();
    }

    /**
     * Add a line (array) and fill with a key and these values
     *
     * @param lineName      The line name
     * @param cellSize      The max size that can be used to center
     * @param values        The values to fill into the line
     * @param stringBuilder The stringbuilder of the array
     */
    static void toArrayLine(String lineName, int cellSize, List<String> values, StringBuilder stringBuilder) {
        stringBuilder.append("|").append(OutputUtils.alignToCenter(lineName, cellSize));
        for (String value : values)
            stringBuilder.append(OutputUtils.alignToCenter(value, cellSize));
        stringBuilder.append("\n");
    }
}
