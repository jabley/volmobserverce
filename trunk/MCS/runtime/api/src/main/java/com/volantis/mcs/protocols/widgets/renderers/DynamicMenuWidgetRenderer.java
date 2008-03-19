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

import com.volantis.mcs.protocols.ListItemAttributes;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.NavigationListAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;

/**
 * Interface of renderer of DynamicMenu widget
 * 
 * @mock.generate base="WidgetRenderer"
 */
public interface DynamicMenuWidgetRenderer extends WidgetRenderer {

    /**
     * Generate Dynamic Menu markup on opening of nl element  
     */
    public void renderNlOpen(VolantisProtocol protocol, NavigationListAttributes attributes)
        throws ProtocolException;

    /**
     * Generate Dynamic Menu markup on closing of nl element  
     */
    public void renderNlClose(VolantisProtocol protocol, NavigationListAttributes attributes)
        throws ProtocolException;

    /**
     * Generate Dynamic Menu markup on opening of label element  
     */
    public boolean renderLabelOpen(VolantisProtocol protocol, MCSAttributes attributes) 
        throws ProtocolException;

    /**
     * Generate Dynamic Menu markup on closing of label element  
     */
    public void renderLabelClose(VolantisProtocol protocol, MCSAttributes attributes) 
        throws ProtocolException;
    
    /**
     * Generate Dynamic Menu markup on opening of li element  
     */
    public void renderLiOpen(VolantisProtocol protocol, ListItemAttributes attributes)
        throws ProtocolException;

    /**
     * Generate Dynamic Menu markup on closing of li element  
     */
    public void renderLiClose(VolantisProtocol protocol, ListItemAttributes attributes) 
        throws ProtocolException;
}
