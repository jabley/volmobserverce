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
import java.io.StringWriter;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.widgets.attributes.LogAttributes;
import com.volantis.mcs.runtime.scriptlibrarymanager.WidgetScriptModules;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Widget renderer for Log widget suitable for HTML protocols.
 */
public class LogDefaultRenderer extends WidgetDefaultRenderer {
    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(LogDefaultRenderer.class);

    // Javadoc inherited.
    public void doRenderOpen(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {

        // If protocol does not support Framework Client, do not render
        // anything.
        if (!isWidgetSupported(protocol)) {
            return;
        }

        require(WidgetScriptModules.DEBUG, protocol, attributes);

        // If Log element does not have an ID, generate it.
        if (attributes.getId() == null) {
            attributes.setId(protocol.getMarinerPageContext()
                    .generateUniqueFCID());
        }

        // Eventually, open the <div> element with given ID.
        Element div = openDivElement(attributes.getStyles(), getCurrentBuffer(protocol));
        
        div.setAttribute("id", attributes.getId());
    }

    // Javadoc inherited.
    public void doRenderClose(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {

        // If protocol does not support Framework Client, do not render
        // anything.
        if (!isWidgetSupported(protocol)) {
            return;
        }

        LogAttributes logAttributes = (LogAttributes) attributes;
        
        // Close DIV element, which was opened in renderOpen().
        closeDivElement(getCurrentBuffer(protocol));
        
        // Write the script
        StringWriter scriptWriter = new StringWriter();

        scriptWriter.write("new Widget.Log({id:" + createJavaScriptString(logAttributes.getId()));
        
        if (logAttributes.getTopics() != null) {
            scriptWriter.write(",topics:" + createJavaScriptString(logAttributes.getTopics()));
        }

        scriptWriter.write("})");
        
        try {
            writeScriptElement(getCurrentBuffer(protocol), scriptWriter.toString());
        } catch (IOException e) {
            throw new ProtocolException(e);
        }
    }
}
