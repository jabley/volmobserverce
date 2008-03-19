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
* (c) Volantis Systems Ltd 2004. 
* ----------------------------------------------------------------------------
*/

package com.volantis.mcs.ibm.websphere.mcsi;

/**
 * Class which encapsulates the information which needs to be maintained
 * from the startElement to the endElement methods.
 */
public class MCSIElementStackEntry {
    
    /**
     * The Volantis copyright statement
     */
    private static final String mark =
            "(c) Volantis Systems Ltd 2004. ";
    
    /**
     * The MCSIElement.
     */
    protected MCSIElement element;

    /**
     * The MCSIAttributes.
     */
    protected MCSIAttributes attributes;

    
    /**
     * Create a new instance of MCSIElementStackEntry with the specified
     * MCSIElement and MCSIAttributes values.
     * @param element The MCSIElement
     * @param attrs The MCSIAttributes
     */ 
    public MCSIElementStackEntry(MCSIElement element, MCSIAttributes attrs) {
        this.element = element;
        this.attributes = attrs;
    }
    
    /**
     * Get the MCSIAttributes
     * @return the MCSIAttributes
     */
    public MCSIAttributes getAttributes() {
        return attributes;
    }

    /**
     * Set the MCSIAttributes
     * @param attributes The MCSIAttributes
     */ 
    public void setAttributes(MCSIAttributes attributes) {
        this.attributes = attributes;
    }

    /**
     * Get the MCSIElement
     * @return the MCSIElement
     */ 
    public MCSIElement getElement() {
        return element;
    }

    /**
     * Set the MCSIElement
      * @param element the MCSIElement
     */ 
    public void setElement(MCSIElement element) {
        this.element = element;
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 04-Feb-04	2828/1	ianw	VBM:2004011922 Added MCSI content handler

 ===========================================================================
*/
