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
 * $Header: /src/voyager/com/volantis/testtools/io/ReusableInputStream.java,v 1.2 2003/03/20 15:15:34 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 27-Feb-03    Steve           VBM:2003021815 An input stream decorator that is
 *                              reusable. The stream supports mark() and
 *                              reset() and if there is no mark set, the stream
 *                              will 'rewind' to the start.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * 12-May-03    Steve           VBM:2003021815 Refactored to derive from
 *                              AbstractReusableInputStream. All we need here is
 *                              a constructor and a read method.
 * 12-May-03    Steve           VBM:2003021815 Reformatted with Jalopy
 * ----------------------------------------------------------------------------
 */
package com.volantis.testtools.io;

import java.io.IOException;
import java.io.InputStream;


public class ReusableInputStream extends AbstractReusableInputStream {
    /** Copyright */
    private static String mark="(c) Volantis Systems Ltd 2000.";

    /** Creates a new instance of ReusableInputStream */
    public ReusableInputStream(InputStream is) {
        super(is);
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
        if (pos<count) {
            return (buf[pos++] & 0xff);
        } else {
            return -1;
        }
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
