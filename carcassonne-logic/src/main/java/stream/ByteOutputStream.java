package stream;

import java.nio.charset.StandardCharsets;

public class ByteOutputStream {
    private byte[] buffer;
    private int index;

    public ByteOutputStream(int size) {
        buffer = new byte[size];
        index = 0;
    }

    public void writeByte(byte b) {
        buffer[index++] = b;
    }

    public void writeShort(short s) {
        writeByte((byte)(s >>> 8));
        writeByte((byte)s);
    }

    public void writeInt(int i) {
        writeByte((byte)(i >>> 24));
        writeByte((byte)(i >>> 16));
        writeByte((byte)(i >>> 8));
        writeByte((byte)i);
    }

    public void writeString(String s) {
        if (s == null) {
            writeInt(-1);
        } else {
            System.arraycopy(s.getBytes(StandardCharsets.UTF_8), 0, buffer, index, s.length());
            index += s.length();
        }
    }

    public void writeBytes(byte[] b) {
        if (b == null) {
            writeInt(-1);
        } else {
            System.arraycopy(b, 0, buffer, index, b.length);
            index += b.length;
        }
    }

    public byte[] getBytes() {
        return buffer;
    }

    public int getLength() {
        return index;
    }
}
