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

import java.util.HashMap;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.widgets.ActionName;
import com.volantis.mcs.protocols.widgets.EventName;
import com.volantis.mcs.protocols.widgets.PropertyName;
import com.volantis.mcs.protocols.widgets.attributes.LoadAttributes;
import com.volantis.mcs.protocols.widgets.attributes.TableBodyAttributes;
import com.volantis.mcs.themes.StyleInteger;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.runtime.scriptlibrarymanager.WidgetScriptModules;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Widget renderer for TableBody widget suitable for HTML protocols.
 */
public class TableBodyDefaultRenderer extends WidgetDefaultRenderer {
    /**
     * Array of supported action names.
     */
    private static final ActionName[] SUPPORTED_ACTION_NAMES =
        {
            ActionName.NEXT_PAGE,
            ActionName.PREVIOUS_PAGE,
            ActionName.NEXT_ROW,
            ActionName.PREVIOUS_ROW,
            ActionName.FIRST_PAGE,
            ActionName.LAST_PAGE
        };
    
    /**
     * Array of supported property names.
     */
    private static final PropertyName[] SUPPORTED_PROPERTY_NAMES =
        {
            PropertyName.ROWS_COUNT,
            PropertyName.START_ROW_NUMBER,
            PropertyName.END_ROW_NUMBER,
            PropertyName.LOAD,
            PropertyName.LOAD_ERROR_MESSAGE
        };
    
    /**
     * Array of supported event names.
     */
    private static final EventName[] SUPPORTED_EVENT_NAMES =
        {};
    
    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(TableBodyDefaultRenderer.class);

    // Javadoc inherited.
    public void doRenderOpen(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {

        if (!isWidgetSupported(protocol)) {
            return;
        }
   
        // Render TBODY element opening
        TableBodyAttributes tableBodyAttributes = (TableBodyAttributes) attributes;
        
        if (tableBodyAttributes.getXHTML2Attributes().getId() == null) {
            tableBodyAttributes.getXHTML2Attributes().setId(protocol.getMarinerPageContext().generateUniqueFCID());
        }
        
        protocol.writeOpenTableBody(tableBodyAttributes.getXHTML2Attributes());
    }

    // Javadoc inherited.
    public void doRenderClose(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {

        if (!isWidgetSupported(protocol)) {
            return;
        }

        // Render TBODY element closure.
        TableBodyAttributes tableBodyAttributes = (TableBodyAttributes) attributes;
        
        protocol.writeCloseTableBody(tableBodyAttributes.getXHTML2Attributes());
        
        // Prepare buffer for JavaScript
        StringBuffer script = new StringBuffer();
        
        String tableBodyId = attributes.getId();
        HashMap options = new HashMap();
        
        // Prepare options to render.
        StyleValue rowsPerPage = attributes.getStyles().getPropertyValues()
            .getSpecifiedValue(StylePropertyDetails.MCS_TABLE_ROWS_PER_PAGE);
        
        if (rowsPerPage instanceof StyleInteger) {
            options.put("rowsPerPage", Integer.toString(((StyleInteger) rowsPerPage).getInteger()));
        }
        
        LoadAttributes loadAttributes = tableBodyAttributes.getLoadAttributes();
        
        if (loadAttributes != null) {

            // require AJAX script module
            require(WidgetScriptModules.BASE_AJAX, protocol, attributes);
            
            HashMap loadOptions = new HashMap();

            loadOptions.put("src", createJavaScriptString(loadAttributes.getSrc()));

            String when = loadAttributes.getWhen();
            loadOptions.put("when", createJavaScriptString(when != null ? when : "onload"));
            
            StringBuffer loadScript = new StringBuffer();
            
            loadScript.append("new Widget.TableBodyLoad(")
                .append(createJavaScriptObject(loadOptions))
                .append(")");
            
            options.put("load", loadScript.toString());
        }
        
        // Initialize cache.
        if (tableBodyAttributes.getCachedPagesCount() != 0) {
            options.put("cachedPagesCount", Integer.toString(tableBodyAttributes.getCachedPagesCount()));
        }
        
        // Render JavaScript to buffer
        if (tableBodyId != null) {
            script.append(createJavaScriptWidgetRegistrationOpening(tableBodyId, true));
        }
        
        script.append("new Widget.TableBody(")
            .append(createJavaScriptString(tableBodyAttributes.getXHTML2Attributes().getId()))
            .append(", ")
            .append(createJavaScriptObject(options))
            .append(")");

        if (tableBodyId != null) {
            script.append(createJavaScriptWidgetRegistrationClosure());
        }
        
        writeJavaScript(script.toString());
    }
    
    // Javadoc inherited
    protected ActionName[] getSupportedActionNames() {
        return SUPPORTED_ACTION_NAMES;
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
