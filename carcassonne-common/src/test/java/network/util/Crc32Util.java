package network.util;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Crc32Util {
    @Test
    void testCrc32() {
        /* CRC32 calculated from a website CRC32 calculator (https://emn178.github.io/online-tools/crc32.html) */
        assertEquals(0xf7d18982, Crc32.getCrc("Hello".getBytes(StandardCharsets.UTF_8)));
        assertEquals(0xb0fe0bcf, Crc32.getCrc("Toto".getBytes(StandardCharsets.UTF_8)));
        assertEquals(0x021616b4, Crc32.getCrc("M. Renevier :)".getBytes(StandardCharsets.UTF_8)));

        assertEquals(0x5dbe2d74, Crc32.getCrc("Capedepe".getBytes(StandardCharsets.UTF_8), 0, 8));
        assertEquals(0x61737724, Crc32.getCrc("IGNORED Doctor Loli 007 IGNORED".getBytes(StandardCharsets.UTF_8), 8, 15));
    }
}
