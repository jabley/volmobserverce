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

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.widgets.attributes.TableAttributes;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Widget renderer for TableBody widget suitable for HTML protocols.
 */
public class TableDefaultRenderer extends WidgetDefaultRenderer {
    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(TableDefaultRenderer.class);

    // Javadoc inherited.
    public void doRenderOpen(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {

        if (!isWidgetSupported(protocol)) {
            return;
        }

        // Inform of required JavaScript libraries.
        requireStandardLibraries(protocol);
        requireLibrary("/vfc-table.mscr", protocol);

        // Render XHTML2 TABLE element opening
        TableAttributes tableAttributes = (TableAttributes) attributes;
        
        if (tableAttributes.getXHTML2Attributes().getId() == null) {
            tableAttributes.getXHTML2Attributes().setId(protocol.getMarinerPageContext().generateUniqueFCID());
        }
        
        protocol.writeOpenTable(tableAttributes.getXHTML2Attributes());
    }

    // Javadoc inherited.
    public void doRenderClose(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {

        if (!isWidgetSupported(protocol)) {
            return;
        }

        // Render XHTML2 TABLE element opening
        TableAttributes tableAttributes = (TableAttributes) attributes;
        
        protocol.writeCloseTable(tableAttributes.getXHTML2Attributes());

        // Prepare buffer for JavaScript
        StringBuffer script = new StringBuffer();
        
        // Render JavaScript to buffer
        if (attributes.getId() != null) {
            script.append(createJavaScriptWidgetRegistrationOpening(attributes.getId(), true));
        }
        
        script.append("new Widget.Table(")
            .append(createJavaScriptString(tableAttributes.getXHTML2Attributes().getId()))
            .append(")");

        if (attributes.getId() != null) {
            script.append(createJavaScriptWidgetRegistrationClosure());
        }
        
        writeJavaScript(script.toString());
    }
}
