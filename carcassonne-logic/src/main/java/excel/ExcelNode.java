package excel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an excel file.
 */
public class ExcelNode {
    private static String CELL_SEPARATOR = "\t";

    private String name;
    private ArrayList<ExcelNode> children;
    private ArrayList<String> columns;
    private ArrayList<ExcelRow> rows;

    private ExcelNode() {
        children = new ArrayList<>();
        columns = new ArrayList<>();
        rows = new ArrayList<>();
    }

    /**
     * Gets the name of the node.
     * @return The name of the node.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the specified child node.
     * @param name The name of the child node.
     * @return The child node.
     */
    public ExcelNode getChild(String name) {
        for (ExcelNode child : children) {
            if (child.name.equalsIgnoreCase(name)) {
                return child;
            }
        }

        return null;
    }

    /**
     * Gets the column count.
     * @return The column count.
     */
    public int getColumnCount() {
        return columns.size();
    }

    /**
     * Gets the column name at the specified index.
     * @param index The index of the column.
     * @return The column name.
     */
    public String getColumnAt(int index) {
        return columns.get(index);
    }

    /**
     * Gets the index of the specified column.
     * @param name The name of the column.
     * @return The index of the column.
     */
    public int getColumnIndex(String name) {
        return columns.indexOf(name);
    }

    /**
     * Gets the row count.
     * @return The row count.
     */
    public int getRowCount() {
        return rows.size();
    }

    /**
     * Gets the row at the specified index.
     * @param index The index of the row.
     * @return The row.
     */
    public ExcelRow getRowAt(int index) {
        return rows.get(index);
    }

    /**
     * Gets the row by the specified name.
     * @param name The name of the row.
     * @return The row.
     */
    public ExcelRow getRow(String name) {
        for (ExcelRow row : rows) {
            if (row.getName().equals(name)) {
                return row;
            }
        }

        return null;
    }

    /**
     * Determines if data is outside the child range. If the data is outside the child range, then it's the end of the child.
     * @param cells The cells.
     * @param childIndex The child index.
     * @return True if the data is outside the child range.
     */
    private boolean hasDataOutsideChildRange(String[] cells, int childIndex) {
        for (int i = Math.min(cells.length, childIndex) - 1; i >= 0; i--) {
            String cell = cells[i];

            if (!cell.isEmpty() && !cell.isBlank()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Loads the specified excel file.
     * @param lines The lines of the excel file.
     * @param rowIndex The row index.
     * @param columnIndex The column index.
     * @return The current row position.
     */
    private int load(List<String> lines, int rowIndex, int columnIndex) {
        State state = State.LOADING_COLUMNS;

        while (rowIndex < lines.size()) {
            String[] cells = lines.get(rowIndex).split(CELL_SEPARATOR, -1);

            if (hasDataOutsideChildRange(cells, columnIndex)) {
                return rowIndex - 1;
            }

            switch (state) {
                case LOADING_COLUMNS:
                    String firstCell = cells[columnIndex];

                    if (!firstCell.equalsIgnoreCase("Name")) {
                        ExcelNode child = new ExcelNode();
                        child.name = cells[columnIndex];
                        rowIndex = child.load(lines, rowIndex + 1, columnIndex + 1);
                        children.add(child);
                    } else {
                        loadColumns(cells, columnIndex);
                        state = State.LOADING_ROWS;
                    }

                    break;
                case LOADING_ROWS:
                    loadRow(cells, rowIndex, columnIndex);
                    break;
            }

            rowIndex++;
        }

        return rowIndex;
    }

    /**
     * Loads the columns from the specified cells.
     * @param cells The cells.
     * @param childIndex The child index.
     */
    private void loadColumns(String[] cells, int childIndex) {
        columns.ensureCapacity(cells.length - childIndex);

        for (int i = childIndex; i < cells.length; i++) {
            String columnName = cells[i];

            if (columnName.isEmpty() || columnName.isBlank()) {
                break;
            }

            columns.add(columnName);
        }
    }

    /**
     * Loads the row from the specified cells.
     * @param cells The cells.
     * @param index The index.
     * @param childIndex The child index.
     */
    private void loadRow(String[] cells, int index, int childIndex) {
        ExcelRow row = new ExcelRow(this, index);

        for (int i = childIndex; i < cells.length; i++) {
            row.add(cells[i]);
        }

        rows.add(row);
    }

    /**
     * Loads the specified excel file.
     * @param filePath The file path.
     * @return The root node.
     */
    public static ExcelNode load(Path filePath) {
        ExcelNode root = new ExcelNode();
        try {
            root.load(Files.readAllLines(filePath), 0, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;
    }

    /**
     * State of the reader.
     */
    private enum State {
        /**
         * Reader is loading columns.
         * After this state, the reader will load rows.
         */
        LOADING_COLUMNS,

        /**
         * Reader is loading rows.
         */
        LOADING_ROWS
    }
}
