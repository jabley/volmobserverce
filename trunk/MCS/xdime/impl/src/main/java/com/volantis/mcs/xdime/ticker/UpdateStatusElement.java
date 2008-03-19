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

package com.volantis.mcs.xdime.ticker;

import com.volantis.mcs.protocols.ticker.attributes.UpdateStatusAttributes;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;

/**
 * FeedPoller element.
 */
public class UpdateStatusElement extends TickerElement {
       
    /**
     * Creates and returns new instance of FeedPollerElement, 
     * initalised with empty attributes.
     * @param context
     */
    public UpdateStatusElement(XDIMEContextInternal context) {
        super(TickerElements.UPDATE_STATUS, context);
        
        protocolAttributes = new UpdateStatusAttributes();        
    }  
    
    // Javadoc inherited
    public XDIMEResult callOpenOnProtocol(
            XDIMEContextInternal context, XDIMEAttributes attributes)
            throws XDIMEException {

        // Check, if this FeedPoller element has already been specified.
        checkFeedPollerSpecified(context);
        
        return super.callOpenOnProtocol(context, attributes);
    } 
}
