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
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Renderer for Carousel widget suitable for HTML protocol
 */
public class ResponseErrorDefaultRenderer extends BaseClientResponseDefaultRenderer {
    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(ResponseDeckDefaultRenderer.class);
    
    /**
     * A buffer for error message content (PCDATA). 
     */
    private DOMOutputBuffer bodyContentBuffer;
    
    // Javadoc inherited
    public void doRenderOpenResponse(VolantisProtocol protocol,
        MCSAttributes attributes) throws ProtocolException {

        bodyContentBuffer = (DOMOutputBuffer) protocol.getOutputBufferFactory().createOutputBuffer();
        
        protocol.getMarinerPageContext().pushOutputBuffer(bodyContentBuffer);
    }
    
    // Javadoc inherited
    public void doRenderCloseResponse(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {        

        protocol.getMarinerPageContext().popOutputBuffer(bodyContentBuffer);

        String message = bodyContentBuffer.getPCDATAValue();
        
        if (message == null) {
            message = "";
        }

        StringBuffer buffer = new StringBuffer();
        
        if (attributes.getId() != null) {
            buffer.append(createJavaScriptWidgetRegistrationOpening(attributes.getId(), true));
        }
        
        buffer.append("new Widget.Response.Error(")
            .append(createJavaScriptString(message))
            .append(")");
        
        if (attributes.getId() != null) {
            buffer.append(createJavaScriptWidgetRegistrationClosure());
        }
        
        writeJavaScript(buffer.toString());
    }
}
