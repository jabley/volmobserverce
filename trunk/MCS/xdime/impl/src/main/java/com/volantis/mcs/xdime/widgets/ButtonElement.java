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

import com.volantis.mcs.protocols.widgets.attributes.ButtonAttributes;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;

/**
 * The element object representing <widget:button> XDIME element.
 */
public class ButtonElement extends WidgetElement {
    
    /**
     * Initializes this element with specified XDIME context.
     * 
     * @param context The XDIME context
     */
    public ButtonElement(XDIMEContextInternal context){
        super(WidgetElements.BUTTON, context);
        
        protocolAttributes = new ButtonAttributes();
    }
    
    /**
     * @inheritDoc
     */
    protected void initialiseElementSpecificAttributes(
            XDIMEContextInternal context, XDIMEAttributes attributes)
            throws XDIMEException {
        
        ButtonAttributes buttonAttributes = (ButtonAttributes) protocolAttributes;

        String attributeValue = attributes.getValue("", "action");
        
        if (attributeValue != null) {
            buttonAttributes.setActionReference(new ActionReferenceImpl(attributeValue));
        }
    }

    // Javadoc inherited
    protected XDIMEResult doFallbackOpen(XDIMEContextInternal context, XDIMEAttributes attributes) {
        return XDIMEResult.SKIP_ELEMENT_BODY;
    }
}
