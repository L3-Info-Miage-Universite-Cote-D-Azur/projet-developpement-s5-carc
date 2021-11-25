package excel;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExcelNodeTest {

    @Test
    void testLoadExcel() {
        ExcelNode node = ExcelNode.load("Name\tColumn1\tColumn2\n" +
                "Row1\tA\tB\n" +
                "Row2\tC\tD\n");
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
}
