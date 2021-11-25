package network;

/**
 * A resizable byte buffer with an initial size and a maximum size.
 */
public class ResizableByteBuffer {
    private byte[] buffer;
    private int index;
    private int maxSize;

    public ResizableByteBuffer(int size, int maxSize) {
        this.buffer = new byte[size];
        this.maxSize = maxSize;
        this.index = 0;
    }

    /**
     * Clears the buffer.
     */
    public void clear() {
        this.index = 0;
    }

    /**
     * Puts the given byte array into the buffer.
     *
     * @param data The data to put into the buffer.
     */
    public void put(byte[] data) {
        put(data, 0, data.length);
    }

    /**
     * Puts the given byte array into the buffer.
     *
     * @param data   The data to put into the buffer.
     * @param offset The offset to start putting the data at.
     * @param length The length of the data to put.
     */
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

    /**
     * Resizes the buffer to the given size.
     *
     * @param newSize The new size of the buffer.
     */
    private void resize(int newSize) {
        byte[] newBuffer = new byte[newSize];
        System.arraycopy(this.buffer, 0, newBuffer, 0, this.index);
        this.buffer = newBuffer;
    }

    /**
     * Removes the given amount of bytes from the buffer.
     *
     * @param size The amount of bytes to remove.
     */
    public void remove(int size) {
        if (size > this.index) {
            throw new IllegalArgumentException("Size is greater than buffer size");
        }
        System.arraycopy(this.buffer, size, this.buffer, 0, this.index - size);
        this.index -= size;
    }

    /**
     * Gets the buffer.
     *
     * @return The buffer.
     */
    public byte[] getBuffer() {
        return this.buffer;
    }

    /**
     * Gets the number of bytes in the buffer.
     *
     * @return The number of bytes in the buffer.
     */
    public int size() {
        return this.index;
    }
}
