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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.xdime.response;

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.response.attributes.ResponseTimerAttributes;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.mcs.xdime.widgets.ResponseElements;

/**
 * Implementation of AJAX resposne for Timer widget.  
 */
public class ResponseTimerElement extends ResponseElement {
    public ResponseTimerElement(XDIMEContextInternal context) {
        super(ResponseElements.TIMER, context);
        protocolAttributes = new ResponseTimerAttributes();
    }

    // Javadoc inherited
    public XDIMEResult callOpenOnProtocol(
        XDIMEContextInternal context, XDIMEAttributes attributes)
        throws XDIMEException {

        return XDIMEResult.PROCESS_ELEMENT_BODY;
    }

    // Javadoc inherited
    public void callCloseOnProtocol(XDIMEContextInternal context) {
        MarinerPageContext pageContext = getPageContext(context);
        OutputBuffer buffer = pageContext.getCurrentOutputBuffer();
        
        ResponseTimerAttributes responseTimerAttributes = (ResponseTimerAttributes) protocolAttributes;
        
        buffer.writeText("{start: "+responseTimerAttributes.getStartTime());
        buffer.writeText(",stop: "+responseTimerAttributes.getStopTime());
        buffer.writeText("} ");
    }

    protected void initialiseElementSpecificAttributes(XDIMEContextInternal context, XDIMEAttributes attributes) 
        throws XDIMEException {
        
        ResponseTimerAttributes responseTimerAttributes = (ResponseTimerAttributes) protocolAttributes;
        /**
         * start-time and stop-time attributes
         */
        responseTimerAttributes.setStartTime(new Integer(attributes.getValue("", "start-time")));
        responseTimerAttributes.setStopTime(new Integer(attributes.getValue("", "stop-time")));
        super.initialiseElementSpecificAttributes(context, attributes);
    }
    
}
