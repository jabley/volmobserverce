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

import com.volantis.mcs.protocols.response.attributes.ResponseDeckAttributes;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.widgets.ResponseElements;

/**
 * Implementation of deck element from widgets-response namespace.
 */
public class ResponseDeckElement extends WidgetResponseElement {
    /**
     * Initializes this element with given XDIME context.
     * 
     * @param context An internal XDIME context.
     */
    public ResponseDeckElement(XDIMEContextInternal context){
        super(ResponseElements.DECK, context);
        
        protocolAttributes = new ResponseDeckAttributes();
    }
    
    // Javadoc inherited
    protected void initialiseElementSpecificAttributes(
            XDIMEContextInternal context, XDIMEAttributes attributes)
            throws XDIMEException {
        
        ResponseDeckAttributes responseDeckAttributes = (ResponseDeckAttributes) protocolAttributes;

        // Parse the "total-pages-count" attribute.
        String totalPagesCountAttribute = attributes.getValue("", "total-pages-count");
        
        if (totalPagesCountAttribute == null) {
            throw new XDIMEException("Missing total-pages-count attribute.");            
        }
        
        int totalPagesCount; 
        
        try {
            totalPagesCount = Integer.parseInt(totalPagesCountAttribute);
        } catch (NumberFormatException e) {
            throw new XDIMEException("Invalid value for total-pages-count attribute.");
        }
        
        responseDeckAttributes.setTotalPagesCount(totalPagesCount);
    }

}
