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
package com.volantis.mcs.protocols.widgets.renderers;

import com.volantis.mcs.protocols.ListItemAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;

/**
 * Interface of renderer of AutocompleteRenderer widget
 * 
 * @mock.generate base="WidgetRenderer"
 */
public interface AutocompleteRenderer extends WidgetRenderer {

    /**
     * Generate Autocomplete response markup on opening of li element  
     */
    public void renderLiOpen(VolantisProtocol protocol, ListItemAttributes attributes)
        throws ProtocolException;

    /**
     * Generate Autocomplete response markup on closing of li element  
     */
    public void renderLiClose(VolantisProtocol protocol, ListItemAttributes attributes) 
        throws ProtocolException;

}
