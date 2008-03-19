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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.xdime.ticker;

import com.volantis.mcs.protocols.ticker.attributes.ItemPropertyAttributes;
import com.volantis.mcs.xdime.XDIMEContextImpl;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xml.schema.model.ElementType;

import java.util.Stack;

/**
 * Base class for Item Property elements.
 */
public class ItemPropertyElement extends TickerElement {
       
    /**
     * Creates and returns new instance of ItemPropertyElement, 
     * initalised with empty attributes.
     * @param context
     */
    public ItemPropertyElement(ElementType type, XDIMEContextInternal context) {
        super(type, context);
    }  
    
    // Javadoc inherited
    public void callCloseOnProtocol(XDIMEContextInternal context) throws XDIMEException {
        // Iterate the stack of XDIME elements from top to bottom,
        // looking for first ItemDisplay element. 
        Stack stack = ((XDIMEContextImpl)context).getStack();

        // Start from the top of the stack.
        int elementIndex = stack.size() - 1;
        
        while (elementIndex >= 0) {
            Object element = stack.elementAt(elementIndex);
           
            if (element instanceof ItemDisplayElement) {
                ((ItemDisplayElement)element).addItemProperty((ItemPropertyAttributes)protocolAttributes);
                break;
            }
            
            elementIndex--;
        }
        
        super.callCloseOnProtocol(context);
    }
}
