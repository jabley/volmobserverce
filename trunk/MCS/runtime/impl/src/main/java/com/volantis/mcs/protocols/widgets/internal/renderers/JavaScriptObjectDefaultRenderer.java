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

import java.util.Iterator;
import java.util.Map;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.widgets.internal.attributes.JavaScriptObjectAttributes;
import com.volantis.mcs.protocols.widgets.renderers.WidgetDefaultRenderer;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Map renderer.
 * 
 * Renders a JavaScript object (a 'thing' enclosed within curly braces: {}).
 */
public class JavaScriptObjectDefaultRenderer extends WidgetDefaultRenderer {
    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(JavaScriptArrayDefaultRenderer.class);
    
    // Javadoc inherited.
    public void doRenderOpen(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {

        // If protocol does not support Framework Client, do not render
        // anything.
        if (!isWidgetSupported(protocol)) {
            return;
        }
    }

    // Javadoc inherited.
    public void doRenderClose(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {

        // If protocol does not support Framework Client, do not render
        // anything.
        if (!isWidgetSupported(protocol)) {
            return;
        }

        JavaScriptObjectAttributes mapAttributes = (JavaScriptObjectAttributes) attributes;
        
        StringBuffer buffer = new StringBuffer();
        
        if (attributes.getId() != null) {
            buffer.append(createJavaScriptWidgetRegistrationOpening(attributes.getId()));
            
            addCreatedWidgetId(attributes.getId());
        }
        
        buffer.append("{");
        
        Map map = mapAttributes.getWidgetsMap();
        
        Iterator iterator = map.keySet().iterator();
        
        while (iterator.hasNext()) {
            String name = (String) iterator.next();
            
            String id = (String) map.get(name);
            
            buffer.append(createJavaScriptString(name))
                .append(": ")
                .append(createJavaScriptWidgetReference(id));
            
            addUsedWidgetId(id);
            
            if (iterator.hasNext()) {
                buffer.append(", ");
            }
        }
        
        buffer.append("}");

        if (attributes.getId() != null) {
            buffer.append(createJavaScriptWidgetRegistrationClosure());
        }
        
        writeJavaScript(buffer.toString());
    }
}
