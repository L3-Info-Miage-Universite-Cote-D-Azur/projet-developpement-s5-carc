package excel;

import java.util.ArrayList;

/**
 * Represents a row in an Excel file.
 */
public class ExcelRow {
    private ExcelNode node;
    private ArrayList<String> values;
    private int index;

    public ExcelRow(ExcelNode node, int index) {
        this.node = node;
        this.index = index;
        this.values = new ArrayList<String>();
    }

    public void add(String value) {
        values.add(value);
    }

    /**
     * Gets the name of the row.
     * @return The name of the row.
     */
    public String getName() {
        return values.get(0);
    }

    /**
     * Gets the value of the row at the given column.
     * @param columnName The name of the column.
     * @return The value of the row at the given column.
     */
    public String getValue(String columnName) {
        return values.get(node.getColumnIndex(columnName));
    }

    /**
     * Gets the value of the row at the given column index.
     * @param index The index of the column.
     * @return The value of the row at the given column index.
     */
    public String getValueAt(int index) {
        return values.get(index);
    }
}
