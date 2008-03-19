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

import com.volantis.mcs.protocols.ticker.attributes.ItemsCountAttributes;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;

/**
 * ItemsCount element.
 */
public class ItemsCountElement extends TickerElement {
       
    /**
     * Creates and returns new instance of ItemsCountElement, 
     * initalised with empty attributes.
     * @param context
     */
    public ItemsCountElement(XDIMEContextInternal context) {
        super(TickerElements.ITEMS_COUNT, context);
        
        protocolAttributes = new ItemsCountAttributes();        
    }  
    
    // Javadoc inherited
    public XDIMEResult callOpenOnProtocol(
            XDIMEContextInternal context, XDIMEAttributes attributes)
            throws XDIMEException {

        // Check, if this FeedPoller element has already been specified.
        checkFeedPollerSpecified(context);
        
        return super.callOpenOnProtocol(context, attributes);
    }
    
    // Javadoc inherited
    protected void initialiseElementSpecificAttributes(XDIMEContextInternal context, XDIMEAttributes attributes) throws XDIMEException {
        ItemsCountAttributes itemsCountAttributes = (ItemsCountAttributes) protocolAttributes;
        
        itemsCountAttributes.setChannel(attributes.getValue("", "channel"));
        itemsCountAttributes.setRead(attributes.getValue("", "read"));
        itemsCountAttributes.setFollowed(attributes.getValue("", "followed"));
    }
}
