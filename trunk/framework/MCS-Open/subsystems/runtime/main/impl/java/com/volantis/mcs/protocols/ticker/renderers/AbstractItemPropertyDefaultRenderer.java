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

import java.io.IOException;
import java.io.StringWriter;

import com.volantis.mcs.protocols.DivAttributes;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.SpanAttributes;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.ticker.attributes.FeedAttributes;

/**
 * Abstract renderer for all Item Property elements.
 */
public abstract class AbstractItemPropertyDefaultRenderer extends ElementDefaultRenderer {
    /**
     * Attributes of the rendered element.
     */
    private MCSAttributes elementAttributes;
    
    
     
    // Javadoc inherited
    public void doRenderOpen(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {

        if(!isWidgetSupported(protocol)) {
            return;
        }        

        // Require script module
        require(WIDGET_TICKER, protocol, attributes);        

        // Generate the ID attributes automatically.
        if (attributes.getId() == null) {
            attributes.setId(protocol.getMarinerPageContext().generateUniqueFCID());
        }

        // Render the placeholder HTML element, which may be DIV or SPAN,
        // depending on the type of the property.
        if (getElementName() == "span") {
            openSpanElement(protocol, attributes);
        } else {
            openDivElement(protocol, attributes);
        }
    }

    // Javadoc inherited
    public void doRenderClose(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {

        if(!isWidgetSupported(protocol)) {
            return;
        }                

        if (getElementName() == "span") {
            closeSpanElement(protocol);
        } else {
            closeDivElement(protocol);   
        }
        
        // Render ItemProperty widget if this widget is NOT renderer within Feed widget.
        if (!isRenderingFeed(protocol)) {
            // Prepare Javascript content.
            StringWriter scriptWriter = new StringWriter();
    
            if (attributes.getId() != null) {
                scriptWriter.write(createJavaScriptWidgetRegistrationOpening(attributes.getId()));
                
                addCreatedWidgetId(attributes.getId());
            }
            
            scriptWriter.write("Ticker.createItemProperty({");
    
            scriptWriter.write("id:" + createJavaScriptString(attributes.getId()));
            
            scriptWriter.write(", propertyName:" + createJavaScriptString(getPropertyName()));
            
            scriptWriter.write("})");
    
            if (attributes.getId() != null) {
                scriptWriter.write(createJavaScriptWidgetRegistrationClosure());
            }
            
            // Write JavaScript content to DOM.
            writeJavaScript(scriptWriter.toString());
        } else {
            addItemProperty(protocol, attributes.getId());
        }
    }
    
    /**
     * Returns the property name.
     * 
     * @return The property name.
     */
    abstract protected String getPropertyName();

    /**
     * Returns the element name.
     * 
     * @return The element name.
     */
    abstract protected String getElementName();
    
    /**
     * Returns renderer for the Feed widget.
     * 
     * @param protocol The protocol used.
     * @return The FeedDefaultRenderer.
     * @throws ProtocolException
     */
    private FeedDefaultRenderer getFeedDefaultRenderer(VolantisProtocol protocol) throws ProtocolException {
        return (FeedDefaultRenderer) getTickerDefaultModule(protocol)
                .getElementRenderer(FeedAttributes.class);
    }
    
    /**
     * Returns true, if this widget is rendered within the Feed widget.
     *  
     * @param protocol The protocol used.
     * @return 
     * @throws ProtocolException
     */
    private boolean isRenderingFeed(VolantisProtocol protocol) throws ProtocolException {
        return getFeedDefaultRenderer(protocol).isRendering();
    }

    /**
     * A method invoked to add new property found within the template.
     * 
     * @param protocol The protocol used.
     * @param id The ID of the property element.
     * @throws ProtocolException
     */
    private void addItemProperty(VolantisProtocol protocol, String id) throws ProtocolException {
        getFeedDefaultRenderer(protocol).addItemProperty(id, getPropertyName());
    }
}
