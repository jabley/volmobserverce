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

import java.io.IOException;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.widgets.attributes.LaunchAttributes;

/**
 * Widget renderer for Launch element.
 */
public class LaunchRenderer extends WidgetDefaultRenderer {
       
    /**
     * Open div element for widget:launch and set unique value for id attribute if was not set by user
     *    
     * @throws ProtocolException 
     */        
    public void doRenderOpen(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {

        // Do nothing, if widgets are not supported.
        if(!isWidgetSupported(protocol)) {
            return;
        }
        
        // Do nothing, if launcher content is not to be rendered.
        if (!shouldRenderContents(protocol, attributes)) {
            return;
        }
        
        LaunchAttributes launchAttributes = (LaunchAttributes) attributes;

        Element divElement = openDivElement(attributes.getStyles(),
                getCurrentBuffer(protocol));
        
        String launchId = "";
        if (attributes.getId() == null) {
            launchId = protocol.getMarinerPageContext().generateUniqueFCID();
        } else {
            launchId = attributes.getId();
        }
        attributes.setId(launchId);
        divElement.setAttribute("id", launchId);                
        divElement.setAttribute("onclick", "Widget.getInstance('" + launchAttributes.getWidgetId() + "').show();");
    }

    /**
     * Close div element for widget:launch
     *     
     * @throws ProtocolException 
     */        
    public void doRenderClose(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {

        // Do nothing, if widgets are not supported.
        if(!isWidgetSupported(protocol)) {
            return;
        }        

        // Do nothing, if launcher content is not to be rendered.
        if (!shouldRenderContents(protocol, attributes)) {
            return;
        }

        LaunchAttributes attrs = (LaunchAttributes)attributes;
        DOMOutputBuffer currentBuffer = getCurrentBuffer(protocol);
        
        // store current launcher element
        currentBuffer.saveInsertionPoint();
        
        closeDivElement(currentBuffer);
        
        String textToScript = "Widget.getInstance(" + createJavaScriptString(attrs.getWidgetId()) 
                + ").registerLauncher(" + createJavaScriptString(attrs.getId()) + ");";            
        try {
            writeScriptElement(currentBuffer,textToScript);
        } catch (IOException e) {
            throw new ProtocolException();                
        }
    }
    
    // Javadoc inherited
    public boolean shouldRenderContents(VolantisProtocol protocol, MCSAttributes attributes) {
        // Render launcher content only, 
        // if there exists a widget to launch.
        return ((LaunchAttributes)attributes).getWidgetId() != null;
    }
}
