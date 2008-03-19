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
 * $Header: $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 12-May-03    Steve           VBM:2003021815 Created to hold common code
 *                              from Reusable and Replay input streams
 * ----------------------------------------------------------------------------
 */
package com.volantis.testtools.io;

import org.apache.log4j.Category;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * AbstractReusableInputStream
 *
 * @author steve
 *
 */
public abstract class AbstractReusableInputStream extends InputStream {
    /**
     * The log4j object to log to.
     */
    private static Category logger=Category.getInstance(
            "com.volantis.testtools.io.AbstractReusableInputStream");

    /** Stream contents */
    protected byte[] buf;

    /**
     * The index one greater than the last valid character in the input
     * stream buffer.
     */
    protected int count;

    /** The current mark position */
    protected int markpos=-1;

    /** The index of the next character to read from the input stream buffer. */
    protected int pos;

    /** Creates a new instance of AbstractReusableInputStream */
    public AbstractReusableInputStream(InputStream is) {
        ByteArrayOutputStream os=new ByteArrayOutputStream();
        count=0;
        pos=0;

        try {
            int ch;

            while ((ch=is.read())!=-1) {
                os.write(ch);
            }

            buf=os.toByteArray();
            count=buf.length;
        } catch (IOException ioe) {
            if (logger.isDebugEnabled()) {
                logger.debug("Error opening stream", ioe);
            }

            buf=null;
            count=0;
        }
    }

    /** Returns the number of bytes that can be read (or skipped over) from
     * this input stream without blocking by the next caller of a method for
     * this input stream.  The next caller might be the same thread or
     * another thread.
     * @return     the number of bytes that can be read from this input stream
     *             without blocking.
     */
    public int available()
        throws IOException {
        if (buf==null) {
            return 0;
        } else {
            return count - pos;
        }
    }

    /** Closes this input stream and releases any system resources associated
     * with the stream.
     *
     * <p> The <code>close</code> method of <code>InputStream</code> does
     * nothing.
     *
     * @exception  IOException  if an I/O error occurs.
     *
     */
    public void close()
        throws IOException {
    }

    /** Marks the current position in this input stream. A subsequent call to
     * the <code>reset</code> method repositions this stream at the last marked
     * position so that subsequent reads re-read the same bytes.
     * @param   readlimit   the maximum limit of bytes that can be read before
     *                      the mark position becomes invalid.
     */
    public synchronized void mark(int readlimit) {
        markpos=pos;
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
        return true;
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
    public abstract int read()
        throws IOException;

    /** Repositions this stream to the position at the time the
     * <code>mark</code> method was last called on this input stream.
     */
    public synchronized void reset()
        throws IOException {
        if (markpos!=-1) {
            pos=markpos;
            markpos=-1;
        } else {
            pos=0;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 24-Feb-05	7129/1	philws	VBM:2005011701 Ensure logger info, warn and error calls are localizable

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
