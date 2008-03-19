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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.response.renderers;

import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.response.attributes.ResponseDatePickerAttributes;

/**
 * Response renderer for DatePicker widget suitable for HTML protocol
 */
public class ResponseDatePickerDefaultRenderer extends
        BaseClientResponseDefaultRenderer {

    // Javadoc inherited    
    protected void doRenderCloseResponse(VolantisProtocol protocol,
        MCSAttributes attributes) throws ProtocolException {

        ResponseDatePickerAttributes responseDatePickerAttributes = (ResponseDatePickerAttributes) attributes;
            
        StringBuffer buffer = new StringBuffer();
        
        if (attributes.getId() != null) {
            buffer.append(createJavaScriptWidgetRegistrationOpening(attributes.getId()));            
            addCreatedWidgetId(attributes.getId());
        }
                            
        buffer.append("new Widget.Response.DatePicker(");

        String params[] = {
                responseDatePickerAttributes.getCurrentDate(),
                responseDatePickerAttributes.getRangeStart(),
                responseDatePickerAttributes.getRangeEnd()
        };
        
        for (int i = 0 ; i < params.length ; i++) {
            if (params[i] != null) {
                buffer.append("'" + params[i] + "'");
            } else {
                buffer.append("null");
            }
            if (i != params.length - 1) {
                buffer.append(",");
            }
        }
        
        buffer.append(")");
                
        if (attributes.getId() != null) {
            buffer.append(createJavaScriptWidgetRegistrationClosure());
        }        
        writeJavaScript(buffer.toString());        
    }

    // java inherited
    protected void doRenderOpenResponse(VolantisProtocol protocol, MCSAttributes attributes) throws ProtocolException {
        //TODO - generate ID shouldn't be mandatory, but exists because parent class not render any response  
        if (attributes.getId() == null) {
            attributes.setId(protocol.getMarinerPageContext().generateUniqueFCID());
        }        
    }
}
