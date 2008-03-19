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

package com.volantis.mcs.xdime.widgets;

import com.volantis.mcs.protocols.widgets.attributes.InputAttributes;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextImpl;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;

import java.util.Stack;

public class InputElement extends WidgetElement {
    
    public InputElement(XDIMEContextInternal context){
        super(WidgetElements.INPUT, context);
        protocolAttributes = new InputAttributes();
    }
    
    /**
     * Rewrite InputAttributes attributes to ArrayList
     */
    public void callCloseOnProtocol(XDIMEContextInternal context) throws XDIMEException {
        // Iterate the stack of XDIME elements from top to bottom,
        // looking for first dismissable element. 
        Stack stack = ((XDIMEContextImpl)context).getStack();

        // Start from the top of the stack.
        int elementIndex = stack.size() - 1;
        
        while (elementIndex >= 0) {
            Object element = stack.elementAt(elementIndex);
           
            if (element instanceof InputContainer) {
                ((InputContainer)element).addInput((InputAttributes)protocolAttributes);
                break;
            }
            
            elementIndex--;
        }
        super.callCloseOnProtocol(context);
    } 
    
    protected void initialiseElementSpecificAttributes(XDIMEContextInternal context, XDIMEAttributes attributes) throws XDIMEException {
        InputAttributes inputAttributes = (InputAttributes) protocolAttributes;
        
        // Get value and type attributes
        inputAttributes.setValue(attributes.getValue("", "value"));
        inputAttributes.setType(attributes.getValue("", "type"));
        
        // Get optional property attribute
        String propertyAttributeValue = attributes.getValue("", "property");

        if (propertyAttributeValue != null) {
            inputAttributes.setPropertyReference(new PropertyReferenceImpl(propertyAttributeValue));
        }
    }    
}
