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
package com.volantis.shared.net.impl.url.file;

import com.volantis.shared.net.impl.TimingOutTask;

import java.io.InputStream;
import java.io.IOException;

/**
 * This class is responsible for providing an {@link InputStream}
 * that can only be read for a limited amount of time.
 */
public class TimeLimitedInputStream extends InputStream {

    /**
     * The input stream we are decorating.
     */ 
    private InputStream delegate;

    /**
     * The task that has been scheduled in order to interrupt the
     * reading of this stream.
     */
    private TimingOutTask timingOutTask;

    /**
     * Initialize the new instance using the given parameters.
     *
     * @param inputStream the input stream to wrap.
     * @param timeoutTask the task that will interrupt the reading of
     * the given <code>inputStream</code>.
     */
    public TimeLimitedInputStream(InputStream inputStream,
                                  TimingOutTask timeoutTask) {
        delegate = inputStream;
        this.timingOutTask = timeoutTask;
    }

    public TimeLimitedInputStream(InputStream inputStream) {
        this(inputStream, null);
    }

    public void setTimerTask(TimingOutTask timeoutTask) {
        this.timingOutTask = timeoutTask;
    }

    // javadoc inherited.
    public int read() throws IOException {
        return delegate.read();
    }

    // javadoc inherited.
    public int available() throws IOException {

        return delegate.available();
    }

    // javadoc inherited.
    public void close() throws IOException {
        try {
            delegate.close();
        }
        finally {
            if (timingOutTask != null) {
                timingOutTask.deactivate();
            }
        }
    }

    // javadoc inherited.
    public synchronized void reset() throws IOException {

        delegate.reset();
    }

    // javadoc inherited.
    public boolean markSupported() {
        return delegate.markSupported();
    }

    // javadoc inherited.
    public synchronized void mark(int readlimit) {
        delegate.mark(readlimit);
    }

    // javadoc inherited.
    public long skip(long n) throws IOException {

        return delegate.skip(n);
    }

    // javadoc inherited.
    public int read(byte b[]) throws IOException {

        return delegate.read(b);
    }

    // javadoc inherited.
    public int read(byte b[], int off, int len) throws IOException {

        return delegate.read(b, off, len);
    }
}
