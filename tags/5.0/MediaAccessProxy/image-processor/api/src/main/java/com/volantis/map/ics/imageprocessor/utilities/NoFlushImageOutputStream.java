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
package com.volantis.map.ics.imageprocessor.utilities;

import com.volantis.map.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;

import java.io.IOException;
import java.nio.ByteOrder;

import javax.imageio.stream.IIOByteBuffer;
import javax.imageio.stream.ImageOutputStream;

/**
 * A delegate class that can be used to wrap an ImageOutputStream such that the
 * underlying IOS does not receive flush instructions.
 */
public class NoFlushImageOutputStream implements ImageOutputStream {

    /**
     * Used to localize the messages in exceptions
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(NoFlushImageOutputStream.class);

    /**
     * The output stream to delegate to
     */
    private ImageOutputStream outputStream;

    /**
     * The current "fake" flush position
     */
    private long flushPos = 0;

    /**
     *
     * @param ios
     */
    public NoFlushImageOutputStream(ImageOutputStream ios) {
        if (null == ios) {
            throw new IllegalArgumentException(
                EXCEPTION_LOCALIZER.format("argument-is-null", "ios"));
        }
        outputStream = ios;
        flushPos = ios.getFlushedPosition();
    }

    // javadoc inherited
    public void writeBit(int bit) throws IOException {
        outputStream.writeBit(bit);
    }

    // javadoc inherited
    public void writeBits(long bits, int numBits) throws IOException {
        outputStream.writeBits(bits, numBits);
    }

    // javadoc inherited
    public void writeChars(char[] c, int off, int len) throws IOException {
        outputStream.writeChars(c, off, len);
    }

    // javadoc inherited
    public void writeDoubles(double[] d, int off, int len) throws IOException {
        outputStream.writeDoubles(d, off, len);
    }

    // javadoc inherited
    public void writeFloats(float[] f, int off, int len) throws IOException {
        outputStream.writeFloats(f, off, len);
    }

    // javadoc inherited
    public void writeInts(int[] i, int off, int len) throws IOException {
        outputStream.writeInts(i, off, len);
    }

    // javadoc inherited
    public void writeLongs(long[] l, int off, int len) throws IOException {
        outputStream.writeLongs(l, off, len);
    }

    // javadoc inherited
    public void writeShorts(short[] s, int off, int len) throws IOException {
        outputStream.writeShorts(s, off, len);
    }

    // javadoc inherited
    public void close() throws IOException {
        outputStream.close();
    }

    // javadoc inherited
    public void flush() throws IOException {
        flushBefore(outputStream.getStreamPosition());
    }

    // javadoc inherited
    public void flushBefore(long pos) throws IOException {
        flushPos = pos;
    }

    // javadoc inherited
    public int getBitOffset() throws IOException {
        return outputStream.getBitOffset();
    }

    // javadoc inherited
    public ByteOrder getByteOrder() {
        return outputStream.getByteOrder();
    }

    // javadoc inherited
    public long getFlushedPosition() {
        return flushPos;
    }

    // javadoc inherited
    public long getStreamPosition() throws IOException {
        return outputStream.getStreamPosition();
    }

    // javadoc inherited
    public boolean isCached() {
        return outputStream.isCached();
    }

    // javadoc inherited
    public boolean isCachedFile() {
        return outputStream.isCachedFile();
    }

    // javadoc inherited
    public boolean isCachedMemory() {
        return outputStream.isCachedMemory();
    }

    // javadoc inherited
    public long length() throws IOException {
        return outputStream.length();
    }

    // javadoc inherited
    public int skipBytes(int n) throws IOException {
        return outputStream.skipBytes(n);
    }

    // javadoc inherited
    public void mark() {
        outputStream.mark();
    }

    // javadoc inherited
    public int read() throws IOException {
        return outputStream.read();
    }

    // javadoc inherited
    public int read(byte[] b) throws IOException {
        return outputStream.read(b);
    }

    // javadoc inherited
    public int read(byte[] b, int off, int len) throws IOException {
        return outputStream.read(b, off, len);
    }

    // javadoc inherited
    public int readBit() throws IOException {
        return outputStream.readBit();
    }

    // javadoc inherited
    public long readBits(int numBits) throws IOException {
        return outputStream.readBits(numBits);
    }

    // javadoc inherited
    public void readBytes(IIOByteBuffer buf, int len) throws IOException {
        outputStream.readBytes(buf, len);
    }

    // javadoc inherited
    public void readFully(char[] c, int off, int len) throws IOException {
        outputStream.readFully(c, off, len);
    }

    // javadoc inherited
    public void readFully(double[] d, int off, int len) throws IOException {
        outputStream.readFully(d, off, len);
    }

