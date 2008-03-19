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

import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.widgets.attributes.MessageAttributes;
import com.volantis.mcs.protocols.widgets.attributes.ValidateAttributes;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;

/**
 * The message element.
 */
public class MessageElement extends WidgetElement {
    /**
     * Creates new instance.
     * @param context
     */
    public MessageElement(XDIMEContextInternal context) {
        // Message element is stylable, so use StyledStrategy there.
        // Message element lies inside not-validable validate element,
        // so its validation strategy must be AnywhereStrategy.
        super(WidgetElements.MESSAGE, context);

        protocolAttributes = new MessageAttributes();
    }
    
    /**
     * Returns the message attributes.
     * 
     * @return message attributes.
     */
    public MessageAttributes getMessageAttributes() {
        return (MessageAttributes) protocolAttributes;
    }

    // Javadoc inherited.
    protected void initialiseElementSpecificAttributes(
            XDIMEContextInternal context, XDIMEAttributes attributes)
            throws XDIMEException {
        MessageAttributes messageAttributes = getMessageAttributes();
        
        String messageType = attributes.getValue("", "type");
        
        messageAttributes.setType(messageType);

        if (parent instanceof ValidateElement) {
            ValidateElement validateElement = (ValidateElement) parent;
            
            ValidateAttributes validateAttributes = validateElement.getValidateAttributes();

            VolantisProtocol protocol = getProtocol(context);
            
            // Generate ID for the message element
            if (messageAttributes.getId() == null) {
                messageAttributes.setId(protocol.getMarinerPageContext()
                        .generateUniqueFCID());
            }
            
            // Process the 'type' attribute, and propagate it to the enclosing validate element.
            if ((messageType == null) || messageType.equals("invalid")) {
                validateAttributes.setInvalidMessageElementId(messageAttributes.getId());
            } else if (messageType.equals("empty")) {
                validateAttributes.setEmptyMessageElementId(messageAttributes.getId());
            }
        }
    }

    protected XDIMEResult doFallbackOpen(XDIMEContextInternal context, XDIMEAttributes attributes) {
        // By default do nothing and do not process the body
        return XDIMEResult.SKIP_ELEMENT_BODY;
    }
}
