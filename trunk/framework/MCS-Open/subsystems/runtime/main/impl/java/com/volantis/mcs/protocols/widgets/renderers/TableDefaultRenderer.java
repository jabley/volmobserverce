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

import java.util.Set;
import java.util.HashSet;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.widgets.attributes.TableAttributes;
import com.volantis.mcs.runtime.scriptlibrarymanager.ScriptModule;
import com.volantis.mcs.runtime.scriptlibrarymanager.WidgetScriptModules;
import com.volantis.mcs.runtime.scriptlibrarymanager.ScriptModulesDefinitionRegistry;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Widget renderer for TableBody widget suitable for HTML protocols.
 */
public class TableDefaultRenderer extends WidgetDefaultRenderer {

    public static final ScriptModule MODULE = createAndRegisterModule();

    private static ScriptModule createAndRegisterModule() {
        Set dependencies = new HashSet();
        // always needed directly dependend module
        dependencies.add(WidgetScriptModules.BASE_BB_LOAD);
        dependencies.add(WidgetScriptModules.BASE_BB_CLIENT);
        ScriptModule module = new ScriptModule("/vfc-table.mscr", dependencies,
                20200, true);
        ScriptModulesDefinitionRegistry.register(module);
        return module;
    }

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(TableDefaultRenderer.class);

    // Javadoc inherited.
    public void doRenderOpen(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {

        // Render XHTML2 TABLE element opening
        TableAttributes tableAttributes = (TableAttributes) attributes;

        if (isWidgetSupported(protocol)) {
        
            require(MODULE, protocol, attributes);

            if (tableAttributes.getXHTML2Attributes().getId() == null) {
                tableAttributes.getXHTML2Attributes().setId(protocol.getMarinerPageContext().generateUniqueFCID());
            }
        }

        // open table element no matter widget is supported or not
        protocol.writeOpenTable(tableAttributes.getXHTML2Attributes());
    }

    // Javadoc inherited.
    public void doRenderClose(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {

        // Render XHTML2 TABLE element opening
        TableAttributes tableAttributes = (TableAttributes) attributes;

        // close table element no matter widget is supported or not 
        protocol.writeCloseTable(tableAttributes.getXHTML2Attributes());

        if (isWidgetSupported(protocol)) {

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
}
