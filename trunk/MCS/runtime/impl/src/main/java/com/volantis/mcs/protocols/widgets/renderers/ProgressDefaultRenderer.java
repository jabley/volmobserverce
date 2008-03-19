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
import com.volantis.mcs.protocols.widgets.attributes.ProgressAttributes;
import com.volantis.mcs.protocols.widgets.attributes.RefreshAttributes;

/**
 * Renderer for Progress Bar widget.
 * 
 */
public class ProgressDefaultRenderer extends WidgetDefaultRenderer {

    // TODO: to be removed when styling plumbing is ready
    private String getIntegerPart(String value)
    {
        if(value.matches("[0-9]*.*")) {
            return value.split("[^0-9]")[0];
        }
        else {
            return "''";
        }
    }

    public void doRenderOpen(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {

        if(!isWidgetSupported(protocol)) {
            return;
        }

        requireStandardLibraries(protocol);
        requireLibrary("/vfc-transitions.mscr",protocol);
        requireLibrary("/vfc-progressbar.mscr",protocol);

        if(attributes.getId() == null){
            attributes.setId(
                   protocol.getMarinerPageContext().generateUniqueFCID());
        }        
        Element divElement = openDivElement(attributes.getStyles(),getCurrentBuffer(protocol));
        divElement.setAttribute("id", attributes.getId());
    }
    
    public void doRenderClose(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {
        if(!isWidgetSupported(protocol)) {
            return;
        }
        
        DOMOutputBuffer currentBuffer = getCurrentBuffer(protocol);     
        closeDivElement(currentBuffer);
        String textToScript;
        String textRefreshAttr = "";
        
        RefreshAttributes refreshAttributes = ((ProgressAttributes)attributes).getRefreshAttributes();
        if(refreshAttributes != null) {               
            textRefreshAttr = "refreshURL: "+ createJavaScriptString(refreshAttributes.getSrc())+
            ", refreshInterval: "+ getIntegerPart(refreshAttributes.getInterval()) + ", ";
        }
        textToScript = new StringBuffer("Widget.register(")
            .append(createJavaScriptString(attributes.getId()))
            .append(",new Widget.ProgressBar(")
            .append(createJavaScriptString(attributes.getId())).append(", {") 
            .append(textRefreshAttr)             
            .append(getDisappearableOptions(attributes))
            .append(", ").append(getAppearableOptions(attributes))
            .append("}));")
            .toString();
        try {
            writeScriptElement(currentBuffer, textToScript);
        } catch (IOException e) {
            throw new ProtocolException(e);
        }
    }
}
