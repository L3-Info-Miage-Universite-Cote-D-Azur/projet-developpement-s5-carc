package client.utils;

import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

class ImageDatabaseTest {
    @Test
    void testImageDatabase() {
        int sizew = 50;
        int sizeh = 80;
        ImageDatabase imageDatabase = new ImageDatabase(sizew, sizeh);

        assertEquals(sizeh, imageDatabase.getHeight());
        assertEquals(sizew, imageDatabase.getWidth());
    }

    @Test
    void testCache() {
        int sizew = 50;
        int sizeh = 80;
        ImageDatabase imageDatabase = new ImageDatabase(sizew, sizeh);
        BufferedImage bi0 = new BufferedImage(sizew, sizeh, BufferedImage.TYPE_INT_RGB);
        BufferedImage bi1 = new BufferedImage(sizew, sizeh, BufferedImage.TYPE_INT_RGB);
        imageDatabase.cache("B0", bi0);
        imageDatabase.cache("B1", bi1);

        assertEquals(bi0, imageDatabase.get("B0"));
        assertEquals(bi1, imageDatabase.get("B1"));
        imageDatabase.flush();

        assertThrows(IllegalArgumentException.class, () -> {
            imageDatabase.get("B0");
        });
    }
}
