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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.synergetics.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * OutputStream class for splitting a single stream into two, allowing the same
 * information to be written to two locations simultaneously. <p>If an error
 * occurs while writing to one of the child streams, no attempt to synchronize
 * the state with the other stream is made - for example, if an exception is
 * raised while writing a 3k byte array to the first stream, none of that array
 * will be written to the second stream, even if the exception occurred on the
 * last byte being written to the first stream.</p> <p>This class is mostly for
 * use in situations where one of the streams is expected to be extremely
 * reliable and the contents of both streams will be abandoned if an exception
 * occurs during processing.</p>
 *
 * @see TeeWriter
 */
public class TeeOutputStream extends OutputStream {

    /**
     * The primary output stream to which data will be copied.
     */
    private OutputStream primaryOutputStream;

    /**
     * The secondary output stream to which data will be copied.
     */
    private OutputStream secondaryOutputStream;

    /**
     * Create a new tee output stream with the specified primary and secondary
     * output streams.
     *
     * @param primary   The primary output stream
     * @param secondary The secondary output stream
     */
    public TeeOutputStream(OutputStream primary, OutputStream secondary) {
        primaryOutputStream = primary;
        secondaryOutputStream = secondary;
    }

    // Javadoc inherited
    public void write(int b) throws IOException {
        primaryOutputStream.write(b);
        secondaryOutputStream.write(b);
    }

    // Javadoc inherited
    public void write(byte[] b) throws IOException {
        primaryOutputStream.write(b);
        secondaryOutputStream.write(b);
    }

    // Javadoc inherited
    public void write(byte[] b, int off, int len) throws IOException {
        primaryOutputStream.write(b, off, len);
        secondaryOutputStream.write(b, off, len);
    }

    // Javadoc inherited
    public void flush() throws IOException {
        primaryOutputStream.flush();
        secondaryOutputStream.flush();
    }

    // Javadoc inherited
    public void close() throws IOException {
        primaryOutputStream.close();
        secondaryOutputStream.close();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Feb-05	393/4	adrianj	VBM:2005012506 Rendered page cache rework

 11-Feb-05	393/2	adrianj	VBM:2005012506 Added TeeWriter and TeeOutputStream

 ===========================================================================
*/
