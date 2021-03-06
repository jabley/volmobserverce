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

import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.TableAttributes;
import com.volantis.mcs.protocols.VolantisProtocol;

/**
 * Renderer for CalendarDisplay element 
 */
public class CalendarDisplayDefaultRenderer extends WidgetDefaultRenderer {
    /**
     * Attributes of the table element containing calendar
     */
    private TableAttributes tableAttributes;

    // Javadoc inherited
    public void doRenderOpen(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {

        if(!isWidgetSupported(protocol)) {
            return;
        }        

        //TBody is created and appended on client side of widget
        tableAttributes = new TableAttributes();
        
        tableAttributes.copy(attributes);
        
        if (tableAttributes.getId() == null) {
            tableAttributes.setId(protocol.getMarinerPageContext().generateUniqueFCID());
        }
        protocol.writeOpenTable(tableAttributes);

    }

    // Javadoc inherited
    public void doRenderClose(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {

        if(!isWidgetSupported(protocol)) {
            return;
        }                
        
        protocol.writeCloseTable(tableAttributes);
        
        //register table id at date-picker renderer
        ((DatePickerDefaultRenderer)getWidgetDefaultModule(protocol).getDatePickerRenderer()).renderCalendarDisplay(tableAttributes.getId());
    }
    
    // Javadoc inherited
    public boolean shouldRenderContents(VolantisProtocol protocol, MCSAttributes attributes) {
        return isWidgetSupported(protocol);
    }
}

