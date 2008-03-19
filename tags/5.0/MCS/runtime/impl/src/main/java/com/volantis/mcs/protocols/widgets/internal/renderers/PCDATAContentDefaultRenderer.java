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

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.widgets.EventName;
import com.volantis.mcs.protocols.widgets.PropertyName;
import com.volantis.mcs.protocols.widgets.renderers.WidgetDefaultRenderer;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Widget renderer for Content widget suitable for HTML protocols.
 */
public class PCDATAContentDefaultRenderer extends WidgetDefaultRenderer {
    /**
     * Array of supported property names.
     */
    private static final PropertyName[] SUPPORTED_PROPERTY_NAMES =
        {
            PropertyName.CONTAINER,
        };
    
    /**
     * Array of supported event names.
     */
    private static final EventName[] SUPPORTED_EVENT_NAMES =
        {
            EventName.ADDING,
            EventName.ADDED,
            EventName.REMOVING,
            EventName.REMOVED,
        };
    
    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(BlockContentDefaultRenderer.class);
    
    /**
     * A buffer for widget content.
     */
    private DOMOutputBuffer contentBuffer;

    // Javadoc inherited.
    public void doRenderOpen(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {

        // If protocol does not support Framework Client, do not render
        // anything.
        if (!isWidgetSupported(protocol)) {
            return;
        }

        // Inform of required JavaScript libraries.
        requireStandardLibraries(protocol);
        
        // Push buffer for widget content.
        contentBuffer = (DOMOutputBuffer) protocol.getOutputBufferFactory().createOutputBuffer();

        protocol.getMarinerPageContext().pushOutputBuffer(contentBuffer);
    }

    // Javadoc inherited.
    public void doRenderClose(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {

        // If protocol does not support Framework Client, do not render
        // anything.
        if (!isWidgetSupported(protocol)) {
            return;
        }

        protocol.getMarinerPageContext().popOutputBuffer(contentBuffer);

        String content = contentBuffer.getPCDATAValue();
        
        if (content == null) {
            content = "";
        }
        
        // Prepare Javascript content.
        StringWriter scriptWriter = new StringWriter();
        
        // Finally, render the JavaScript part.
        if (attributes.getId() != null) {
            scriptWriter.write("Widget.register(" + createJavaScriptString(attributes.getId()) + ",");
            
            addCreatedWidgetId(attributes.getId());
        }
        
        scriptWriter.write("new Widget.Internal.PCDATAContent(createJavaScriptString(" + content + "))");
        
        // Render closing parentheses for Widget.Register invokation
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
    
    // Javadoc inherited
    protected PropertyName[] getSupportedPropertyNames() {
        return SUPPORTED_PROPERTY_NAMES;
    }    

    // Javadoc inherited
    protected EventName[] getSupportedEventNames() {
        return SUPPORTED_EVENT_NAMES;
    }
}
