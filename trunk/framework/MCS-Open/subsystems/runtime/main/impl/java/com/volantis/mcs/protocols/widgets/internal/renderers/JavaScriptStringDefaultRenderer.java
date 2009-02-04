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

package com.volantis.mcs.protocols.widgets.internal.renderers;

import java.io.IOException;
import java.io.StringWriter;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.SpanAttributes;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.widgets.renderers.WidgetDefaultRenderer;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.mcs.runtime.scriptlibrarymanager.WidgetScriptModules;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Renderer for String widget suitable for HTML protocols.
 */
public class JavaScriptStringDefaultRenderer extends WidgetDefaultRenderer {
    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(JavaScriptStringDefaultRenderer.class);
    
    /**
     * Attributes of the span element holding the string content.
     */
    private SpanAttributes placeholderSpanAttributes;

    /**
     * An output buffer, where the string content will be written.
     */
    private DOMOutputBuffer stringOutputBuffer;

    // Javadoc inherited.
    public void doRenderOpen(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {
        DOMProtocol domProtocol = (DOMProtocol) protocol;

        require(WidgetScriptModules.BASE_COMMON,protocol, attributes);        

        // Open a placeholder span element for the widget
        // content, which will converted to the JavaScript string.
        // Generate a HTML ID for it, so that it can be referenced
        // from JavaScript code.
        placeholderSpanAttributes = new SpanAttributes();
        
        placeholderSpanAttributes.copy(attributes);
        
        if (placeholderSpanAttributes.getId() == null) {
            placeholderSpanAttributes.setId(protocol.getMarinerPageContext().generateUniqueFCID());
        }
 
        // Now, this is a hack to better the user experience.
        // In almost most cases, the 'display' stylistic attribute for this
        // element is 'none', because we don't want the user to see rendered
        // string on the page. But in the most common configuration, MCS renders
        // that property into the stylesheet. Some browsers before
        // downloading stylesheets, displays the RAW content of the page,
        // including this string. This looks very messy. That's why we
        // put display:none into the inline styles right here.
        if (attributes.getStyles()
                .getPropertyValues()
                .getComputedValue(StylePropertyDetails.DISPLAY) == DisplayKeywords.NONE) {
            Element element = openSpanElement(protocol, attributes);
        
            element.setAttribute("style", "display:none");
        }
        
        // Create and output buffer for the widget content.
        // This will be an instance of DOMOutputBuffer, because the protocol
        // used is an instance of DOMProtocol.
        stringOutputBuffer = (DOMOutputBuffer) domProtocol.getOutputBufferFactory().createOutputBuffer();

        // Push the buffer on the stack, so that the widget content will be
        // rendered to that buffer.
        protocol.getMarinerPageContext().pushOutputBuffer(stringOutputBuffer);
    }

    // Javadoc inherited.
    public void doRenderClose(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {
        DOMProtocol domProtocol = (DOMProtocol) protocol;

        // Pop the output buffer from the stack. At this moment it contains the
        // content of the widget, which will be rendered to JavaScript string.
        domProtocol.getMarinerPageContext().popOutputBuffer(stringOutputBuffer);

        // Mark all elements in the output buffer as requiring to be transformed to text nodes.
        Node child = stringOutputBuffer.getRoot().getHead();
        
        while (child != null) {
            if (child instanceof Element) {
                domProtocol.setTransformToTextMarker((Element) child, true);
            }
            
            child = child.getNext();
        }
        
        // Transfer the content of the output buffer back to the current output buffer.
        getCurrentBuffer(protocol).addOutputBuffer(stringOutputBuffer);

        // Close the placeholder span element.
        protocol.writeCloseSpan(placeholderSpanAttributes);

        // Finally, prepare and render the JavaScript code, which will add the string.
        StringWriter scriptWriter = new StringWriter();
        
        if (attributes.getId() != null) {
            scriptWriter.write("Widget.register(" + createJavaScriptString(attributes.getId()) + ",");                  
        
            addCreatedWidgetId(attributes.getId());
        }
        
        scriptWriter.write("Widget.createString(" + createJavaScriptString(placeholderSpanAttributes.getId()) + ")");
        
        if (attributes.getId() != null) {
            scriptWriter.write(")");                  
        }
         
        // Write JavaScript content to DOM.
        try {
            getJavaScriptWriter().write(scriptWriter.toString());
        } catch (IOException e) {
            throw new ProtocolException(e);
        }
    }
}
