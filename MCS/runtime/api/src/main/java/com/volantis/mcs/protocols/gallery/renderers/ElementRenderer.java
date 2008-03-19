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

package com.volantis.mcs.protocols.gallery.renderers;

import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;

/**
 * Interace for renderers of all Gallery widget elements. 
 * 
 * @mock.generate
 */
public interface ElementRenderer {
    
    /**
     * Renders the opening of the element using the protocol provided.
     * 
     * @param protocol The protocol for rendering.
     * @param attributes The attributes of an element.
     * @throws ProtocolException
     */
    public void renderOpen(VolantisProtocol protocol, MCSAttributes attributes) 
        throws ProtocolException;
    
    /**
     * Renders the closure of the element using the protocol provided.
     * 
     * @param protocol The protocol for rendering.
     * @param attributes The attributes of an element.
     * @throws ProtocolException
     */
    public void renderClose(VolantisProtocol protocol, MCSAttributes attributes) 
        throws ProtocolException;

    /**
     * Indicates, whether the element content should be rendered, or not.
     * 
     * @param protocol The protocol for rendering.
     * @param attributes The element attributes.
     * @return True, if the content of the element should be rendered. 
     */
    public boolean shouldRenderContents(VolantisProtocol protocol, MCSAttributes attributes) 
        throws ProtocolException;
}
