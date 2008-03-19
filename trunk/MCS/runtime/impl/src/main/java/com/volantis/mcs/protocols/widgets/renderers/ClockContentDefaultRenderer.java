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

package com.volantis.mcs.protocols.widgets.renderers;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.SpanAttributes;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Widget renderer for ClockContent widget suitable for HTML protocols.
 */
public class ClockContentDefaultRenderer extends WidgetDefaultRenderer {
    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(ClockContentDefaultRenderer.class);
    
    private SpanAttributes clockContentSpanAttributes;

    // Javadoc inherited.
    public void doRenderOpen(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {

        // If protocol does not support Framework Client, do not render
        // anything.
        if (!isWidgetSupported(protocol)) {
            return;
        }  

        //generate id if not specified
        if (attributes.getId() == null) {
            attributes.setId(protocol.getMarinerPageContext()
                    .generateUniqueFCID());
        } 
        
        clockContentSpanAttributes = new SpanAttributes();
        clockContentSpanAttributes.copy(attributes);
        
        renderOpenString(protocol, attributes.getId());     
        protocol.writeOpenSpan(clockContentSpanAttributes);
    }

    // Javadoc inherited.
    public void doRenderClose(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {

        // If protocol does not support Framework Client, do not render
        // anything.
        if (!isWidgetSupported(protocol)) {
            return;
        }
        protocol.writeCloseSpan(clockContentSpanAttributes);
        renderCloseString(protocol);
	}
    
    public boolean shouldRenderContents(VolantisProtocol protocol, MCSAttributes attributes) 
            throws ProtocolException {
        return isWidgetSupported(protocol);
    }
}
