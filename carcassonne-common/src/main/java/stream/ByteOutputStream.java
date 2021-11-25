package stream;

import java.nio.charset.StandardCharsets;

/**
 * Stream for writing primitive types to a byte array.
 */
public class ByteOutputStream {
    private byte[] buffer;
    private int index;

    public ByteOutputStream(int size) {
        buffer = new byte[size];
        index = 0;
    }

    public void writeBoolean(boolean value) {
        writeByte(value ? (byte)1 : (byte)0);
    }

    public void writeByte(byte b) {
        ensureCapacity(index + 1);
        buffer[index++] = b;
    }

    public void writeShort(short s) {
        ensureCapacity(index + 2);
        buffer[index++] = (byte)(s >>> 8);
        buffer[index++] = (byte) s;
    }

    public void writeInt(int i) {
        ensureCapacity(index + 4);
        buffer[index++] = (byte)(i >>> 24);
        buffer[index++] = (byte)(i >>> 16);
        buffer[index++] = (byte)(i >>> 8);
        buffer[index++] = (byte)i;
    }

    public void writeString(String s) {
        if (s == null) {
            writeInt(-1);
        } else {
            writeBytes(s.getBytes(StandardCharsets.UTF_8));
        }
    }

    public void writeBytes(byte[] b) {
        if (b == null) {
            writeInt(-1);
        } else {
            ensureCapacity(index + b.length + 4);
            writeInt(b.length);
            writeBytesWithoutLength(b);
        }
    }

    public void writeBytesWithoutLength(byte[] b) {
        if (b != null) {
            ensureCapacity(index + b.length);
            System.arraycopy(b, 0, buffer, index, b.length);
            index += b.length;
        }
    }

    public void writeBytesWithoutLength(byte[] b, int offset, int length) {
        if (b != null) {
            ensureCapacity(index + length);
            System.arraycopy(b, offset, buffer, index, length);
            index += length;
        }
    }

    private void ensureCapacity(int capacity) {
        if (buffer.length < capacity) {
            byte[] newBuffer = new byte[Math.max(capacity, buffer.length * 2)];
            System.arraycopy(buffer, 0, newBuffer, 0, index);
            buffer = newBuffer;
        }
    }

    public byte[] getBytes() {
        return buffer;
    }

    public int getLength() {
        return index;
    }

    public byte[] toByteArray() {
        byte[] result = new byte[index];
        System.arraycopy(buffer, 0, result, 0, index);
        return result;
    }
}
