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

package com.volantis.mcs.protocols.response.renderers;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.response.attributes.ResponseDeckAttributes;
import com.volantis.mcs.protocols.widgets.internal.attributes.JavaScriptArrayAttributes;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Renderer for Carousel widget suitable for HTML protocol
 */
public class ResponseDeckDefaultRenderer extends BaseClientResponseDefaultRenderer {
    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(ResponseDeckDefaultRenderer.class);
    
    /**
     * The list attributes used. 
     */
    private JavaScriptArrayAttributes listAttributes;
    
    // Javadoc inherited
    public void doRenderOpenResponse(VolantisProtocol protocol,
        MCSAttributes attributes) throws ProtocolException {

        if (attributes.getId() == null) {
            attributes.setId(protocol.getMarinerPageContext().generateUniqueFCID());
        }
        
        listAttributes = new JavaScriptArrayAttributes();
        
        listAttributes.setId(protocol.getMarinerPageContext().generateUniqueFCID());
        
        renderWidgetOpen(protocol, listAttributes);
    }
    
    // Javadoc inherited
    public void doRenderCloseResponse(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {        

        renderWidgetClose(protocol, listAttributes);

        ResponseDeckAttributes responseDeckAttributes = (ResponseDeckAttributes) attributes;
        
        StringBuffer buffer = new StringBuffer();
        
        if (attributes.getId() != null) {
            buffer.append(createJavaScriptWidgetRegistrationOpening(attributes.getId()));
            
            addCreatedWidgetId(attributes.getId());
        }
        
        buffer.append("new Widget.Internal.DeckResponse(")
            .append(createJavaScriptWidgetReference(listAttributes.getId()))
            .append(", ")
            .append(responseDeckAttributes.getPagesCount())
            .append(")");
        
        addUsedWidgetId(listAttributes.getId());

        if (attributes.getId() != null) {
            buffer.append(createJavaScriptWidgetRegistrationClosure());
        }
        
        writeJavaScript(buffer.toString());
    }
}
