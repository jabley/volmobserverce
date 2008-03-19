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
 * Renderer of map widget.
 * 
 * @mock.generate base="WidgetRenderer"
 */
public interface MapRenderer extends WidgetRenderer {

    // Methods afor rendering the main element (map)
    // inherited from WidgetRenderer
    
    /**
     *  Method for rendering map-view element, called on element open 
     */
    public void renderMapView(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException;
    
    /**
     * Returns id of currently rendered map element. It can be null if no
     * element is rendered.
     */
    public String getCurrentMapId();
    
}
