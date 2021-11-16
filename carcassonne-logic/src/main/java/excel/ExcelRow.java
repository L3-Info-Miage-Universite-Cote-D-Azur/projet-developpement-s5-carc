package excel;

import java.util.ArrayList;

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

    public String getName() {
        return values.get(0);
    }

    public String getValue(String columnName) {
        return values.get(node.getColumnIndex(columnName));
    }

    public String getValueAt(int index) {
        return values.get(index);
    }
}
