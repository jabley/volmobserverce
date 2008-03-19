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

package com.volantis.mcs.protocols.widgets.renderers;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.styling.Styles;
import com.volantis.styling.StylingFactory;

public class MessageDefaultRenderer extends WidgetDefaultRenderer {

    // Javadoc inherited
    public void doRenderOpen(VolantisProtocol protocol, MCSAttributes attributes) throws ProtocolException {
        // If protocol does not support Framework Client, do not render
        // anything.
        if(!isWidgetSupported(protocol)) {
            return;
        }

        // Open span element enclosing the message.
        DOMOutputBuffer currentBuffer = getCurrentBuffer(protocol);
        
        Styles emptyStyles = StylingFactory.getDefaultInstance()
        	.createInheritedStyles(
                protocol.getMarinerPageContext()
                .getStylingEngine().getStyles(), DisplayKeywords.INLINE);
        
        Element messageElement = currentBuffer.openStyledElement("span", emptyStyles);
        
        if (attributes.getId() != null) {
            messageElement.setAttribute("id", attributes.getId());
        }
        
        // Open span element with actual message content
        Element contentElement = currentBuffer.openStyledElement("span", attributes.getStyles());
    }

    // Javadoc inherited
    public void doRenderClose(VolantisProtocol protocol, MCSAttributes attributes) throws ProtocolException {
        // If protocol does not support Framework Client, do not render
        // anything.
        if(!isWidgetSupported(protocol)) {
            return;
        }

        DOMOutputBuffer currentBuffer = getCurrentBuffer(protocol);

        // Close span element with message content
        currentBuffer.closeElement("span");

        // Close span element enclosing the message
        currentBuffer.closeElement("span");
    }
    
    public boolean shouldRenderContents(VolantisProtocol protocol, MCSAttributes attributes) {
        return isWidgetSupported(protocol);
    }
}




