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
package com.volantis.mcs.wbdom.dissection;

import com.volantis.mcs.dissection.dom.OutputDocument;
import com.volantis.mcs.dissection.dom.BinaryOutputDocument;
import com.volantis.mcs.dissection.dom.DocumentStats;
import com.volantis.mcs.wbsax.WBSAXContentHandler;
import com.volantis.mcs.wbsax.io.WBXMLProducer;

import java.io.OutputStream;

/**
 * A WBXML implementation of {@link WBDOMDissectableDocumentTestAbstract}.
 * <p>
 * This class tests WBDOM with WBXML output against the generic test 
 * infrastructure provided by the "accurate" dissector.
 * <p>
 * This extends the parent to create a {@link WBXMLProducer} for binary output, 
 * and uses the parent's binary size information for output size checking.
 */ 
public class WBXMLWBDOMDissectableDocumentTestCase 
        extends WBDOMDissectableDocumentTestAbstract {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    public WBXMLWBDOMDissectableDocumentTestCase(String name) {
        super(name);
    }

    protected WBSAXContentHandler createProducer(OutputDocument output) {
        // Create a producer for WBXML.
        OutputStream outputStream = 
                ((BinaryOutputDocument) output).getOutputStream();
        return new WBXMLProducer(outputStream);
    }

    protected void setOutputSize(DocumentStats statistics, int adjust) {
        // For WMLC the output size does not need to be set since the total
        // cost will be never be less than the document size. 
    }

    protected OutputDocument createOutputDocument() throws Exception {
        return new BinaryOutputDocument();
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 10-Jul-03	751/3	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 ===========================================================================
*/
