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

import com.volantis.mcs.wbsax.WBSAXException;

/**
 * A default implementation of WBSAX value processor which just forwards to
 * a {@link com.volantis.mcs.dom2wbsax.WBSAXStringSerialiser}.
 * <p>
 * This is useful for custom value processors which don't want access to each
 * value as a single Java string, but prefer to access it once it has been 
 * split into the various components by the string serialiser, as they can 
 * simply provide a custom value serialiser to the string serialiser.
 */
public class WBSAXDefaultValueProcessor implements WBSAXValueProcessor {
    
    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    private WBSAXStringSerialiser serialiser;

    /**
     * Construct an instance of this class.
     * 
     * @param serialiser the string serialiser which will be used to
     *      parse the value data.
     */ 
    public WBSAXDefaultValueProcessor(WBSAXStringSerialiser serialiser) {
        
        this.serialiser = serialiser;
        
    }

    // Inherit Javadoc.
    public void value(char [] value, int length) throws WBSAXException {
        
        serialiser.parseValue(value, length);
        
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

 02-Oct-03	1469/3	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols

 ===========================================================================
*/
