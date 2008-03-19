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
package com.volantis.mcs.wbdom;

import com.volantis.mcs.wbsax.AttributeValueVisitor;
import com.volantis.mcs.wbsax.WBSAXException;

/**
 * An implementation of {@link AttributesInternalIterator} which iterates over
 * the attributes, visiting the WBSAX values contained within each one with an 
 * instance of {@link NameVisitor} for the attribute name and an instance of 
 * {@link AttributeValueVisitor} for the attribute values.
 * <p>
 * This can be considered to be a kind of Adaptor which adapts the higher 
 * level "internal iterator" interface to the lower level attribute "name and 
 * values visitor" interfaces.
 */ 
public class VisitorAttributesIterator implements AttributesInternalIterator {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * The name visitor we will use for visiting the name of each attribute.
     */ 
    private NameVisitor nameVisitor;
    
    /**
     * The value visitor we will use for visiting the values of each attribute. 
     */ 
    private AttributeValueVisitor valueVisitor;

    /**
     * Construct an instance of this class with the name and value visitor
     * provided.
     * 
     * @param nameVisitor the visitor to visit the contained attribute names.
     * @param valueVisitor the visitor to visit the contained attribute values.
     */ 
    public VisitorAttributesIterator(NameVisitor nameVisitor, 
            AttributeValueVisitor valueVisitor) {
        this.nameVisitor = nameVisitor;
        this.valueVisitor = valueVisitor;
    }

    // Javadoc inherited.
    public void before() throws WBDOMException {
        // By default, do nothing before visiting the attributes.
        // The sizing/serialising subclasses will override this.
    }

    // Javadoc inherited.
    public void next(WBDOMAttribute attribute) throws WBDOMException {
        // Visit the attribute name,
        attribute.accept(nameVisitor);
        // Visit the attribute value.
        try {
            attribute.accept(valueVisitor);
        } catch (WBSAXException e) {
            throw new WBDOMException(e);
        }
    }

    // Javadoc inherited.
    public void after() throws WBDOMException {
        // By default, do nothing after visiting the attributes.
        // The sizing/serialising subclasses will override this.
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 27-Aug-03	1286/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Proteus)

 26-Aug-03	1284/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Mimas)

 26-Aug-03	1278/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Metis)

 26-Aug-03	1238/1	geoff	VBM:2003082101 Clean up wbdom.dissection

 15-Jul-03	804/1	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/2	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 ===========================================================================
*/
