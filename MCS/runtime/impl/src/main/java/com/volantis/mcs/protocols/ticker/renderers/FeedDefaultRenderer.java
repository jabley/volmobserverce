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

package com.volantis.mcs.protocols.ticker.renderers;

import java.util.HashMap;
import java.util.Iterator;

import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.widgets.attributes.CarouselAttributes;
import com.volantis.mcs.protocols.widgets.attributes.TickerTapeAttributes;
import com.volantis.mcs.protocols.widgets.internal.attributes.InlineContentAttributes;
import com.volantis.mcs.protocols.widgets.renderers.CarouselDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.TickerTapeDefaultRenderer;

/**
 * Renderer for ItemDisplay element 
 */
public class FeedDefaultRenderer extends ElementDefaultRenderer {
    private InlineContentAttributes inlineContentAttributes;
    
    private boolean isRendering = false;

    private OutputBuffer inlineContentOutputBuffer;

    private OutputBuffer itemTemplateOutputBuffer;
    
    private HashMap propertyNamesMap;

    // Javadoc inherited
    public void doRenderOpen(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {

        if(!isWidgetSupported(protocol)) {
            return;
        }        

        // Require libraries
        requireStandardLibraries(protocol);

        requireLibrary("/vfc-ticker.mscr",protocol);
        
        // Generate an ID for this widget, if not already generated.
        // It'll be used to register this widget.
        if (attributes.getId() == null) {
            attributes.setId(protocol.getMarinerPageContext().generateUniqueFCID());
        }

        inlineContentOutputBuffer = protocol.getOutputBufferFactory().createOutputBuffer();
        
        protocol.getMarinerPageContext().pushOutputBuffer(inlineContentOutputBuffer);
        
        inlineContentAttributes = new InlineContentAttributes();
        
        inlineContentAttributes.copy(attributes);
        
        inlineContentAttributes.setId(protocol.getMarinerPageContext().generateUniqueFCID());
        
        renderWidgetOpen(protocol, inlineContentAttributes);
        
        itemTemplateOutputBuffer = protocol.getOutputBufferFactory().createOutputBuffer();
        
        protocol.getMarinerPageContext().pushOutputBuffer(itemTemplateOutputBuffer);
        
        isRendering = true;
        
        propertyNamesMap = new HashMap();
    }

    // Javadoc inherited
    public void doRenderClose(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {

        if(!isWidgetSupported(protocol)) {
            return;
        }                

        isRendering  = false;

        protocol.getMarinerPageContext().popOutputBuffer(itemTemplateOutputBuffer);
        
        boolean hasTemplate = !itemTemplateOutputBuffer.isEmpty();
        
        protocol.getMarinerPageContext().getCurrentOutputBuffer()
            .transferContentsFrom(itemTemplateOutputBuffer);
        
        renderWidgetClose(protocol, inlineContentAttributes);
        
        protocol.getMarinerPageContext().popOutputBuffer(inlineContentOutputBuffer);

        renderTemplateOutputBuffer(protocol, inlineContentOutputBuffer);
        
        // Prepare Javascript content.
        StringBuffer buffer = new StringBuffer();
        
        // Finally, render the JavaScript part.
        buffer.append(createJavaScriptWidgetRegistrationOpening(attributes.getId()))
            .append("new Ticker.ItemTemplate(")
            .append(createJavaScriptWidgetReference(inlineContentAttributes.getId()))
            .append(",{");

        Iterator iterator = propertyNamesMap.keySet().iterator();
        
        while (iterator.hasNext()) {
            String id = (String) iterator.next();
            String propertyName = (String) propertyNamesMap.get(id);
            
            buffer.append(createJavaScriptString(id))
                .append(": ")
                .append(createJavaScriptString(propertyName));
            
            if (iterator.hasNext()) {
                buffer.append(", ");
            }
        }
        
        propertyNamesMap = null;
        
        buffer.append("})")
            .append(createJavaScriptWidgetRegistrationClosure());
        
        addCreatedWidgetId(attributes.getId());
        
        addUsedWidgetId(inlineContentAttributes.getId());
        
        // Write JavaScript content to DOM.
        writeJavaScript(buffer.toString());
            
        // If the content of the template was not empty, use it.
        // Otherwise don't use empty template but render as if there
        // was no template (icon and title by default).
        if (hasTemplate) {
            TickerTapeDefaultRenderer tickerTapeRenderer = (TickerTapeDefaultRenderer)
                 getWidgetDefaultModule(protocol).getWidgetRenderer(TickerTapeAttributes.class);
            
            tickerTapeRenderer.setItemTemplateId(attributes.getId());
    
            CarouselDefaultRenderer carouselRenderer = (CarouselDefaultRenderer)
                getWidgetDefaultModule(protocol).getWidgetRenderer(CarouselAttributes.class);
       
            carouselRenderer.setItemTemplateId(attributes.getId());
        }
    }   
    
    protected boolean isRendering() {
        return isRendering;
    }
    
    protected void addItemProperty(String id, String name) {
        if (propertyNamesMap != null) {
            propertyNamesMap.put(id, name);
        }
    }
    
    protected void renderTemplateOutputBuffer(VolantisProtocol protocol, OutputBuffer buffer) throws ProtocolException {
        TickerTapeDefaultRenderer tickerTapeRenderer = (TickerTapeDefaultRenderer)
            getWidgetDefaultModule(protocol).getWidgetRenderer(TickerTapeAttributes.class);
   
        tickerTapeRenderer.setItemTemplateOutputBuffer(buffer);

        CarouselDefaultRenderer carouselRenderer = (CarouselDefaultRenderer)
            getWidgetDefaultModule(protocol).getWidgetRenderer(CarouselAttributes.class);

        carouselRenderer.setItemTemplateOutputBuffer(buffer);
    }
}
