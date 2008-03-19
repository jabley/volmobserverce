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
package com.volantis.mcs.dom2wbsax;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.wbsax.WBSAXException;
import com.volantis.mcs.dom2wbsax.WBSAXElementProcessor;

public class WBSAXIgnoreElementProcessor extends WBSAXElementProcessor {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    public WBSAXIgnoreElementProcessor(
            WBSAXProcessorContext context) {
        super(context);
    }

    public void elementStart(Element element,
            boolean content) throws WBSAXException {
        // Do nothing.
    }

    public void elementEnd(Element element, boolean content)
            throws WBSAXException {
        // Do nothing.
    }

    public void contentStart() throws WBSAXException {
        // Do nothing.
    }

    public void contentEnd() throws WBSAXException {
        // Do nothing.
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-May-05	8005/2	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 02-Oct-03	1469/1	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols

 ===========================================================================
*/
