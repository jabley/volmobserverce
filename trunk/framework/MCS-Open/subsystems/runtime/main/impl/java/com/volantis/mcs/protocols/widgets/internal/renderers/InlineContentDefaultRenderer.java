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

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.widgets.EventName;
import com.volantis.mcs.protocols.widgets.PropertyName;
import com.volantis.mcs.protocols.widgets.renderers.WidgetDefaultRenderer;
import com.volantis.mcs.runtime.scriptlibrarymanager.WidgetScriptModules;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Widget renderer for Content widget suitable for HTML protocols.
 */
public class InlineContentDefaultRenderer extends WidgetDefaultRenderer {
    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(BlockContentDefaultRenderer.class);
    
    /**
     * An ID of the JavaScript string.
     * TODO: Provide a stack to enable widget self-enclosing
     */
    private String stringID;

    // Javadoc inherited.
    public void doRenderOpen(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {

        // If protocol does not support Framework Client, do not render
        // anything.
        if (!isWidgetSupported(protocol)) {
            return;
        }

        require(WidgetScriptModules.BASE_BB_CONTENT, protocol, attributes);
        
        if (getWidgetDefaultModule(protocol).getParentContainerId() == null) {
            // If parent element is not a container, render this content as off-page.
            stringID = protocol.getMarinerPageContext().generateUniqueFCID();
        
            renderOpenString(protocol, stringID);

            openSpanElement(protocol, attributes);
        } else {
            // If parent element is a container, render this content directly on the page,
            // assigning ID for the element.
            openSpanElement(protocol, attributes, true);
        }
        
        getWidgetDefaultModule(protocol).openingContent();        
    }

    // Javadoc inherited.
    public void doRenderClose(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {

        // If protocol does not support Framework Client, do not render
        // anything.
        if (!isWidgetSupported(protocol)) {
            return;
        }

        getWidgetDefaultModule(protocol).closingContent(protocol);        

        Element spanElement = closeSpanElement(protocol);
        
        String parentContainerId = getWidgetDefaultModule(protocol).getParentContainerId();

        if (parentContainerId == null) {
            renderCloseString(protocol);
        }
        
        // Prepare Javascript content.
        StringBuffer buffer = new StringBuffer();
        
        // Finally, render the JavaScript part.
        if (attributes.getId() != null) {
            buffer.append(createJavaScriptWidgetRegistrationOpening(attributes.getId()));
            
            addCreatedWidgetId(attributes.getId());
        }
        
        buffer.append("new Widget.Internal.InlineContent(");
        
        if (parentContainerId == null) {
            buffer
                .append("null")
                .append(",")
                .append(createJavaScriptWidgetReference(stringID));

            addUsedWidgetId(stringID);
        } else {
            buffer
                .append(createJavaScriptString(spanElement.getAttributeValue("id")))
                .append(",")
                .append("null")
                .append(",")
                .append(createJavaScriptWidgetReference(parentContainerId));
            
            addUsedWidgetId(parentContainerId);
        }
        
        buffer.append(")");
        
        // Render closing parentheses for Widget.Register invokation
        if (attributes.getId() != null) {
            buffer.append(createJavaScriptWidgetRegistrationClosure());
        }
        
        // Write JavaScript content to DOM.
        writeJavaScript(buffer.toString());
    }
}
