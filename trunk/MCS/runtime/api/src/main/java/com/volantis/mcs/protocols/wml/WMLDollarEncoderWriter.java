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
 * $Header: /src/voyager/com/volantis/mcs/protocols/wml/Attic/WMLDollarEncoderWriter.java,v 1.1.2.1 2003/04/16 15:41:28 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-Apr-03    steve              VBM:2003041501 Encode a WML stream
 * 28-May-03    Steve           VBM:2003042206 - Patch 2003041501 from Metis
 * 30-May-03    Mat             VBM:2003042911 Add a null to the end of the
 *                              the string in write so that variables on the
 *                              end of a line are parsed correctly.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.wml;

import com.volantis.mcs.protocols.OutputBufferWriter;

import java.io.IOException;
import java.io.Writer;


/**
 * A Writer decorator which encodes WML Dollar character usage and passes it on
 * to an underlying writer. This writer handles raw WML and encodes it as
 * follows.
 * $$ is replaced with a single $ character.
 * $value is replaced with WMLV_NOBRACKETS value WMLV_NOBRACKETS
 * $(value) is replaced with WMLV_BRACKETED value WMLV_BRACKETED
 * $(value:escape) $(value:e) is replaced with WMLV_BRACKETED value WMLV_ESCAPE
 * $(value:unesc) $(value:u) is replaced with WMLV_BRACKETED value WMLV_UNESC
 * $(value:noesc) $(value:n) is replaced with WMLV_BRACKETED value WMLV_NOESC
 * <p/>
 * Note that the WMLV_ codes are single characters defined in the WMLVariable
 * interface.
 *
 * @author steve
 * @see com.volantis.mcs.protocols.wml.WMLVariable
 *      <p/>
 *      This has been greatly simplified by using the WMLDollarEncoder class to
 *      prevent duplication of logic.
 */
public final class WMLDollarEncoderWriter extends OutputBufferWriter {

    /**
     * The encoder object to encode $ characters into tokens.
     */
    private final WMLDollarEncoder encoder;

    /**
     * Whether or not the stream is open
     */
    private boolean isOpen;

    /**
     * Create a writer that encodes WML $ for our WMLC implementation
     *
     * @param writer the writer we are wrapping.
     */
    public WMLDollarEncoderWriter(Writer writer) {
        super(writer);
        isOpen = true;
        encoder = new WMLDollarEncoder();
    }

    /**
     * Close the stream, flushing it first. Once a stream has been closed,
     * further write() or flush() invocations will cause an IOException to be
     * thrown. Closing a previously-closed stream, however, has no effect.
     */
    public void close()
            throws IOException {
        if (isOpen) {
            flush();
            isOpen = false;
        }
    }

    /**
     * Flush the stream. If the stream has saved any characters from the
     * various write() methods in a buffer, write them immediately to their
     * intended destination. Then, if that destination is another character or
     * byte stream, flush it. Thus one flush() invocation will flush all the
     * buffers in a chain of Writers and OutputStreams.
     */
    public void flush()
            throws IOException {
        encoder.reset();
        getWriter().flush();
    }

    /**
     * Write a portion of an array of characters.
     *
     * @param cbuf - Array of characters
     * @param off  - Offset from which to start writing characters
     * @param len  - Number of characters to write
     * @throws IOException - If an I/O error occurs
     */
    public void write(char[] cbuf, int off, int len)
            throws IOException {
        write(new String(cbuf, off, len));
    }

    /**
     * Write a string.
     *
     * @param s - The string to encode and write
     * @throws IOException - If an I/O error occurs
     */
    public void write(String s) throws IOException {
        if (getWriter() != null) {
            String encoded;
            try {
                encoded = encoder.encode(s);
            } catch (WMLVariableException e) {
                throw new IOException(e.getMessage());
            }
            getWriter().write(encoded);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 25-Mar-04	3386/8	steve	VBM:2004030901 Supermerged and merged back with Proteus

 10-Mar-04	3370/1	steve	VBM:2004030901 Application crashes if protocols element is missing

 23-Jan-04	2736/1	steve	VBM:2003121104 Configurable WMLC and dollar encoding

 22-Jan-04	2685/1	steve	VBM:2003121104 Allow WMLC and special character encoding to be turned off in Mariner Config

 10-Mar-04	3370/1	steve	VBM:2004030901 Application crashes if protocols element is missing

 22-Jan-04	2685/1	steve	VBM:2003121104 Allow WMLC and special character encoding to be turned off in Mariner Config

 23-Jun-03	501/1	steve	VBM:2003061807 Dollar encoding for JSP and Marlin

 05-Jun-03	285/2	mat	VBM:2003042911 Merged with MCS

 ===========================================================================
*/
