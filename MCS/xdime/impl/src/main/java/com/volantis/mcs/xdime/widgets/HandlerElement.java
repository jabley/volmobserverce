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

import com.volantis.mcs.protocols.widgets.attributes.HandlerAttributes;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;

/**
 * The element object representing <widget:handler> XDIME element.
 */
public class HandlerElement extends WidgetElement {
    
    /**
     * Initializes this element with specified XDIME context.
     * 
     * @param context The XDIME context
     */
    public HandlerElement(XDIMEContextInternal context){
        super(WidgetElements.HANDLER, context);
        
        protocolAttributes = new HandlerAttributes();
    }
    
    // Javadoc inherited
    protected void initialiseElementSpecificAttributes(
            XDIMEContextInternal context, XDIMEAttributes attributes)
            throws XDIMEException {

        // Initialize event and action attributes.
        String eventAttributeValue = attributes.getValue("", "event");
        
        String actionAttributeValue = attributes.getValue("", "action");
        
        if (eventAttributeValue == null) {
            throw new XDIMEException("No event attribute.");
        }
        
        if (actionAttributeValue == null) {
            throw new XDIMEException("No action attribute.");
        }
        
        HandlerAttributes handlerAttributes = (HandlerAttributes) protocolAttributes;

        handlerAttributes.setEventReference(new EventReferenceImpl(eventAttributeValue));
        handlerAttributes.setActionReference(new ActionReferenceImpl(actionAttributeValue));
        
        // Initialize isEnabled attribute.
        String isEnabledAttribute = attributes.getValue("", "is-enabled");
        
        if (isEnabledAttribute != null 
                && !isEnabledAttribute.equals("yes") 
                && !isEnabledAttribute.equals("no")) {
            throw new XDIMEException("Invalid value for is-enabled attribute.");
        }

        handlerAttributes.setEnabled(isEnabledAttribute == null || isEnabledAttribute.equals("yes"));
    }

}
