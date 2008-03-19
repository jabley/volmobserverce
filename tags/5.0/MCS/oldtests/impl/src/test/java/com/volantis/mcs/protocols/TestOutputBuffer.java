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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/TestOutputBuffer.java,v 1.2 2003/02/06 11:38:48 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 31-Jan-03    Geoff           VBM:2003012101 - Created.
 * 06-May-03    Steve           VBM:2003041606 - Removed setTrim() and 
 *                              isWhitespace() as these are now in 
 *                              AbstractOutputBuffer. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols;

import java.io.StringWriter;
import java.io.Writer;

/**
 * An OutputBuffer which is "designed" for use with test cases.
 * <p>
 * Note that the original "design" for this was simply to collect together all
 * the existing usages demonstrated in the various inner classes which had 
 * previously extended OutputBuffer before the existance of this class,
 * so the code is not necessarily the best at the moment. However, with 
 * continued use and more refactoring, this should evolve into something which 
 * is useful for all test cases.
 * <p> 
 * Make sure you run ALL the test cases if you modify this class! 
 */ 
public class TestOutputBuffer extends AbstractOutputBuffer {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2002.";

    private StringWriter writer = new StringWriter();

    /**
     * @see com.volantis.mcs.protocols.OutputBuffer#getWriter()
     */
    public Writer getWriter() {
        return writer;
    }

    /**
     * @see com.volantis.mcs.protocols.OutputBuffer#getCurrentBuffer()
     */
    public OutputBuffer getCurrentBuffer() {
        return null;
    }

    /**
     * @see com.volantis.mcs.protocols.OutputBuffer#isEmpty()
     */
    public boolean isEmpty() {
        return false;
    }

    public String toString() {
        return writer.toString();
    }

    // Javadoc inherited
    public void writeText(char[] text, int off, int len, boolean preEncoded) {
        // todo - This method needs to be overridden if a functioning
        // buffer is required
    }

    // Javadoc inherited
    public void writeText(char[] text, int off, int len) {
        // todo - This method needs to be overridden if a functioning
        // buffer is required
    }

    // Javadoc inherited
    public void writeText(String text, boolean preEncoded) {
        // todo - This method needs to be overridden if a functioning
        // buffer is required
    }

    // Javadoc inherited
    public void writeText(String text) {
        // todo - This method needs to be overridden if a functioning
        // buffer is required
    }

    //javadoc inherited
    public void handleOpenElementWhitespace() {
    }

    //javadoc inherited
    public void handleCloseElementWhitespace() {
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Dec-05	10562/1	ibush	VBM:2005113001 Fix extra whitespace characters appearing

 01-Dec-05	10517/1	ibush	VBM:2005113001 Fix extra whitespace characters appearing

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 13-Jul-04	4868/1	adrianj	VBM:2004070509 Implemented missing writeText(...) methods in StringOutputBuffer

 13-Jul-04	4862/1	adrianj	VBM:2004070509 Implemented missing writeText(...) methods in StringBuffer

 ===========================================================================
*/
