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
import java.io.Writer;

/**
 * Writer class for splitting a single writer into two, allowing the same
 * information to be written to two locations simultaneously. <p>If an error
 * occurs while writing to one of the child writers, no attempt to synchronize
 * the state with the other writer is made - for example, if an exception is
 * raised while writing a 3k char array to the first writer, none of that array
 * will be written to the second writer, even if the exception occurred on the
 * last char being written to the first writer.</p> <p>This class is mostly for
 * use in situations where one of the writers is expected to be extremely
 * reliable and the contents of both writers will be abandoned if an exception
 * occurs during processing.</p>
 *
 * @see TeeOutputStream
 */
public class TeeWriter extends Writer {

    /**
     * The primary writer to which data will be copied. <p><strong>NB:</strong>
     * If an exception occurs while writing to the primary writer, then the
     * data that caused the exception is not written to the secondary writer,
     * and the exception is thrown to the caller of the TeeWriter
     * immediately.</p>
     */
    private Writer primaryWriter;

    /**
     * The secondary writer to which data will be copied.
     * <p><strong>NB:</strong> If an exception occurs while writing to the
     * primary writer, then the data that caused the exception is not written
     * to the secondary writer, and the exception is thrown to the caller of
     * the TeeWriter immediately.</p>
     */
    private Writer secondaryWriter;

    /**
     * Create a new tee writer with the specified primary and secondary
     * writers.
     *
     * @param primary   The primary writer
     * @param secondary The secondary writer
     */
    public TeeWriter(Writer primary, Writer secondary) {
        primaryWriter = primary;
        secondaryWriter = secondary;
    }

    // Javadoc inherited
    public void write(int c) throws IOException {
        primaryWriter.write(c);
        secondaryWriter.write(c);
    }

    // Javadoc inherited
    public void write(char cbuf[], int off, int len) throws IOException {
        primaryWriter.write(cbuf, off, len);
        secondaryWriter.write(cbuf, off, len);
    }

    // Javadoc inherited
    public void write(String str, int off, int len) throws IOException {
        primaryWriter.write(str, off, len);
        secondaryWriter.write(str, off, len);
    }

    // Javadoc inherited
    public void write(char cbuf[]) throws IOException {
        primaryWriter.write(cbuf);
        secondaryWriter.write(cbuf);
    }

    // Javadoc inherited
    public void write(String str) throws IOException {
        primaryWriter.write(str);
        secondaryWriter.write(str);
    }

    // Javadoc inherited
    public void flush() throws IOException {
        primaryWriter.flush();
        secondaryWriter.flush();
    }

    // Javadoc inherited
    public void close() throws IOException {
        primaryWriter.close();
        secondaryWriter.close();
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
