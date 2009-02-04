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
 * Interface of renderer of FieldExpander widget 
 * 
 * @mock.generate base="WidgetRenderer"
 */
public interface FieldExpanderWidgetRenderer extends WidgetRenderer {

    /**
     * Generate Field Expander markup after opening an XFGroup 
     */
    public void renderXFGroupOpen(VolantisProtocol protocol, MCSAttributes attributes)
        throws ProtocolException;

    /**
     * Generate Field Exapnder markup before closing an XFGroup  
     */
    public void renderXFGroupClose(VolantisProtocol protocol, MCSAttributes attributes)        
        throws ProtocolException;

    /**
     * Generate Field Expander markup after opening an XFControl  
     */
    public void renderXFControlOpen(VolantisProtocol protocol, MCSAttributes attributes)
        throws ProtocolException;

    /**
     * Generate Field Expander markup before closing an XFControl  
     */
    public void renderXFControlClose(VolantisProtocol protocol, MCSAttributes attributes)
        throws ProtocolException;    
}
