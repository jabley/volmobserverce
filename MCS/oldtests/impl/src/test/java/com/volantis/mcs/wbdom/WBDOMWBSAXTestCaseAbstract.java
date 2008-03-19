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

import com.volantis.mcs.wbsax.WBSAXTestCaseAbstract;
import com.volantis.mcs.wbsax.WBSAXContentHandler;
import com.volantis.mcs.wbsax.io.WBXMLProducer;
import com.volantis.mcs.wbsax.io.WMLProducer;
import com.volantis.mcs.wbdom.io.WBSAXParser;
import com.volantis.mcs.wbdom.io.WBSAXSerialiser;
import com.volantis.mcs.wbdom.io.SerialisationConfiguration;
import com.volantis.mcs.protocols.ProtocolCharacterEncoder;
import com.volantis.mcs.protocols.EncodingWriter;
import com.volantis.mcs.protocols.wml.WMLVersion1_3;

import java.io.OutputStream;
import java.io.Writer;

/**
 * A abstract test case which exercises {@link WBSAXParser} and 
 * {@link WBSAXSerialiser} using {@link WBSAXTestCaseAbstract} and 
 * {@link com.volantis.mcs.wbsax.WBSAXTestData}.
 * <p>
 * The WBSAX test data infrastructure creates WBSAX sax events streams and 
 * then serialises them. This test case injects a WBSAX filter into the 
 * "pipeline" which parses the incoming WBSAX stream to create a WBDOM and 
 * then serialises that back to WBSAX events.
 */ 
public abstract class WBDOMWBSAXTestCaseAbstract extends WBSAXTestCaseAbstract {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    private static TestURLListener urlListener = new TestURLListener();
    
    public WBDOMWBSAXTestCaseAbstract(String s) {
        super(s);
    }

    protected WBSAXContentHandler createWBXMLProducer(OutputStream output) {

        // We create WBXML by parsing the events into a DOM and then 
        // serialising the DOM back to WBXML.

        WBSAXContentHandler producer = super.createWBXMLProducer(output);

        // This will serialise the DOM to WBSAX events.
        SerialisationConfiguration configuration = createConfiguration();
        WBSAXSerialiser serialiser = new WBSAXSerialiser(producer, 
                configuration, urlListener);
        return new TestWBDOMWBSAXFilter(
                new WBSAXParser(new DefaultWBDOMFactory(), configuration),
                serialiser); 
    }

    protected WBSAXContentHandler createXMLProducer(Writer output) {

        // We create XML by parsing the events into a DOM and then 
        // serialising the DOM back to XML.

        WBSAXContentHandler producer = super.createXMLProducer(output);

        // This will serialise the DOM to WBSAX events.
        SerialisationConfiguration configuration = createConfiguration();
        WBSAXSerialiser serialiser = new WBSAXSerialiser(producer, 
                configuration, urlListener);
        return new TestWBDOMWBSAXFilter(
                new WBSAXParser(new DefaultWBDOMFactory(), configuration),
                serialiser); 
    }

    protected abstract SerialisationConfiguration createConfiguration();

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Sep-03	1336/1	geoff	VBM:2003090301 Provide support for StringLiteral in WMLC

 15-Jul-03	804/1	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/1	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 10-Jul-03	751/2	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 ===========================================================================
*/
