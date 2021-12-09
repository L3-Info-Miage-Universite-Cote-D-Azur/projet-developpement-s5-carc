package excel;

import java.util.ArrayList;

/**
 * Represents a row in an Excel file.
 */
public class ExcelRow {
    private final ExcelNode node;
    private final ArrayList<String> values;

    public ExcelRow(ExcelNode node) {
        this.node = node;
        this.values = new ArrayList<>();
    }

    /**
     * Adds the specified value to the row.
     *
     * @param value
     */
    public void add(String value) {
        values.add(value);
    }

    /**
     * Adds the specified value to the row.
     *
     * @param value
     */
    public void add(String columnName, String value) {
        int columnIndex = node.getColumnIndex(columnName);

        while (values.size() <= columnIndex) {
            values.add("");
        }

        values.set(columnIndex, value);
    }

    /**
     * Gets the name of the row.
     *
     * @return The name of the row.
     */
    public String getName() {
        return values.get(0);
    }

    /**
     * Gets the value of the row at the given column.
     *
     * @param columnName The name of the column.
     * @return The value of the row at the given column.
     */
    public String getValue(String columnName) {
        return values.get(node.getColumnIndex(columnName));
    }

    /**
     * Gets the value of the row at the given column index.
     *
     * @param index The index of the column.
     * @return The value of the row at the given column index.
     */
    public String getValueAt(int index) {
        return values.get(index);
    }

    public void writeToStringBuilder(StringBuilder sb, String separator) {
        boolean first = true;

        for (String value : values) {
            if (!first) {
                sb.append(separator);
            } else {
                first = false;
            }

            sb.append(value);
        }
    }
}
