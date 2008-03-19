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

package com.volantis.mcs.wbsax;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This is a test stub WBSAXContentHandler. Currently stores the events sent to
 * it in a List which the testcase can then iterate through to check in that
 * they have been generated in the right order.
 * 
 * @deprecated use {@link com.volantis.mcs.wbsax.io.TestDebugProducer} instead,
 *      it is much easier to use. 
 */
public class TestWBSAXContentHandler extends WBSAXDefaultHandler {

    /**
     * The string table
     */
    private StringTable stringTable;

    /**
     * The list of WBSAX events
     */
    private List events;

    /**
     * Default constructor
     */
    public TestWBSAXContentHandler() {
        events = new ArrayList();
    }

    public void startDocument(VersionCode version, PublicIdCode publicId,
            Codec codec, StringTable stringTable, StringFactory strings)
            throws WBSAXException {
        events.add(version);
        events.add(publicId);
        events.add(codec);
        events.add(stringTable);
        this.stringTable = stringTable;
    }

    public void startDocument(VersionCode version, StringReference publicId,
            Codec codec, StringTable stringTable, StringFactory strings)
            throws WBSAXException {
        events.add(version);
        events.add(publicId);
        events.add(codec);
        events.add(stringTable);
        this.stringTable = stringTable;
    }

    public void addAttribute(AttributeStartCode start) throws WBSAXException {
        events.add(start);
    }

    public void addAttribute(StringReference name) throws WBSAXException {
        events.add(name);
    }

    public void addAttributeValue(AttributeValueCode part)
            throws WBSAXException {
        events.add(part);
    }

    public void addAttributeValue(StringReference part) throws WBSAXException {
        events.add(part);
    }

    public void addAttributeValue(WBSAXString part) throws WBSAXException {
        events.add(part);
    }

    /**
     * Returns an iterator containing the events passed into this content handler
     * @return an Iterator to the events
     */
    public Iterator getEvents() {
        return events.iterator();
    }

    /**
     * Returns the string table used by this content handler
     */
    public StringTable getStringTable() {
        return stringTable;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 02-Oct-03	1469/1	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols

 14-Jul-03	790/1	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 14-Jul-03	781/1	geoff	VBM:2003070404 clean up WBSAX

 03-Jul-03	709/1	geoff	VBM:2003070209 hacked port from metis, without synergetics changes

 03-Jul-03	696/1	geoff	VBM:2003070209 clean up WBSAX test cases

 16-Jun-03	372/2	geoff	VBM:2003060609 avoid merge hell by committing on geoffs machine

 13-Jun-03	372/1	chrisw	VBM:2003060609 Implement wmlc url optimiser

 ===========================================================================
*/
