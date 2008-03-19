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

package com.volantis.mcs.protocols.response.renderers;

import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.response.attributes.ResponseTableBodyAttributes;

/**
 * Renderer for Table response.
 */
public class ResponseTableBodyDefaultRenderer extends BaseClientResponseDefaultRenderer {

    // Javadoc inherited
    public void doRenderOpenResponse(VolantisProtocol protocol,
        MCSAttributes attributes) throws ProtocolException {

        // Render opening of the String widget, which would hold content of table rows.
        renderOpenString(protocol, protocol.getMarinerPageContext().generateUniqueFCID());
    }
    
    // Javadoc inherited
    public void doRenderCloseResponse(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {
        
        // Render closure of the string widget. It'll now contain content of table rows.
        String stringId = renderCloseString(protocol);

        StringBuffer script = new StringBuffer();
        
        ResponseTableBodyAttributes tableAttributes = (ResponseTableBodyAttributes) attributes;

        if (tableAttributes.getId() != null) {
            script.append(createJavaScriptWidgetRegistrationOpening(tableAttributes.getId(), true));
        }
        
        int rowsCount = tableAttributes.getTotalRowsCount();
        
        script.append("new Widget.TableBodyResponse(")
            .append(rowsCount == -1 ? "null" : Integer.toString(rowsCount))
            .append(", ")
            .append(createJavaScriptWidgetReference(stringId, true))
            .append(")");
                                            
        if (tableAttributes.getId() != null) {
            script.append(createJavaScriptWidgetRegistrationClosure());
        }
        
        writeJavaScript(script.toString());
    }
}
