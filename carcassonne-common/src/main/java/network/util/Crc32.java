package network.util;

/**
 * CRC32 checksum calculator.
 */
public class Crc32 {
    private static final int[] crcTable = new int[256];
    private static final int crcPoly = 0xEDB88320;

    static {
        for (int i = 0; i < 256; ++i) {
            int crc = i;
            for (int j = 0; j < 8; ++j) {
                if ((crc & 0x00000001) != 0) {
                    crc = crc >>> 1 ^ crcPoly;
                } else {
                    crc >>>= 1;
                }
            }
            crcTable[i] = crc;
        }
    }

    /**
     * Calculates the CRC32 checksum of the given data.
     *
     * @param data the data to calculate the checksum for
     * @return the checksum
     */
    public static int getCrc(byte[] data) {
        int crc = 0xFFFFFFFF;
        for (int i = 0; i < data.length; i++) {
            crc = (crc >>> 8) ^ crcTable[(crc ^ data[i]) & 0xFF];
        }
        return crc ^ 0xFFFFFFFF;
    }

    /**
     * Calculates the CRC32 checksum of the given data.
     *
     * @param data   the data to calculate the checksum for
     * @param offset the offset to start at
     * @param length the length to calculate the checksum for
     * @return the checksum
     */
    public static int getCrc(byte[] data, int offset, int length) {
        int crc = 0xFFFFFFFF;
        for (int i = offset; i < offset + length; i++) {
            crc = (crc >>> 8) ^ crcTable[(crc ^ data[i]) & 0xFF];
        }
        return crc ^ 0xFFFFFFFF;
    }
}
