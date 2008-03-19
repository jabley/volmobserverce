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

package com.volantis.mcs.xdime.ticker.response;

import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.ticker.response.TickerResponseModule;
import com.volantis.mcs.protocols.ticker.response.attributes.AddItemAttributes;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.ticker.TickerResponseElements;

/**
 * AddItem response element.  
 */
public class AddItemElement extends TickerResponseElement {

    public AddItemElement(XDIMEContextInternal context) {
        super(TickerResponseElements.ADD_ITEM, context);
        
        protocolAttributes = new AddItemAttributes();
    }

    // Javadoc inherited
    public void callOpenOnModule(TickerResponseModule module) throws ProtocolException {
        module.openAddItem((AddItemAttributes) protocolAttributes);
    }

    // Javadoc inherited
    public void callCloseOnModule(TickerResponseModule module) throws ProtocolException {
        module.closeAddItem((AddItemAttributes) protocolAttributes);
    }
    
    // Javadoc inherited
    protected void initialiseElementSpecificAttributes(
            XDIMEContextInternal context, XDIMEAttributes attributes)
            throws XDIMEException {
        AddItemAttributes addItemAttributes = (AddItemAttributes) protocolAttributes;

        addItemAttributes.setItemId(attributes.getValue("", "item-id"));
        addItemAttributes.setChannel(attributes.getValue("", "channel"));
    }
}
