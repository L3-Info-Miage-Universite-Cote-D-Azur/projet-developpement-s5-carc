package network;

import java.nio.ByteBuffer;

public class ResizableByteBuffer {
    private byte[] buffer;
    private int index;
    private int maxSize;

    public ResizableByteBuffer(int size, int maxSize) {
        this.buffer = new byte[size];
        this.maxSize = maxSize;
        this.index = 0;
    }

    public void clear() {
        this.index = 0;
    }

    public void put(byte[] data) {
        put(data, 0, data.length);
    }

    public void put(byte[] data, int offset, int length) {
        int remaining = this.buffer.length - this.index;

        if (remaining < length) {
            if (this.index + length > this.maxSize) {
                throw new RuntimeException("Buffer too large");
            }
            resize(Math.min((this.index + length) * 2, this.maxSize));
        }

        System.arraycopy(data, offset, this.buffer, this.index, length);
        this.index += length;
    }

    private void resize(int newSize) {
        byte[] newBuffer = new byte[newSize];
        System.arraycopy(this.buffer, 0, newBuffer, 0, this.index);
        this.buffer = newBuffer;
    }

    public void remove(int size) {
        if (size > this.index) {
            throw new IllegalArgumentException("Size is greater than buffer size");
        }
        System.arraycopy(this.buffer, size, this.buffer, 0, this.index - size);
        this.index -= size;
    }

    public byte[] getBuffer() {
        return this.buffer;
    }

    public int size() {
        return this.index;
    }
}
