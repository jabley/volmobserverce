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

import com.volantis.mcs.protocols.widgets.attributes.DisplayAttributes;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;

/**
 * The element object representing <widget:Display> XDIME element.
 */
public class DisplayElement extends WidgetElement {
    
    /**
     * Initializes this element with specified XDIME context.
     * 
     * @param context The XDIME context
     */
    public DisplayElement(XDIMEContextInternal context){
        super(WidgetElements.DISPLAY, context);
        
        protocolAttributes = new DisplayAttributes();
    }
    
    /**
     * @inheritDoc
     */
    protected void initialiseElementSpecificAttributes(
            XDIMEContextInternal context, XDIMEAttributes attributes)
            throws XDIMEException {
        DisplayAttributes displayAttributes = (DisplayAttributes) protocolAttributes;

        String attributeValue = attributes.getValue("", "property");
        
        if (attributeValue != null) {
            displayAttributes.setPropertyReference(new PropertyReferenceImpl(attributeValue));
        }
        
        displayAttributes.setContent(attributes.getValue("", "content"));
        
    }

    // Javadoc inherited
    protected XDIMEResult doFallbackOpen(XDIMEContextInternal context, XDIMEAttributes attributes) {
        return XDIMEResult.SKIP_ELEMENT_BODY;
    }
}
