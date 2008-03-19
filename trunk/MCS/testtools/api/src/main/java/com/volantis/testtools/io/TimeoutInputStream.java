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
 * $Header: /src/voyager/com/volantis/testtools/io/TimeoutInputStream.java,v 1.1 2003/02/28 17:07:44 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 27-Feb-03    Steve           VBM:2003021815 An input stream decorator that
 *                              times out on reads. If the timeout value is
 *                              0 then all reads will error immediately but
 *                              if the timeout value is set, then that many
 *                              milliseconds will elapse before the timeout.
 * 12-May-03    Steve           VBM:2003021815 Reformatted with Jalopy. mark()
 *                              and reset() should be declared as synchronized
 * ----------------------------------------------------------------------------
 */
package com.volantis.testtools.io;

import org.apache.log4j.Category;

import java.io.IOException;
import java.io.InputStream;


public class TimeoutInputStream extends InputStream {
    /** Copyright */
    private static String mark="(c) Volantis Systems Ltd 2000.";

    /**
     * The log4j object to log to.
     */
    private static Category logger=Category.getInstance(
            "com.volantis.mcs.whatever.TimeoutInputStream");

    /** The decorated stream. */
    private InputStream in=null;

    /** Timeout in milliseconds */
    private long timeout;

    /** Creates a new instance of TimeoutInputStream */
    public TimeoutInputStream(InputStream is) {
        this(is, 0);
    }

    /** Creates a new instance of TimeoutInputStream */
    public TimeoutInputStream(InputStream is, long milliSecs) {
        in=is;
        timeout=milliSecs;
    }

    /** Returns the number of bytes that can be read (or skipped over) from
     * this input stream without blocking by the next caller of a method for
     * this input stream.  The next caller might be the same thread or or
     * another thread.
     *
     * @return     the number of bytes that can be read from this input stream
     *             without blocking.
     * @exception  IOException  if an I/O error occurs.
     *
     */
    public int available()
        throws IOException {
        return in.available();
    }

    /** Marks the current position in this input stream. A subsequent call to
     * the <code>reset</code> method repositions this stream at the last marked
     * position so that subsequent reads re-read the same bytes.
     *
     * @param   readlimit   the maximum limit of bytes that can be read before
     *                      the mark position becomes invalid.
     */
    public synchronized void mark(int readlimit) {
        if (in.markSupported()) {
            in.mark(readlimit);
        }
    }

    /** Tests if this input stream supports the <code>mark</code> and
     * <code>reset</code> methods. Whether or not <code>mark</code> and
     * <code>reset</code> are supported is an invariant property of a
     * particular input stream instance. The <code>markSupported</code> method
     * of <code>InputStream</code> returns <code>false</code>.
     *
     * @return  <code>true</code> if this stream instance supports the mark
     *          and reset methods; <code>false</code> otherwise.
     */
    public boolean markSupported() {
        return in.markSupported();
    }

    /** Reads the next byte of data from the input stream. The value byte is
     * returned as an <code>int</code> in the range <code>0</code> to
     * <code>255</code>. If no byte is available because the end of the stream
     * has been reached, the value <code>-1</code> is returned. This method
     * blocks until input data is available, the end of the stream is detected,
     * or an exception is thrown.
     * @return     the next byte of data, or <code>-1</code> if the end of the
     *             stream is reached.
     * @exception  IOException  if an I/O error occurs.
     */
    public int read()
        throws IOException {
        // Placed the throw in a finally as most other ways will result in
        // java complaining about unreachable code.
        try {
            if (timeout>0) {
                long now=System.currentTimeMillis();
                long then=now + timeout;

                while (now<then) {
                    now=System.currentTimeMillis();
                }
            }

            return -1;
        } finally {
            throw new IOException("Simulated timeout.");
        }
    }

    /** Repositions this stream to the position at the time the
     * <code>mark</code> method was last called on this input stream.
     */
    public synchronized void reset()
        throws IOException {
        in.reset();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
