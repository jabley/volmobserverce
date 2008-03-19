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

package com.volantis.mcs.marlin.sax;

import com.volantis.mcs.integration.iapi.IAPIAttributes;
import com.volantis.mcs.integration.iapi.IAPIElement;

/**
 * Class which encapsulates the information which needs to be maintained
 * from the startElement to the endElement methods.
 */
public class IAPIElementStackEntry {
    
    /**
     * The Volantis copyright statement
     */
    private static final String mark =
            "(c) Volantis Systems Ltd 2003. ";
    
    /**
     * The IAPIElement.
     */
    protected IAPIElement element;

    /**
     * The IAPIAttributes.
     */
    protected IAPIAttributes attributes;

    
    /**
     * Create a new instance of IAPIElementStackEntry with the specified
     * IAPIElement and IAPIAttributes values.
     * @param element The IAPIElement
     * @param attrs The IAPIAttributes
     */ 
    public IAPIElementStackEntry(IAPIElement element, IAPIAttributes attrs) {
        this.element = element;
        this.attributes = attrs;
    }
    
    /**
     * Get the IAPIAttributes
     * @return the IAPIAttributes
     */
    public IAPIAttributes getAttributes() {
        return attributes;
    }

    /**
     * Set the IAPIAttributes
     * @param attributes The IAPIAttributes
     */ 
    public void setAttributes(IAPIAttributes attributes) {
        this.attributes = attributes;
    }

    /**
     * Get the IAPIElement
     * @return the IAPIElement
     */ 
    public IAPIElement getElement() {
        return element;
    }

    /**
     * Set the IAPIElement
      * @param element the IAPIElement
     */ 
    public void setElement(IAPIElement element) {
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

 22-Jul-03	833/1	adrian	VBM:2003071902 added marlin support for invocation elements

 ===========================================================================
*/
