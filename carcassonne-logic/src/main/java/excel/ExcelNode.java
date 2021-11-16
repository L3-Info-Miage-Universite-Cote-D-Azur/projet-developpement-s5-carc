package excel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ExcelNode {
    private static String CELL_SEPARATOR = "\t";

    private String name;
    private ArrayList<ExcelNode> children;
    private ArrayList<String> columns;
    private ArrayList<ExcelRow> rows;

    public ExcelNode() {
        children = new ArrayList<>();
        columns = new ArrayList<>();
        rows = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public ExcelNode getChild(String name) {
        for (ExcelNode child : children) {
            if (child.name.equalsIgnoreCase(name)) {
                return child;
            }
        }

        return null;
    }

    public int getColumnCount() {
        return columns.size();
    }

    public String getColumnAt(int index) {
        return columns.get(index);
    }

    public int getColumnIndex(String name) {
        return columns.indexOf(name);
    }

    public int getRowCount() {
        return rows.size();
    }

    public ExcelRow getRowAt(int index) {
        return rows.get(index);
    }

    public ExcelRow getRow(String name) {
        for (ExcelRow row : rows) {
            if (row.getName().equals(name)) {
                return row;
            }
        }

        return null;
    }

    private boolean hasDataOutsideChildRange(String[] cells, int childIndex) {
        for (int i = Math.min(cells.length, childIndex) - 1; i >= 0; i--) {
            String cell = cells[i];

            if (!cell.isEmpty() && !cell.isBlank()) {
                return true;
            }
        }

        return false;
    }

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

    private void loadRow(String[] cells, int index, int childIndex) {
        ExcelRow row = new ExcelRow(this, index);

        for (int i = childIndex; i < cells.length; i++) {
            row.add(cells[i]);
        }

        rows.add(row);
    }

    public static ExcelNode load(Path filePath) {
        ExcelNode root = new ExcelNode();
        try {
            root.load(Files.readAllLines(filePath), 0, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;
    }

    private enum State {
        LOADING_COLUMNS,
        LOADING_ROWS
    }
}
