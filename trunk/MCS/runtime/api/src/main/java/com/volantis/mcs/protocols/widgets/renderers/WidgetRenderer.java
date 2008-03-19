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

import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;

/**
 * Interace for renderer of a widget. 
 * 
 * Widgets that need more sophisticated API, should create their own renderer interface
 * extend this interface.
 * 
 * @mock.generate
 */
public interface WidgetRenderer {
    
    /**
     * Render widget opening using specified protocol, and widget attributes.
     * 
     * @param protocol The protocol used.
     * @param attributes The widget attributes.
     * @throws ProtocolException
     */
    public void renderOpen(VolantisProtocol protocol, MCSAttributes attributes) 
        throws ProtocolException;
    
    /**
     * Render widget closure using specified protocol, and widget attributes.
     * 
     * @param protocol The protocol used.
     * @param attributes The widget attributes.
     * @throws ProtocolException
     */
    public void renderClose(VolantisProtocol protocol, MCSAttributes attributes) 
        throws ProtocolException;

    /**
     * Informs, whether the content included within the widget element should be
     * rendered using specified protocol, or not
     * 
     * @param protocol The protocol used.
     * @param attributes The widget attributes.
     * @return true, if content should be rendered, false otherwise.
     * @throws ProtocolException 
     */
    public boolean shouldRenderContents(VolantisProtocol protocol, MCSAttributes attributes) 
        throws ProtocolException;
    
    /**
     * Convenience method for checking if this widget is supported by specified
     * protocol.
     * 
     * @param protocol The protocol.
     * @return true, if widget is supported, false otherwise.
     */ 
    public boolean isWidgetSupported(VolantisProtocol protocol);

}
