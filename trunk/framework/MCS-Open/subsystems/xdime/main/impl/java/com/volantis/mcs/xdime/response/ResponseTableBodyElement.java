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

package com.volantis.mcs.xdime.response;

import com.volantis.mcs.protocols.response.attributes.ResponseTableBodyAttributes;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.widgets.ResponseElements;

/**
 * Implementation of deck element from widgets-response namespace.
 */
public class ResponseTableBodyElement extends WidgetResponseElement {
    /**
     * Initializes this element with given XDIME context.
     * 
     * @param context An internal XDIME context.
     */
    public ResponseTableBodyElement(XDIMEContextInternal context){
        super(ResponseElements.TABLE_BODY, context);
        
        protocolAttributes = new ResponseTableBodyAttributes();
    }
    
    // Javadoc inherited
    protected void initialiseElementSpecificAttributes(
            XDIMEContextInternal context, XDIMEAttributes attributes)
            throws XDIMEException {
        
        ResponseTableBodyAttributes responseTableAttributes = (ResponseTableBodyAttributes) protocolAttributes;

        // Parse the "total-pages-count" attribute.
        String totalRowsCountAttribute = attributes.getValue("", "total-rows-count");
        
        if (totalRowsCountAttribute != null) {
            int totalRowsCount; 
        
            try {
                totalRowsCount = Integer.parseInt(totalRowsCountAttribute);
            } catch (NumberFormatException e) {
                throw new XDIMEException("Invalid value for total-rows-count attribute.");
            }
            
            if (totalRowsCount <= 0) {
                throw new XDIMEException("The value of total-rows-count attribute must be positive.");                
            }
        
            responseTableAttributes.setTotalRowsCount(totalRowsCount);
        }
    }

}
