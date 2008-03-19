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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.migrate.impl.framework.io;

import com.volantis.synergetics.log.LogDispatcher;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * Default implementation of {@link StreamBuffer}.
 */
public class DefaultStreamBuffer implements StreamBuffer {

    /**
     * The output stream given to the client.
     */
    private ClosableBufferOutputStream bufferOutput;

    /**
     * The input stream given to the client.
     */
    private ClosableBufferInputStream bufferInput;

    /**
     * Initialise.
     */
    public DefaultStreamBuffer() {

        bufferOutput = new ClosableBufferOutputStream();
    }

    // Javadoc inherited.
    public OutputStream getOutput() {

        return bufferOutput;
    }

    // Javadoc inherited.
    public InputStream getInput() {

        if (bufferOutput.isClosed()) {
            if (bufferInput == null) {
                bufferInput = new ClosableBufferInputStream(bufferOutput.toByteArray());
            } else {
                if (bufferInput.isClosed()) {
                    throw new IllegalStateException("Cannot get input once " +
                            "closed");
                }
            }
        } else {
            throw new IllegalStateException(
                    "Cannot read buffer input until output has been closed");
        }
        return bufferInput;
    }

    // NOTE: maybe these could be refactored to be filter streams:
    // CloseableOutputStream and CloseableInputStream.

    /**
     * A {@link ByteArrayOutputStream} which enforces {@link #close} and allows
     * callers to see if close has been called.
     */
    private class ClosableBufferOutputStream extends ByteArrayOutputStream {

        private boolean closed;

        public synchronized void write(int b) {
            if (closed) {
                throw new IllegalStateException();
            }
            super.write(b);
        }

        public synchronized void write(byte b[], int off, int len) {
            if (closed) {
                throw new IllegalStateException();
            }
            super.write(b, off, len);
        }

        public void close() throws IOException {
            closed = true;
        }

        boolean isClosed() {
            return closed;
        }
    }

    /**
     * A {@link ByteArrayInputStream} which enforces {@link #close} and allows
     * callers to see if close has been called.
     */
    private class ClosableBufferInputStream extends ByteArrayInputStream {

        private boolean closed;

        public ClosableBufferInputStream(byte buf[]) {
            super(buf);
        }

        public synchronized int read() {
            if (closed) {
                throw new IllegalStateException();
            }
            return super.read();
        }

        public synchronized int read(byte b[], int off, int len) {
            if (closed) {
                throw new IllegalStateException();
            }
            return super.read(b, off, len);
        }

        public void close() throws IOException {
            closed = true;
        }

        boolean isClosed() {
            return closed;
        }

    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-May-05	8036/5	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/3	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/1	geoff	VBM:2005050505 XDIMECP: Migration Framework

 ===========================================================================
*/
