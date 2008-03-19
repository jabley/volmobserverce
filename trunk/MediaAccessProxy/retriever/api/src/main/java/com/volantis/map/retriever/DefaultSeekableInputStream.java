/*
This file is part of Volantis Mobility Server. 

Volantis Mobility Server is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Volantis Mobility Server is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Volantis Mobility Server.  If not, see <http://www.gnu.org/licenses/>. 
*/
/* ----------------------------------------------------------------------------
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.map.retriever;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.nio.ByteOrder;

import javax.imageio.stream.FileCacheImageInputStream;
import javax.imageio.stream.IIOByteBuffer;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.imageio.stream.FileImageInputStream;

import our.apache.commons.httpclient.HttpMethod;

/**
 * Simple delegate class to hide the fact we are using ImageInputStreams.
 * Image input streams are {@link java.io.DataInput} implementations that use
 * either memory or files to cache as much of the underlying InputStream as
 * is desired.
 *
 * @see ImageInputStream
 */
public class DefaultSeekableInputStream implements SeekableInputStream {

    /**
     * The temporary location to write cache files to if they are used.
     */
    private static final String TMP_DIR_PROPERTY_KEY = "java.io.tmpdir";

    /**
     * the ImageInput stream to delegate to.
     */
    private final ImageInputStream delegatee;

    /**
     * Store the underlying method so we can close it when we are closed
     */
    private final CloseListener closer;

    /**
     * Construct the SeekableInputStream and specify whether to use in-memory
     * or file based caches for the pushback mechanism.
     *
     * @param close the closeListener to call when this stream is closed.
     * @param is the underlying input stream to use
     * @param useFile use File based caching rather then memory based caching
     * @throws IOException
     */
    public DefaultSeekableInputStream(
        CloseListener close, InputStream is, boolean useFile)
        throws IOException {
        this.closer = close;
        if (useFile) {
            delegatee = new FileCacheImageInputStream(
                is, new File(System.getProperty(TMP_DIR_PROPERTY_KEY)));
        } else {
            delegatee = new MemoryCacheImageInputStream(is);
        }
    }

    /**
     * Construct the SeekableInputStream based on a file.
     * @param file the file to obtain the stream from
     * @throws IOException
     */
    public DefaultSeekableInputStream(File file) throws IOException {
        this.closer = null;
        this.delegatee = new FileImageInputStream(file);
    }

    // javadoc inherited
    public void close() throws IOException {
        delegatee.close();
        // the line above should have done this but to be sure we do it again.
        if (null != closer) {
            closer.close();
        }
    }

    // javadoc inherited
    public void flush() throws IOException {
        delegatee.flush();
    }

    // javadoc inherited
    public void flushBefore(long pos) throws IOException {
        delegatee.flushBefore(pos);
    }

    // javadoc inherited
    public int getBitOffset() throws IOException {
        return delegatee.getBitOffset();
    }

    // javadoc inherited
    public ByteOrder getByteOrder() {
        return delegatee.getByteOrder();
    }

    // javadoc inherited
    public long getFlushedPosition() {
        return delegatee.getFlushedPosition();
    }

    // javadoc inherited
    public long getStreamPosition() throws IOException {
        return delegatee.getStreamPosition();
    }

    // javadoc inherited
    public boolean isCached() {
        return delegatee.isCached();
    }

    // javadoc inherited
    public boolean isCachedFile() {
        return delegatee.isCachedFile();
    }

    // javadoc inherited
    public boolean isCachedMemory() {
        return delegatee.isCachedMemory();
    }

    // javadoc inherited
    public long length() throws IOException {
        return delegatee.length();
    }

    // javadoc inherited
    public void mark() {
        delegatee.mark();
    }

    // javadoc inherited
    public int read() throws IOException {
        return delegatee.read();
    }

    // javadoc inherited
    public int read(byte[] ba) throws IOException {
        return delegatee.read(ba);
    }

    // javadoc inherited
    public int read(byte[] ba, int off, int len) throws IOException {
        return delegatee.read(ba, off, len);
    }

    // javadoc inherited
    public int readBit() throws IOException {
        return delegatee.readBit();
    }

    // javadoc inherited
    public long readBits(int numBits) throws IOException {
        return delegatee.readBits(numBits);
    }

    // javadoc inherited
    public boolean readBoolean() throws IOException {
        return delegatee.readBoolean();
    }

    // javadoc inherited
    public byte readByte() throws IOException {
        return delegatee.readByte();
    }

    // javadoc inherited
    public void readBytes(IIOByteBuffer buf, int len) throws IOException {
        delegatee.readBytes(buf, len);
    }

    // javadoc inherited
    public char readChar() throws IOException {
        return delegatee.readChar();
    }

    // javadoc inherited
    public double readDouble() throws IOException {
        return delegatee.readDouble();
    }

    // javadoc inherited
    public float readFloat() throws IOException {
        return delegatee.readFloat();
    }

    // javadoc inherited
    public void readFully(byte[] ba) throws IOException {
        delegatee.readFully(ba);
    }

    // javadoc inherited
    public void readFully(byte[] ba, int off, int len) throws IOException {
        delegatee.readFully(ba, off, len);
    }

    // javadoc inherited
    public void readFully(char[] ca, int off, int len) throws IOException {
        delegatee.readFully(ca, off, len);
    }

    // javadoc inherited
    public void readFully(double[] da, int off, int len) throws IOException {
        delegatee.readFully(da, off, len);
    }

    // javadoc inherited
    public void readFully(float[] fa, int off, int len) throws IOException {
        delegatee.readFully(fa, off, len);
    }

    // javadoc inherited
    public void readFully(int[] ia, int off, int len) throws IOException {
        delegatee.readFully(ia, off, len);
    }

    // javadoc inherited
    public void readFully(long[] la, int off, int len) throws IOException {
        delegatee.readFully(la, off, len);
    }

    // javadoc inherited
    public void readFully(short[] sa, int off, int len) throws IOException {
        delegatee.readFully(sa, off, len);
    }

    // javadoc inherited
    public int readInt() throws IOException {
        return delegatee.readInt();
    }

    // javadoc inherited
    public String readLine() throws IOException {
        return delegatee.readLine();
    }

    // javadoc inherited
    public long readLong() throws IOException {
        return delegatee.readLong();
    }

    // javadoc inherited
    public short readShort() throws IOException {
        return delegatee.readShort();
    }

    // javadoc inherited
    public int readUnsignedByte() throws IOException {
        return delegatee.readUnsignedByte();
    }

    // javadoc inherited
    public long readUnsignedInt() throws IOException {
        return delegatee.readUnsignedInt();
    }

    // javadoc inherited
    public int readUnsignedShort() throws IOException {
        return delegatee.readUnsignedShort();
    }

    // javadoc inherited
    public String readUTF() throws IOException {
        return delegatee.readUTF();
    }

    // javadoc inherited
    public void reset() throws IOException {
        delegatee.reset();
    }

    // javadoc inherited
    public void seek(long pos) throws IOException {
        delegatee.seek(pos);
    }

    // javadoc inherited
    public void setBitOffset(int bitOffset) throws IOException {
        delegatee.setBitOffset(bitOffset);
    }

    // javadoc inherited
    public void setByteOrder(ByteOrder byteOrder) {
        delegatee.setByteOrder(byteOrder);
    }

    // javadoc inherited
    public int skipBytes(int n) throws IOException {
        return delegatee.skipBytes(n);
    }

    // javadoc inherited
    public long skipBytes(long l) throws IOException {
        return delegatee.skipBytes(l);
    }
}
