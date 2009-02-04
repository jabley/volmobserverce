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

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.widgets.attributes.OptionAttributes;

/**
 * Renderer for option element from widgets namespace
 * It is used only inside select element 
 */

public class OptionDefaultRenderer extends WidgetDefaultRenderer {

    private Element optionElement;
    /**
     * Open element widget:option 
     *    
     * @throws ProtocolException 
     */
    protected void doRenderOpen(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {
        
        if(!isWidgetSupported(protocol)) {
            return;
        }        

        OptionAttributes optionAttributes = (OptionAttributes)attributes;
        
        // Render input element.
        DOMOutputBuffer buffer = getCurrentBuffer(protocol);
        
        optionElement = buffer.openStyledElement("option", attributes.getStyles());
                           
        if (optionAttributes.getValue() != null) {
            optionElement.setAttribute("value", optionAttributes.getValue());
        }
        
        String selected = optionAttributes.getSelected(); 
        if (selected != null && selected.equals("selected")) {
            optionElement.setAttribute("selected", selected);
        }                
    }

    /**
     * Close element widget:option 
     *    
     * @throws ProtocolException 
     */        
    protected void doRenderClose(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {

        if(!isWidgetSupported(protocol)) {
            return;
        }        
        
        DOMOutputBuffer currentBuffer = getCurrentBuffer(protocol);
        currentBuffer.closeElement("option");        
    }

}
