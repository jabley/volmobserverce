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
 * $Header:$
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 29-May-03    Geoff           VBM:2003042905 - Created.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbsax;

import com.volantis.mcs.wbsax.AttributeStartFactory;
import com.volantis.mcs.wbsax.AttributeValueFactory;
import com.volantis.mcs.wbsax.Codec;
import com.volantis.mcs.wbsax.ElementNameFactory;
import com.volantis.mcs.wbsax.StringReferenceFactory;
import com.volantis.mcs.wbsax.StringTable;
import com.volantis.mcs.wbsax.WBSAXContentHandler;
import com.volantis.mcs.wbsax.WBSAXException;
import com.volantis.mcs.wbsax.PublicIdFactory;

/**
 * An abstract class which defines the interface for providing WBSAX test data
 * for instances of {@link com.volantis.mcs.wbsax.WBSAXTestCaseAbstract}.
 * <p>
 * Currently the test data has three different representations which are used
 * for testing:
 * <ul>
 *   <li>Events (generated)
 *   <li>WBXML / WMLC
 *   <li>XML / WML
 * </ul>
 * It would be useful if we added a fourth representation, "Events 
 * (captured)". This would allow for more exhaustive combinations of testing,
 * and Mat has already implemented support for this at least partially in the 
 * class EnumeratedWBSAXContentHandler.
 * <p>
 * This class could perhaps make use of 
 * {@link com.volantis.mcs.wbsax.TokenTable} to standardise the way the
 * factories and related meta data are set up.
 */
public abstract class WBSAXTestData {

    protected Codec codec;
    
    protected PublicIdFactory publicIds;
    
    protected ElementNameFactory elements;

    protected AttributeStartFactory attrStarts;
    
    protected AttributeValueFactory attrValues;
    
    protected StringTable stringTable; 
    
    protected StringFactory strings; 
    
    protected StringReferenceFactory references;

    public WBSAXTestData() {
        codec = new Codec();
        publicIds = new PublicIdFactory();
        elements = new ElementNameFactory();
        attrStarts = new AttributeStartFactory();
        attrValues = new AttributeValueFactory();
        if (requiresStringTable()) {
            stringTable = new StringTable();
        }
        strings = new StringFactory(codec);
        references = new StringReferenceFactory(stringTable, strings);
        initialise();
    }

    public abstract boolean requiresStringTable();
    
    public Codec getCodec() {
        return codec;
    }

    public StringFactory getStrings() {
        return strings;
    }

    public PublicIdFactory getPublicIds() {
        return publicIds;
    }

    public ElementNameFactory getElements() {
        return elements;
    }

    public AttributeStartFactory getAttrStarts() {
        return attrStarts;
    }

    public AttributeValueFactory getAttrValues() {
        return attrValues;
    }

    protected abstract void initialise();
    
    /** Generate the events to create the data */
    public abstract void fireEvents(WBSAXContentHandler handler, 
            boolean bufferBefore) throws WBSAXException;

    /** Return the XML expected from the events */
    public abstract String getXML();

    /** Return the WBXML expected from the events */
    public abstract int[] getBytes();

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-Jul-03	790/1	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 14-Jul-03	781/1	geoff	VBM:2003070404 clean up WBSAX

 03-Jul-03	709/2	geoff	VBM:2003070209 hacked port from metis, without synergetics changes

 03-Jul-03	696/4	geoff	VBM:2003070209 clean up WBSAX test cases

 ===========================================================================
*/
