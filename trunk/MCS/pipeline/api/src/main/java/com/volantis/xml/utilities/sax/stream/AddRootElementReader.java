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
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who         Description
 * -----------  ----------- -------------------------------------------------
 * 07-May-2003  Sumit       VBM:2003050606 - Created
 * 30-May-03    Byron       VBM:2003022819 - Modified read() to correctly
 *                          read past the buffer size.
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.utilities.sax.stream;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;

/**
 * A reader that wraps the data of a wrapped reader
 * in a < fragment>....< /fragment> root element pair
 */
public class AddRootElementReader extends Reader {

    AddRootElementStreamCommand delegate;

    /**
     * Public constructor
     * @param toWrap InputStream that the actual data comes from
     * @throws IOException
     */
    public AddRootElementReader(Reader toWrap) throws IOException {
        delegate = new AddRootElementReaderCommand(toWrap);
    }

    /**
     * This constructor takes a underlying stream that contains the fragment to
     * wrap and a Map of prefix->url namespaces. All entries in the map are
     * (String)->(String). If the map is null or empty then no namespaces will
     * be added to the underlying fragment."" prefix is the default namespace
     * to use.
     *
     * @param toWrap the underlying reader to use
     * @param fragmentNamespace the namespace of the "fragment" element that
     * is inserted
     * @param namespaces the map of namespaces
     */
    public AddRootElementReader(Reader toWrap, String fragmentNamespace,
                                Map namespaces) throws IOException {
        delegate = new AddRootElementReaderCommand(
            toWrap, fragmentNamespace, namespaces);
    }

    // Do not allow the default constructor to be called
    protected AddRootElementReader() throws IOException {
    }

    public int read() throws IOException {
        return delegate.read();
    }

    public boolean markSupported() {
        return false;
    }

    public synchronized void reset() throws IOException {
        delegate.reset();
    }

    public int available() throws IOException {
        return delegate.available();
    }

    /* (non-Javadoc)
     * @see java.io.Reader#read(char[], int, int)
     */
    public int read(char[] cbuf, int off, int len) throws IOException {
        // Catch all for end of stream
        if (delegate.getCurrentState() == AddRootElementStreamCommand.EOF) {
            return -1;
        }

        // Read as much data as will fit in the buffer supplied or
        // until the read() method returns EOF
        int counter = off;
        for (int value = read(); (value != -1) && ((counter - off) < len);) {
            cbuf[counter++] = (char)value;
            if ((counter - off) < len) {
                value = read();
            }
        }

        // return the number of bytes we read
        return counter - off;
    }

    /**
     * Calls the close method on the wrapped reader
     */
    public void close() throws IOException {
        delegate.close();
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 16-Jun-03	23/5	byron	VBM:2003022819 Update to get jsp TLD files with correct merge

 13-Jun-03	23/3	byron	VBM:2003022819 Integration complete

 13-Jun-03	78/1	philws	VBM:2003061205 Added some JSP test pages and fixed some tag bugs

 ===========================================================================
*/
