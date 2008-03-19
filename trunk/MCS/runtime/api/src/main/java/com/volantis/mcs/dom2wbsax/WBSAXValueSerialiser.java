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
import com.volantis.mcs.wbsax.Extension;
import com.volantis.mcs.dom2wbsax.WBSAXProcessorContext;

/**
 * Instances of this class serialise the various components that a MCSDOM 
 * string value (eg text value or attribute value) may contain into the 
 * relevant WBSAX events.
 * <p>
 * The MCS DOM is simpler than the WBDOM, and thus we only have to worry 
 * about:
 * <ul>
 *   <li>inline strings
 *   <li>character entities
 *   <li>extension strings (WML variables)
 * </ul>
 * In particular, MCS DOM doesn't understand string references so we don't need
 * to worry about that at all at this stage.
 */ 
public abstract class WBSAXValueSerialiser {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * The shared processor context used by this serialiser.
     */ 
    protected WBSAXProcessorContext context;
    
    /**
     * Construct an instance of this class using the shared context provided.
     * 
     * @param context the shared processor context to use.
     */ 
    protected WBSAXValueSerialiser(WBSAXProcessorContext context) {
        this.context = context;
    }

    /**
     * Serialise a string value to the content handler.  
     * 
     * @param chars Characters to add
     * @param offset offset into char array
     * @param length length to write
     */
    public abstract void addString(char[] chars, int offset, int length)
        throws WBSAXException;
    
    /**
     * Serialise an entity value.
     * 
     * @param c The char to create the entity code from
     */
    public abstract void addEntity(char c)
        throws WBSAXException;
    
    /**
     * Serialise a extension string value.
     *  
     * @param chars Characters to add
     * @param offset offset into char array
     * @param length length to write
     * @param code The extension code.
     */
    public abstract void addExtensionString(Extension code, char[] chars,
            int offset, int length)
        throws WBSAXException;
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-May-05	8005/2	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 06-Oct-03	1469/4	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols (fix rework stuff from phil)

 02-Oct-03	1469/2	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols

 ===========================================================================
*/
