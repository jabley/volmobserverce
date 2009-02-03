/* ----------------------------------------------------------------------------
 * Copyright Volantis Systems Ltd 2007. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */
package com.volantis.map.common.streams;

import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.IIOByteBuffer;
import java.nio.ByteOrder;
import java.io.IOException;

/**
 * A delegating SeekableStream wrapper that does not delegate the close method
 */
public class NoCloseSeekableInputStream implements SeekableInputStream {

    /**
     * Delegate to this stream
     */
    private final ImageInputStream delegate;

    public NoCloseSeekableInputStream(ImageInputStream delegate) {
        this.delegate = delegate;
    }

    // Javadoc inherited
    public void setByteOrder(final ByteOrder byteOrder) {
        delegate.setByteOrder(byteOrder);
    }

    // Javadoc inherited
    public ByteOrder getByteOrder() {
        return delegate.getByteOrder();
    }
                        
    // Javadoc inherited
    public int read() throws IOException {
        return delegate.read();
    }

    // Javadoc inherited
    public int read(final byte[] b) throws IOException {
        return delegate.read(b);
    }

    // Javadoc inherited
    public int read(final byte[] b, final int off, final int len) throws IOException {
        return delegate.read(b, off, len);
    }

    // Javadoc inherited
    public void readBytes(final IIOByteBuffer buf, final int len) throws IOException {
        delegate.readBytes(buf, len);
    }

    // Javadoc inherited
    public boolean readBoolean() throws IOException {
        return delegate.readBoolean();
    }

    // Javadoc inherited
    public byte readByte() throws IOException {
        return delegate.readByte();
    }

    // Javadoc inherited
    public int readUnsignedByte() throws IOException {
        return delegate.readUnsignedByte();
    }

    // Javadoc inherited
    public short readShort() throws IOException {
        return delegate.readShort();
    }

    // Javadoc inherited
    public int readUnsignedShort() throws IOException {
        return delegate.readUnsignedShort();
    }

    // Javadoc inherited
    public char readChar() throws IOException {
        return delegate.readChar();
    }

    // Javadoc inherited
    public int readInt() throws IOException {
        return delegate.readInt();
    }

    // Javadoc inherited
    public long readUnsignedInt() throws IOException {
        return delegate.readUnsignedInt();
    }

    // Javadoc inherited
    public long readLong() throws IOException {
        return delegate.readLong();
    }

    // Javadoc inherited
    public float readFloat() throws IOException {
        return delegate.readFloat();
    }

    // Javadoc inherited
    public double readDouble() throws IOException {
        return delegate.readDouble();
    }

    // Javadoc inherited
    public String readLine() throws IOException {
        return delegate.readLine();
    }

    // Javadoc inherited
    public String readUTF() throws IOException {
        return delegate.readUTF();
    }

    // Javadoc inherited
    public void readFully(final byte[] b, final int off, final int len) throws IOException {
        delegate.readFully(b, off, len);
    }

    // Javadoc inherited
    public void readFully(final byte[] b) throws IOException {
        delegate.readFully(b);
    }

    // Javadoc inherited
    public void readFully(final short[] s, final int off, final int len) throws IOException {
        delegate.readFully(s, off, len);
    }

    // Javadoc inherited
    public void readFully(final char[] c, final int off, final int len) throws IOException {
        delegate.readFully(c, off, len);
    }

    // Javadoc inherited
    public void readFully(final int[] i, final int off, final int len) throws IOException {
        delegate.readFully(i, off, len);
    }

    // Javadoc inherited
    public void readFully(final long[] l, final int off, final int len) throws IOException {
        delegate.readFully(l, off, len);
    }

    // Javadoc inherited
    public void readFully(final float[] f, final int off, final int len) throws IOException {
        delegate.readFully(f, off, len);
    }

    // Javadoc inherited
    public void readFully(final double[] d, final int off, final int len) throws IOException {
        delegate.readFully(d, off, len);
    }

    // Javadoc inherited
    public long getStreamPosition() throws IOException {
        return delegate.getStreamPosition();
    }

    // Javadoc inherited
    public int getBitOffset() throws IOException {
        return delegate.getBitOffset();
    }

    // Javadoc inherited
    public void setBitOffset(final int bitOffset) throws IOException {
        delegate.setBitOffset(bitOffset);
    }

    // Javadoc inherited
    public int readBit() throws IOException {
        return delegate.readBit();
    }

    // Javadoc inherited
    public long readBits(final int numBits) throws IOException {
        return delegate.readBits(numBits);
    }

    // Javadoc inherited
    public long length() throws IOException {
        return delegate.length();
    }

    // Javadoc inherited
    public int skipBytes(final int n) throws IOException {
        return delegate.skipBytes(n);
    }

    // Javadoc inherited
    public long skipBytes(final long n) throws IOException {
        return delegate.skipBytes(n);
    }

    // Javadoc inherited
    public void seek(final long pos) throws IOException {
        delegate.seek(pos);
    }

    // Javadoc inherited
    public void mark() {
        delegate.mark();
    }

    // Javadoc inherited
    public void reset() throws IOException {
        delegate.reset();
    }

    // Javadoc inherited
    public void flushBefore(final long pos) throws IOException {
        delegate.flushBefore(pos);
    }

    // Javadoc inherited
    public void flush() throws IOException {
        delegate.flush();
    }

    // Javadoc inherited
    public long getFlushedPosition() {
        return delegate.getFlushedPosition();
    }

    // Javadoc inherited
    public boolean isCached() {
        return delegate.isCached();
    }

    // Javadoc inherited
    public boolean isCachedMemory() {
        return delegate.isCachedMemory();
    }

    // Javadoc inherited
    public boolean isCachedFile() {
        return delegate.isCachedFile();
    }

    /**
     * Has no effect on the underlying delegate stream
     * @throws IOException
     */
    public void close() throws IOException {
        // do nothing
    }

    /**
     * Close the underlying delegate stream.
     */
    public void closeDelegate() throws IOException{
        this.delegate.close();
    }
}