    // javadoc inherited
    public void readFully(float[] f, int off, int len) throws IOException {
        outputStream.readFully(f, off, len);
    }

    // javadoc inherited
    public void readFully(int[] i, int off, int len) throws IOException {
        outputStream.readFully(i, off, len);
    }

    // javadoc inherited
    public void readFully(long[] l, int off, int len) throws IOException {
        outputStream.readFully(l, off, len);
    }

    // javadoc inherited
    public void readFully(short[] s, int off, int len) throws IOException {
        outputStream.readFully(s, off, len);
    }

    // javadoc inherited
    public long readUnsignedInt() throws IOException {
        return outputStream.readUnsignedInt();
    }

    // javadoc inherited
    public void reset() throws IOException {
        outputStream.reset();
        flushPos = outputStream.getFlushedPosition();
    }

    // javadoc inherited
    public void seek(long pos) throws IOException {
        outputStream.seek(pos);
    }

    // javadoc inherited
    public void setBitOffset(int bitOffset) throws IOException {
        outputStream.setBitOffset(bitOffset);
    }

    // javadoc inherited
    public void setByteOrder(ByteOrder byteOrder) {
        outputStream.setByteOrder(byteOrder);
    }

    // javadoc inherited
    public long skipBytes(long n) throws IOException {
        return outputStream.skipBytes(n);
    }

    // javadoc inherited
    public boolean readBoolean() throws IOException {
        return outputStream.readBoolean();
    }

    // javadoc inherited
    public byte readByte() throws IOException {
        return outputStream.readByte();
    }

    // javadoc inherited
    public char readChar() throws IOException {
        return outputStream.readChar();
    }

    // javadoc inherited
    public double readDouble() throws IOException {
        return outputStream.readDouble();
    }

    // javadoc inherited
    public float readFloat() throws IOException {
        return outputStream.readFloat();
    }

    // javadoc inherited
    public void readFully(byte b[]) throws IOException {
        outputStream.readFully(b);
    }

    // javadoc inherited
    public void readFully(byte b[], int off, int len) throws IOException {
        outputStream.readFully(b, off, len);
    }

    // javadoc inherited
    public int readInt() throws IOException {
        return outputStream.readInt();
    }

    // javadoc inherited
    public String readLine() throws IOException {
        return outputStream.readLine();
    }

    // javadoc inherited
    public String readUTF() throws IOException {
        return outputStream.readUTF();
    }

    // javadoc inherited
    public long readLong() throws IOException {
        return outputStream.readLong();
    }

    // javadoc inherited
    public short readShort() throws IOException {
        return outputStream.readShort();
    }

    // javadoc inherited
    public int readUnsignedShort() throws IOException {
        return outputStream.readUnsignedShort();
    }

    // javadoc inherited
    public int readUnsignedByte() throws IOException {
        return outputStream.readUnsignedByte();
    }

    // javadoc inherited
    public void write(byte b[]) throws IOException {
        outputStream.write(b);
    }

    // javadoc inherited
    public void write(byte b[], int off, int len) throws IOException {
        outputStream.write(b, off, len);
    }

    // javadoc inherited
    public void write(int b) throws IOException {
        outputStream.write(b);
    }

    // javadoc inherited
    public void writeBoolean(boolean v) throws IOException {
        outputStream.writeBoolean(v);
    }

    // javadoc inherited
    public void writeByte(int v) throws IOException {
        outputStream.writeByte(v);
    }

    // javadoc inherited
    public void writeBytes(String s) throws IOException {
        outputStream.writeBytes(s);
    }

    // javadoc inherited
    public void writeChar(int v) throws IOException {
        outputStream.writeChar(v);
    }

    // javadoc inherited
    public void writeChars(String s) throws IOException {
        outputStream.writeChars(s);
    }

    // javadoc inherited
    public void writeDouble(double v) throws IOException {
        outputStream.writeDouble(v);
    }

    // javadoc inherited
    public void writeFloat(float v) throws IOException {
        outputStream.writeFloat(v);
    }

    // javadoc inherited
    public void writeInt(int v) throws IOException {
        outputStream.writeInt(v);
    }

    // javadoc inherited
    public void writeLong(long v) throws IOException {
        outputStream.writeLong(v);
    }

    // javadoc inherited
    public void writeShort(int v) throws IOException {
        outputStream.writeShort(v);
    }

    // javadoc inherited
    public void writeUTF(String str) throws IOException {
        outputStream.writeUTF(str);
    }

    /**
     * Return the number of bytes written to the underlying stream since
     * it was wrapped in this NoFlush stream.
     *
     * @return the number of bytes written to the underlying stream since the
     * last effective "reset" was performed.
     * @throws IOException
     */
    public long getSize() throws IOException {
        return outputStream.getStreamPosition() - outputStream.getFlushedPosition();
    }
}
