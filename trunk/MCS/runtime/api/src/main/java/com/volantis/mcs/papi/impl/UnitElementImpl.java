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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.protocols.OutputBuffer;

import java.io.IOException;
import java.io.Writer;

/**
 * The PAPI Unit element
 * This element really does nothing except exist, simply to allow
 * the marlin unit element to be written as either JSP or XML.
 */
public class UnitElementImpl
        extends AbstractStyledElementImpl {

    private Writer dummyWriter = null;

    /**
     * Start processing the unit element
     *
     * @param context        the current Mariner request context
     * @param papiAttributes the UnitAttributes passed with the element
     * @return always returns PROCESS_ELEMENT_BODY
     * @throws PAPIException never thrown by this element
     */
    public int styleElementStart(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {
        return PROCESS_ELEMENT_BODY;
    }

    // javadoc inherited.
    public int styleElementEnd(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes) {
        return CONTINUE_PROCESSING;
    }


    // Javadoc inherited from AbstractElement
    public Writer getContentWriter(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        MarinerPageContext pageContext =
                ContextInternals.getMarinerPageContext(context);
        // If the current output buffer is null then the output buffer 
        // wrapped by the protocol encoding writer will return null when
        // it's writer is retrieved.
        OutputBuffer ob = pageContext.getCurrentOutputBuffer();
        Writer w = null;
        if (ob == null) {
            // We are going to throw away whatever is written here
            if (dummyWriter == null) {
                dummyWriter = new BlackHoleWriter();
            }
            w = dummyWriter;
        } else {
            w = pageContext.getProtocol().getContentWriter();

        }
        return w;
    }

    // Javadoc inherited from AbstractElement
    public Writer getDirectWriter(MarinerRequestContext context)
            throws PAPIException {
        throw new UnsupportedOperationException();
    }


    /**
     * This writer ignores all characters in that none are written. Any non
     * whitespace character that it receives will throw an exception.
     */
    private class BlackHoleWriter
            extends Writer {

        private boolean isClosed = false;

        // Javadoc Inherited
        public void close() throws IOException {
            if (isClosed) {
                throw new IOException("Writer is already closed");
            }
            isClosed = true;
        }

        // Javadoc Inherited
        public void flush() throws IOException {
        }

        // Javadoc Inherited
        public void write(char[] cbuf, int off, int len) throws IOException {
            // If any non whitespace characters are written to this writer
            // an exception is thrown. Anything else is just swallowed.
            int last = Math.min(off + len, cbuf.length);
            for (int i = off; i < last; i++) {
                if (!Character.isWhitespace(cbuf[i])) {
                    throw new IOException("Non whitespace character written. '"
                            + cbuf[i] + "' (" + (int) cbuf[i] + ")");
                }
            }
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 28-Jun-05	8878/5	emma	VBM:2005062306 rework

 24-Jun-05	8878/1	emma	VBM:2005062306 Building calls to the styling engine into the framework and fixing NPE

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 05-Apr-05	7513/1	geoff	VBM:2003100606 DOMOutputBuffer allows creation of text which renders incorrectly in WML

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 28-Apr-04	3940/3	steve	VBM:2004042001 Unit element broken

 20-Apr-04	3940/1	steve	VBM:2004042001 Unit tag broken

 15-Aug-03	1042/9	steve	VBM:2003073103 Take the response writer from the environment - Is this environmentally friendly code ??

 15-Aug-03	1042/7	steve	VBM:2003073103 Forgot the unit element javadoc

 14-Aug-03	1042/5	steve	VBM:2003073103 Move Writer logic to UNIT papi element from the UNIT tag

 13-Aug-03	1042/3	steve	VBM:2003073103 Implement UNIT element

 13-Aug-03	1042/1	steve	VBM:2003073103 Implement UNIT element

 ===========================================================================
*/
