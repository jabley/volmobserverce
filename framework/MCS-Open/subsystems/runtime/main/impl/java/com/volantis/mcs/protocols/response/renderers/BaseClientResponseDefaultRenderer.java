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

import java.io.IOException;

import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.widgets.internal.attributes.ResponseClientAttributes;
import com.volantis.mcs.protocols.widgets.renderers.WidgetDefaultRenderer;
import com.volantis.mcs.runtime.scriptlibrarymanager.WidgetScriptModules;

/**
 * Base abstract superclass for all response renderers, which produces responses
 * for the Client widget.
 */
public abstract class BaseClientResponseDefaultRenderer extends WidgetDefaultRenderer {
    // Javadoc inherited
    public final void doRenderOpen(VolantisProtocol protocol, MCSAttributes attributes) 
            throws ProtocolException {

        if (!isWidgetSupported(protocol)) {
            return;
        }
        
        if (rendersJSON()) {
            // Do nothing, if renderer with render JSON only.
            
        } else {
            require(WidgetScriptModules.BASE_BB_CLIENT, protocol, attributes);            
            // Generate an ID, if it's not already specified.
            if (attributes.getId() == null) {
                attributes.setId(protocol.getMarinerPageContext().generateUniqueFCID());
            }
        }
        
        // Redirect rendering to the subclass.
        doRenderOpenResponse(protocol, attributes);
    }
    
    // Javadoc inherited
    public final void doRenderClose(VolantisProtocol protocol, MCSAttributes attributes) throws ProtocolException {        

        if (!isWidgetSupported(protocol)) {
            return;
        }

        doRenderCloseResponse(protocol, attributes);

        if (rendersJSON()) {
            // Do nothing, if JSON only was rendered.
        } else {
            ResponseClientAttributes responseClientAttributes = new ResponseClientAttributes();
            
            responseClientAttributes.setContent(attributes.getId());
            
            renderWidgetOpen(protocol, responseClientAttributes);
            
            renderWidgetClose(protocol, responseClientAttributes);
        }
    }
    
    /**
     * Renders opening of the widget on specified protocol, using specified attributes.
     * 
     * @param protocol The protocol used for rendering.
     * @param attributes The widget attributes.
     * @throws ProtocolException
     */
    abstract protected void doRenderOpenResponse(VolantisProtocol protocol, MCSAttributes attributes) 
        throws ProtocolException;
    
    /**
     * Renders closure of the widget on specified protocol, using specified attributes.
     * 
     * @param protocol The protocol used for rendering.
     * @param attributes The widget attributes.
     * @throws ProtocolException
     */
    abstract protected void doRenderCloseResponse(VolantisProtocol protocol, MCSAttributes attributes) 
        throws ProtocolException;
    
    /**
     * Indicates, whether this renderer renders JSON only.
     * If so, the size of the response may be optimized.
     * 
     * @return True, if this renderer renders JSON only.
     */
    protected boolean rendersJSON() {
        return false;
    }
    
    /**
     * Renders JSON.
     * 
     * @param protocol The protocol for rendering.
     * @param json The string with JSON.
     * @throws ProtocolException 
     */
    protected void renderJSON(VolantisProtocol protocol, String json) throws ProtocolException {
        try {
            protocol.getContentWriter().write(json);
        } catch (IOException e) {
            throw new ProtocolException("Error rendering JSON.");
        }
    }
}
