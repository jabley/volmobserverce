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
package com.volantis.mcs.wbdom;

import com.volantis.mcs.wbsax.WBSAXContentHandler;
import com.volantis.mcs.wbsax.WBSAXException;
import com.volantis.mcs.wbsax.WBSAXFilterHandler;
import com.volantis.mcs.wbsax.NullReferenceResolver;
import com.volantis.mcs.wbdom.io.WBSAXParser;
import com.volantis.mcs.wbdom.io.SerialisationConfiguration;
import com.volantis.mcs.dom.output.SerialisationURLListener;
import com.volantis.mcs.wbdom.io.WBSAXSerialiser;

/**
 * A {@link WBSAXFilterHandler} which translates a WBSAX event stream
 * into a WBDOM using a {@link WBSAXParser} and then back to a WBSAX event 
 * stream again using a {@link WBSAXSerialiser}, purely for testing purposes. 
 * <p>
 * Useful for integrating with the WBSAX test infrastructure which is based on
 * WBSAX streams. 
 */ 
public class TestWBDOMWBSAXFilter extends WBSAXFilterHandler {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    private WBSAXParser parser;
    
    private WBSAXSerialiser serialiser;
    
    public TestWBDOMWBSAXFilter(WBSAXParser parser, 
            WBSAXSerialiser serialiser) {
        super(parser, new NullReferenceResolver());
        // This will turn (input) WBSAX events into a nice WBDOM.
        this.parser = parser;
        // This will turn (output) WBSAX events into some nice WBXML output.
        this.serialiser = serialiser;
    }

    public void endDocument() throws WBSAXException {
        super.endDocument();
        
        // This will serialise the DOM to WBSAX events.
        try {
            serialiser.serialise(parser.getDocument());
        } catch (WBDOMException e) {
            throw new WBSAXException(e);
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

 15-Jul-03	804/1	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/1	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 11-Jul-03	790/1	geoff	VBM:2003070404 merge from mimas and fix rename manually

 11-Jul-03	788/1	geoff	VBM:2003070404 merge from metis and fix rename manually

 11-Jul-03	781/1	geoff	VBM:2003070404 first clean up of WBSAX; javadocs and todos

 10-Jul-03	774/1	geoff	VBM:2003070703 merge from mimas and fix renames manually

 10-Jul-03	770/1	geoff	VBM:2003070703 merge from metis and rename files manually

 10-Jul-03	751/1	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 04-Jul-03	724/1	geoff	VBM:2003070403 first take at cleanup

 ===========================================================================
*/
