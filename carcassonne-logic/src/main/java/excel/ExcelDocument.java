package excel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class ExcelDocument {
    private String[][] cells;
    private String[] columns;
    private int rowIndex;
    private int rowCount;
    private int columnIndex;

    public ExcelDocument(File file) {
        try {
            List<String> lines = Files.readAllLines(file.toPath());

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);

                if (line.trim().length() == 0) {
                    lines.remove(i);
                    i--;
                }
            }

            rowCount = lines.size();
            cells = new String[rowCount][];

            for (int i = 0; i < lines.size(); i++) {
                cells[i] = lines.get(i).split("\t");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ExcelDocument(String[][] cells, int rowIndex, int rowCount, int colIndex) {
        this.cells = cells;
        this.rowIndex = rowIndex;
        this.rowCount = rowCount;
        this.columnIndex = colIndex;

        if (getCell(0, 0).length() != 0) {
            this.columns = cells[rowIndex];
            this.rowIndex++;
            this.rowCount--;
        }
    }

    public String getColumnName(int index) {
        if (columns == null) {
            throw new IllegalStateException("getColumnName() called on non-columned document.");
        }

        return columns[index];
    }

    public int getColumnIndex(String name) {
        for (int i = columnIndex; i < columns.length; i++) {
            if (columns[i].equalsIgnoreCase(name)) {
                return i - columnIndex;
            }
        }

        return -1;
    }

    public int getRowIndex(String name) {
        int columnNameIndex = getColumnIndex("name");

        for (int i = 0; i < getRowCount(); i++) {
            if (getCell(i, columnNameIndex).equalsIgnoreCase(name)) {
                return i;
            }
        }

        return -1;
    }

    public String getCell(String rowName, String columnName) {
        return getCell(getRowIndex(rowName), getColumnIndex(columnName));
    }

    public String getCell(int row, int col) {
        if (row < 0 || row >= rowCount) {
            throw new IllegalArgumentException("Row is out of bounds");
        }

        if (col < 0 || col >= cells[rowIndex + row].length) {
            throw new IllegalArgumentException("Col is out of bounds");
        }

        return cells[rowIndex + row][columnIndex + col];
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return cells[rowIndex].length - columnIndex;
    }

    public ExcelDocument getDocument(String name) {
        int startIndex = -1;
        int endIndex = -1;

        for (int i = 0; i < getRowCount(); i++) {
            String categoryName = getCell(i, 0);

            if (categoryName.equalsIgnoreCase(name)) {
                startIndex = i + 1;
            } else if (categoryName.length() != 0) {
                if (startIndex != -1) {
                    endIndex = i - 1;
                }
            }
        }

        if (startIndex == -1) {
            return null;
        }

        return new ExcelDocument(cells, startIndex, endIndex - startIndex, rowIndex + 1);
    }
}
