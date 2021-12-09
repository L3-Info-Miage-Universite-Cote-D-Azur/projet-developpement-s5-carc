package excel;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExcelNodeTest {

    @Test
    void testLoadExcel() {
        ExcelNode node = ExcelNode.load("""
                Name\tColumn1\tColumn2
                Row1\tA\tB
                Row2\tC\tD
                """);
        ExcelRow r1 = node.getRow("Row1");
        ExcelRow r2 = node.getRow("Row2");
        String resultR1C1 = r1.getValue("Column1");
        String resultR1C2 = r1.getValue("Column2");
        String resultR2C1 = r2.getValue("Column1");
        String resultR2C2 = r2.getValue("Column2");
        assertEquals(2, node.getRowCount());
        assertEquals(3, node.getColumnCount());
        assertEquals("A", resultR1C1);
        assertEquals("B", resultR1C2);
        assertEquals("C", resultR2C1);
        assertEquals("D", resultR2C2);
    }

    @Test
    void testWriteExcel() {
        ExcelNode node = new ExcelNode();

        node.addColumn("Name");
        node.addColumn("Column1");
        node.addColumn("Column2");

        ExcelRow r1 = node.createRow("Row1");
        r1.add("Column1", "A");
        r1.add("Column2", "B");

        ExcelRow r2 = node.createRow("Row2");
        r2.add("Column1", "C");
        r2.add("Column2", "D");

        String result = node.toString();
        assertEquals("""
                Name\tColumn1\tColumn2
                Row1\tA\tB
                Row2\tC\tD
                """, result);
    }

    @Test
    void testWriteWithChildren() {
        ExcelNode root = new ExcelNode();
        ExcelNode child1 = root.createChild("Child1");
        ExcelNode child2 = root.createChild("Child2");

        child1.addColumn("Name");
        child1.addColumn("Column1");
        child1.addColumn("Column2");

        child2.addColumn("Name");
        child2.addColumn("Column3");
        child2.addColumn("Column4");

        ExcelRow r1 = child1.createRow("Row1");
        r1.add("Column1", "A");
        r1.add("Column2", "B");

        ExcelRow r2 = child1.createRow("Row2");
        r2.add("Column1", "C");
        r2.add("Column2", "D");

        ExcelRow r3 = child2.createRow("Row3");
        r3.add("Column3", "E");
        r3.add("Column4", "F");

        ExcelRow r4 = child2.createRow("Row4");
        r4.add("Column3", "G");
        r4.add("Column4", "H");

        String result = root.toString();

        assertEquals("""
                Child1
                \tName\tColumn1\tColumn2
                \tRow1\tA\tB
                \tRow2\tC\tD
                Child2
                \tName\tColumn3\tColumn4
                \tRow3\tE\tF
                \tRow4\tG\tH
                """, result);
    }
}
